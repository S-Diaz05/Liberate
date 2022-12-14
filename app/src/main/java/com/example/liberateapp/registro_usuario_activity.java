package com.example.liberateapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.liberateapp.modelo.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Registrar usuario
 */
public class registro_usuario_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editNombre, editPassword, editCorreo, editConfirmarPassword;
    Button registroBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setContentView(R.layout.activity_registrar_usuario);
        editNombre = (EditText) findViewById(R.id.editText_nombreRegistro);
        editCorreo = (EditText) findViewById(R.id.editText_correoRegistro);
        editPassword = (EditText) findViewById(R.id.editText_passwordRegistro);
        editConfirmarPassword = (EditText) findViewById(R.id.editText_passwordRegistro2);

        registroBtn = (Button) findViewById(R.id.buttonRegistro);
        registroBtn.setOnClickListener(view -> {
            if(registro()){
                Toast.makeText(registro_usuario_activity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(registro_usuario_activity.this, MainActivity.class));
            }else{
                Toast.makeText(registro_usuario_activity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Registro de usuario en la nube
     * @return si el usuario se pudo crear
     */
    public boolean registro(){
        String email = editCorreo.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String verificarPassword = editConfirmarPassword.getText().toString().trim();

        if(!verficar(email, nombre, password, verificarPassword)){
            return false;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Usuario u = new Usuario(UUID.randomUUID().toString(), email, nombre, "no");
                try{
                    databaseReference.child("Users").child(u.getUuid()).setValue(u);
                    Toast.makeText(registro_usuario_activity.this, "Se registro correctamente", Toast.LENGTH_LONG).show();
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(registro_usuario_activity.this, "Error: "+e, Toast.LENGTH_LONG).show();
                }
                finish();
            }else{
                Toast.makeText(registro_usuario_activity.this, "Error en el registro, intente de nuevo m??s adelante", Toast.LENGTH_LONG).show();
            }
        });

        return true;
    }

    /**
     * Verfica que la informaci??n permita crear la cuenta
     * @param email
     * @param nombre
     * @param password
     * @param verificarPassword
     * @return si el usuario se puede crear despues de aceptar la informaci??n dada
     */
    public boolean verficar(String email, String nombre, String password, String verificarPassword){
        if(nombre.isEmpty()){
            editNombre.setError("Se requiere nombre completo");
            editNombre.requestFocus();
            return false;
        }
        if(email.isEmpty()){
            editCorreo.setError("Se requiere un correo electr??nico");
            editCorreo.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editCorreo.setError("Correo no v??lido");
            editCorreo.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            editPassword.setError("Se requiere contrase??a");
            editPassword.requestFocus();
            return false;
        }

        if(password.length()<6){
            editPassword.setError("Escriba una contase??a de mas de 6 d??gitos");
            editPassword.requestFocus();
            return false;
        }
        if(!password.equals(verificarPassword)){
            editPassword.setError("Las contrase??as no son id??nticas");
            editPassword.requestFocus();
            return false;
        }
        Pattern pattern = Pattern.compile("@fundacionliberate.org.co", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if(!matcher.find()){
            editCorreo.setError("Correo inv??lido");
            editPassword.requestFocus();
            return false;
        }
        return true;
    }
}