package com.example.liberateapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrar_usuario_activity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private EditText editNombre, editPassword, editCorreo, editConfirmarPassword;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        editNombre = (EditText) findViewById(R.id.editText_nombreRegistro);
        editCorreo = (EditText) findViewById(R.id.editText_correoRegistro);
        editPassword = (EditText) findViewById(R.id.editText_passwordRegistro);
        editConfirmarPassword = (EditText) findViewById(R.id.editText_passwordRegistro2);

        registrar = (Button) findViewById(R.id.buttonRegistro);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registro()){
                    Toast.makeText(Registrar_usuario_activity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registrar_usuario_activity.this, MainActivity.class));
                }else{
                    Toast.makeText(Registrar_usuario_activity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean registro(){
        String email = editCorreo.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String verificarPassword = editConfirmarPassword.getText().toString().trim();

        if(!verficar(email, nombre, password, verificarPassword)){
            return false;
        }

        Toast.makeText(Registrar_usuario_activity.this, email + password, Toast.LENGTH_SHORT).show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Registrar_usuario_activity.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(Registrar_usuario_activity.this, "Error en el registro, intente de nuevo más adelante", Toast.LENGTH_LONG).show();
                }


            }
        });

        return true;
    }
    public boolean verficar(String email, String nombre, String password, String verificarPassword){
        if(nombre.isEmpty()){
            editNombre.setError("Se requiere nombre completo");
            editNombre.requestFocus();
            return false;
        }
        if(email.isEmpty()){
            editCorreo.setError("Se requiere nombre completo");
            editCorreo.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editCorreo.setError("Correo no válido");
            editCorreo.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            editPassword.setError("Se requiere contraseña");
            editPassword.requestFocus();
            return false;
        }

        if(password.length()<6){
            editPassword.setError("Escriba una contaseña de mas de 6 dígitos");
            editPassword.requestFocus();
            return false;
        }
        if(!password.equals(verificarPassword)){
            editPassword.setError("Las contraseñas no son idénticas");
            editPassword.requestFocus();
            return false;
        }
        Pattern pattern = Pattern.compile("@fundacionliberate.org.co", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if(!matcher.find()){
            editCorreo.setError("Correo inválido");
            editPassword.requestFocus();
            return false;
        }
        return true;
    }
}