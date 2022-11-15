package com.example.liberateapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.liberateapp.fragments.FragmentHome;
import com.example.liberateapp.fragments.FragmentPerfil;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class barra_navegacion_activity extends AppCompatActivity {

    FragmentPerfil fragmentPerfil = new FragmentPerfil();
    FragmentHome fragmentHome = new FragmentHome();
    /**
     * Actvidad que permite moverse entre la actividad home, donde estan las diferentes categorias de archivos
     * y en el caso de ser administrador, dirigirse a la pantalla de subir
     * y la actividad perfil, donde aparece información del usuario y cerrar sesión
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barra_navegacion);
        loadFragment(fragmentHome);
        BottomNavigationView navegacion = findViewById(R.id.bottom_navigation);
        navegacion.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


    }
    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()){
                    case R.id.fragmentPerfil:
                        loadFragment(fragmentPerfil);
                        return true;
                    case R.id.fragmentHome:
                        loadFragment(fragmentHome);
                        return true;
                }
                return false;
            };

    /**
     * Dirige al usuario a la siguente pantalla
     * @param fragment nombre de la pantalla
     */
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}