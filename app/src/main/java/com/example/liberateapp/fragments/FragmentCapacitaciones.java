package com.example.liberateapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liberateapp.R;
import com.example.liberateapp.adapters.AdaptadorArchivos;
import com.example.liberateapp.modelo.Archivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Archivos de tipo capacitaciones
 */
public class FragmentCapacitaciones extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth auth;
    private RecyclerView mRecycler;
    private ArrayList<Archivo> archivosList = new ArrayList<>();
    AdaptadorArchivos adapter;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "tipo";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCapacitaciones() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentCapacitaciones newInstance(String param1, String param2) {
        FragmentCapacitaciones fragment = new FragmentCapacitaciones();
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
        View view =  inflater.inflate(R.layout.fragment_capacitaciones, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerViewCapacitaciones);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        auth = FirebaseAuth.getInstance();

        getArchivosFirebase();

        return  view;
    }
    /**
     * Listar los archivos
     */
    public void getArchivosFirebase(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String tipo = sharedPreferences.getString(TEXT, "No existe ");
        myRef.child("Archivos").child(tipo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    archivosList.clear();

                    for (DataSnapshot ds: snapshot.getChildren()) {

                        String nombre = ds.child("nombre").getValue().toString();
                        String url = ds.child("url").getValue().toString();
                        String extension = ds.child("extension").getValue().toString();
                        archivosList.add(new Archivo(nombre, url,"Capacitaciones", extension));

                    }
                    adapter = new AdaptadorArchivos(archivosList, R.layout.view_archivos);
                    mRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}