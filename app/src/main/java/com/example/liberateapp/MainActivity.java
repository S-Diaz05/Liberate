package com.example.liberateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button inicioSesion;
    Button registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicioSesion = (Button) findViewById(R.id.buttonIrIniciarSesion);
        inicioSesion.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, inicio_sesion_activity.class)));
        registro = (Button) findViewById(R.id.buttonIrRegristrar);
        registro.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, registrar_usuario_activity.class)));
    }


}