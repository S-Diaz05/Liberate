package com.example.liberateapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liberateapp.R;
import com.example.liberateapp.modelo.Archivo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FragmentUpload extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String nombre;
    private String tipo;
    private String extension;

    Spinner spinnerFileType;
    Spinner spinnerExtension;

    private final int STORAGE_PERMISSION_CODE = 1;

    public FragmentUpload() {
        // Required empty public constructor
    }

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
        spinnerFileType = (Spinner) view.findViewById(R.id.spinnerFilesType);
        spinnerExtension = (Spinner) view.findViewById(R.id.spinnerExtension);
        spinners();
        permisos();

        EditText editText = (EditText) view.findViewById(R.id.editText_nombreArchivo);
        selectBtn.setOnClickListener(view1 -> {

            if(!validar(editText.getText().toString())){
                editText.setError("Nombre no válido");
            }
            else if(!editText.getText().toString().equals("") && !spinnerFileType.getSelectedItem().toString().equals("")
                    && !spinnerExtension.getSelectedItem().toString().equals("")){
                nombre = editText.getText().toString();
                tipo = spinnerFileType.getSelectedItem().toString();
                extension = spinnerExtension.getSelectedItem().toString();
                nombre = nombre+"."+extension;
                selectFile();
            }
            else{
                Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }

    /**
     * Llenar spinners con la información
     */
    public void spinners(){
        ArrayList<String> fileTypes = new ArrayList<>();
        ArrayList<String> fileExtension = new ArrayList<>();

        fileTypes.add("");
        fileTypes.add("Boletines");
        fileTypes.add("Capacitaciones");
        fileTypes.add("Informes");
        fileTypes.add("Revistas");

        fileExtension.add("");
        fileExtension.add("csv");
        fileExtension.add("docx");
        fileExtension.add("gif");
        fileExtension.add("jpg");
        fileExtension.add("jpeg");
        fileExtension.add("pdf");
        fileExtension.add("pptx");
        fileExtension.add("png");
        fileExtension.add("xlsx");

        ArrayAdapter<String >adapter = new ArrayAdapter<>(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, fileTypes);
        spinnerFileType.setAdapter(adapter);

        ArrayAdapter<String >adapter2 = new ArrayAdapter<>(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, fileExtension);
        spinnerExtension.setAdapter(adapter2);
    }

    /**
     * Verficar que el usuario tenga permisos para almacenamiento
     * de lo contrario pedir por ellos
     * @return acepto permisos
     */
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
            return false;
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
        return true;
    }

    /**
     * Validar que el nombre del archivo sea válido
     * @param nombreArchivo
     * @return si es válido
     */
    public boolean validar(String nombreArchivo){
        return nombreArchivo.matches("[a-z A-Z 0-9]+");
    }
    public Intent selectFile(){
        Intent i = new Intent();
        i.setType("application/"+extension); //Tipo de archivo
        i.setAction(Intent.ACTION_GET_CONTENT);
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
            Toast.makeText(getContext(), "No se pudo seleccionar un archivo", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Subir archivo a la nube
     * @param uri dirección del archivo
     */
    public void uploadFile(Uri uri){
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
                    Archivo archivo = new Archivo(nombre, url.toString(), tipo, extension);

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