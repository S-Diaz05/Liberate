package com.example.liberateapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liberateapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUpload#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUpload extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Uri uri;
    public FragmentUpload() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUpload.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUpload newInstance(String param1, String param2) {
        FragmentUpload fragment = new FragmentUpload();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_upload, container, false);
        FirebaseDatabase firebaseDatabase;
        FirebaseStorage firebaseStorage;

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Button selectBtn = (Button) view.findViewById(R.id.buttonSelect);
        Button uploadBtn = (Button) view.findViewById(R.id.buttonUploadFile);
        TextView notificacion = (TextView) view.findViewById(R.id.textViewSelectedFile);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uri = selectFile().getData();

            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null){
                    uploadFile(uri, firebaseStorage, firebaseDatabase);
                }else{
                    Toast.makeText(getContext(), "Seleccione un archivo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


    public Intent selectFile(){
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 86);
        return i;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==86 && resultCode==RESULT_OK && data != null){
            uri = data.getData();
           // Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
    }
    public void uploadFile(Uri uri, FirebaseStorage storageReference, FirebaseDatabase firebaseDatabase){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Cargando archivo...");
        progressDialog.setProgress(0);
        progressDialog.show();

        String fileName = System.currentTimeMillis()+"";
        StorageReference reference = storageReference.getReference();
        reference.child("Files").child(fileName).putFile(uri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        DatabaseReference referenceDB  = firebaseDatabase.getReference();

                        referenceDB.child(fileName).setValue(url).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Archivo cargado exitosamente", Toast.LENGTH_SHORT).show();
                                    }else
                                        Toast.makeText(getContext(), "Archivo no se puedo cargar", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Archivo no se puedo cargar", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        int progress = (int) ((int) (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount());
                        progressDialog.setProgress(progress);
                    }
                });
    }
}