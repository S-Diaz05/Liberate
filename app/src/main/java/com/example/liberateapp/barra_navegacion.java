package com.example.liberateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.liberateapp.fragments.FragmentPerfil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class barra_navegacion extends AppCompatActivity {

    FragmentPerfil fragmentPerfil = new FragmentPerfil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barra_navegacion);

        BottomNavigationView navegacion = findViewById(R.id.bottom_navigation);
        navegacion.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }
    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.fragmentPerfil:
                            loadFragment(fragmentPerfil);
                            return true;

                    }

                    return false;
                }
            };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}