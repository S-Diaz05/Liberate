package com.example.liberateapp.adapters;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liberateapp.R;
import com.example.liberateapp.modelo.Archivo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Lista archivos según el tipo y agregar botón de descarga y en el caso de ser administrador
 * botón de eliminar
 */
public class AdaptadorArchivos extends RecyclerView.Adapter<AdaptadorArchivos.ViewHolder> {
    private final int resource;
    private final ArrayList<Archivo> archivoList;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "tipo";
    public static final String MAIL = "correo";
    String tipo;
    String mail;
    FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public AdaptadorArchivos(ArrayList<Archivo> archivos, int resource){
        this.resource = resource;
        this.archivoList = archivos;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        auth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        tipo = sharedPreferences.getString(TEXT, "No existe ");
        mail = sharedPreferences.getString(MAIL, "No existe");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Archivo archivo = archivoList.get(position);

        holder.textName.setText(archivo.getNombre());
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(ds.child("email").getValue().toString().equals(mail)){
                            if(ds.child("admin").getValue().toString().equals("si")){
                                holder.btn_eliminar.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.btn_eliminar.setOnClickListener(view -> {

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Archivos").child(tipo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(ds.child("nombre").getValue().toString().equals(archivo.getNombre())){
                                if(confirmDelete()){

                                    firebaseStorage = FirebaseStorage.getInstance();
                                    storageReference = firebaseStorage.getReference();
                                    StorageReference reference = storageReference.child("Archivos").child(archivo.getNombre());
                                    reference.delete().addOnSuccessListener(unused -> {
                                        databaseReference.child("Archivos").child(snapshot.getKey()).child(ds.getKey()).removeValue();
                                        Toast.makeText(view.getContext(), "El documento se eliminó existosamente", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(fail -> {
                                        Toast.makeText(view.getContext(), "El documento no se pudo eliminar", Toast.LENGTH_SHORT).show();

                                    });

                                }
                               }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(view.getContext(), "El archivo no se pudo eliminar", Toast.LENGTH_SHORT).show();

                }
            });
        });
        holder.btn_download.setOnClickListener(view -> download(archivo, view));
    }

    /**
     * Confirmar eliminar archivo
     * TODO
     * @return
     */
    //TODO
    public boolean confirmDelete(){


        return true;
    }

    /**
     * Descarga archivo
     * @param archivo
     * @param view
     */
    public void download(Archivo archivo, View view){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        StorageReference reference = storageReference.child("Archivos").child(archivo.getNombre());
        reference.getDownloadUrl().addOnSuccessListener(uri -> downloadFiles(view.getContext(), archivo.getNombre(),DIRECTORY_DOWNLOADS, uri.toString() )).
                addOnFailureListener(e -> {       });
    }

    /**
     * Descarga archivos
     * @param context
     * @param fileName
     * @param destinationDirectory
     * @param url
     */
    public void downloadFiles(Context context, String fileName, String destinationDirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request= new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName);
        Toast.makeText(context, "El documento comenzo la descarga", Toast.LENGTH_SHORT).show();
        downloadManager.enqueue(request);
    }
    @Override
    public int getItemCount() {
        return archivoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView  textName;
        ImageButton btn_eliminar, btn_download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textName = (TextView) itemView.findViewById(R.id.nombre_ubi);
            this.btn_eliminar = (ImageButton) itemView.findViewById(R.id.btn_delete);
            this.btn_download = (ImageButton) itemView.findViewById(R.id.btn_download);
        }

    }

}
