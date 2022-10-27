package com.example.liberateapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.health.ServiceHealthStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.liberateapp.R;
import com.example.liberateapp.modelo.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TEXT = "tipo";
    private static final String MAIL = "correo";

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton btnUpload = (FloatingActionButton) view.findViewById(R.id.buttonUpload);
        Button btnRevistas = (Button) view.findViewById(R.id.buttonRevistas);
        Button btnInformes = (Button) view.findViewById(R.id.buttonInformes);
        Button btnCapacitaciones = (Button) view.findViewById(R.id.buttonCapacitaciones);
        Button btnBoletines = (Button) view.findViewById(R.id.buttonBoletines);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(ds.child("email").getValue().toString().equals(user.getEmail())){
                            setSharedPrefs(MAIL, user.getEmail());
                            if(ds.child("admin").getValue().toString().equals("si")){
                                btnUpload.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpload.setOnClickListener(view1 -> {
            FragmentUpload fragmentUpload = new FragmentUpload();
            loadFragment(fragmentUpload);
        });
        btnRevistas.setOnClickListener(view1 -> {
            setSharedPrefs(TEXT,"Revistas");
            FragmentRevistas fragmentRevistas = new FragmentRevistas();
            loadFragment(fragmentRevistas);
        });
        btnBoletines.setOnClickListener(view1 -> {
            setSharedPrefs(TEXT, "Boletines");
            FragmentBoletines fragmentBoletines = new FragmentBoletines();
            loadFragment(fragmentBoletines);
        });
        btnCapacitaciones.setOnClickListener(view1 -> {
            setSharedPrefs(TEXT, "Capacitaciones");
            FragmentCapacitaciones fragmentCapacitaciones = new FragmentCapacitaciones();
            loadFragment(fragmentCapacitaciones);
        });
        btnInformes.setOnClickListener(view1 -> {
            setSharedPrefs(TEXT, "Informes");
            FragmentInformes fragmentInformes = new FragmentInformes();
            loadFragment(fragmentInformes);
        });
        return view;
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
    public void setSharedPrefs(String tipo_dato, String data){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tipo_dato, data);
        editor.apply();
    }

}