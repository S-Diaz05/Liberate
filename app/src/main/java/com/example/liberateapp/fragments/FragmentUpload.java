package com.example.liberateapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liberateapp.R;
import com.example.liberateapp.modelo.Archivo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private String nombre;
    private String tipo;

    private final int STORAGE_PERMISSION_CODE = 1;

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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_upload, container, false);

        Button selectBtn = (Button) view.findViewById(R.id.buttonSelect);
        Spinner spinnerFileType = (Spinner) view.findViewById(R.id.spinnerFilesType);
        ArrayList<String> fileTypes = new ArrayList<>();
        permisos();
        fileTypes.add("");
        fileTypes.add("Informes");
        fileTypes.add("Boletines");
        fileTypes.add("Revistas");
        fileTypes.add("Capacitaciones");

        ArrayAdapter<String >adapter = new ArrayAdapter<>(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, fileTypes);
        spinnerFileType.setAdapter(adapter);

        EditText editText = (EditText) view.findViewById(R.id.editText_nombreArchivo);
        selectBtn.setOnClickListener(view1 -> {


                Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(doc|csv|pdf|jpg|jpeg|docx|pptx|ppt|gif))$)");
                Matcher matcher = pattern.matcher(editText.getText().toString());

                if(!matcher.find() && !editText.getText().toString().equals("")){
                    editText.setError("Falta agregar extensión del archivo o no es válido");
                }else if(!editText.getText().toString().equals("") && !spinnerFileType.getSelectedItem().toString().equals("")){
                    nombre = editText.getText().toString();
                    tipo = spinnerFileType.getSelectedItem().toString();
                    selectFile();
                }else if(!matcher.find()){
                    editText.setError("Extensión no válida");
                }
                else{
                    Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }

        });


        return view;
    }

    public boolean permisos(){
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(getContext()).setTitle("Permisos necesarios").
                    setMessage("El permiso es necesario para continuar con la acción").
                    setPositiveButton("ok", (dialogInterface, i) -> ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE)).setNegativeButton("Cancelado", (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
        return true;
    }
    public Intent selectFile(){
        Intent i = new Intent();
        i.setType(tipo);
        i.setAction(Intent.ACTION_GET_CONTENT);
        Toast.makeText(getContext(), "Llego aqui", Toast.LENGTH_SHORT).show();
        startActivityForResult(Intent.createChooser(i, "Seleccione un archivo") , 1);
        return i;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Permiso dado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==1 && resultCode==RESULT_OK && data != null&& data.getData() != null){
           uploadFile(data.getData());
        }else {
            Toast.makeText(getContext(), "Seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
    }
    public void uploadFile(Uri uri){

        Toast.makeText(getContext(), nombre, Toast.LENGTH_SHORT).show();
        StorageReference storageReference;
        DatabaseReference firebaseDatabase;
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Archivos");

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Cargando archivo...");
        progressDialog.setProgress(0);
        progressDialog.show();
        StorageReference reference = storageReference.child("Archivos/"+nombre);

        reference.putFile(uri).
                addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri url = uriTask.getResult();


                    Archivo archivo = new Archivo(nombre, url.toString(), tipo);

                    firebaseDatabase.child(archivo.getTipo()).child(firebaseDatabase.push().getKey()).setValue(archivo);
                    Toast.makeText(getContext(), "Archivo cargado exitosamente", Toast.LENGTH_SHORT).show();

                    TextView notificacion = (TextView) getView().findViewById(R.id.textViewSelectedFile);
                    notificacion.setText(nombre);

                }).addOnFailureListener(e -> Toast.makeText(getContext(), "Archivo no se puedo cargar", Toast.LENGTH_SHORT).show()).
                addOnProgressListener(snapshot -> {
                    int progress = (int) ((int) (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount());
                    progressDialog.setProgress(progress);
                });
    }
}