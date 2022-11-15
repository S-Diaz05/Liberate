package com.example.liberateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.liberateapp.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * Inicio de sesión
 */
public class inicio_sesion_activity extends AppCompatActivity {

    Button logIn;
    EditText editPassword;
    EditText editCorreo ;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);


        editCorreo = (EditText) findViewById(R.id.editText_correo);
        editPassword = (EditText) findViewById(R.id.editText_password);

        mAuth = FirebaseAuth.getInstance();
        logIn = (Button) findViewById(R.id.login_btn);


        logIn.setOnClickListener(view -> userLogin());
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Se verfica que los campos esten llenados con información correcta
     */
    public void userLogin(){
        String email = editCorreo.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        if(email.isEmpty()){
            editCorreo.setError("Se requiere correo");
            editCorreo.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editCorreo.setError("Ingrese un correo válido");
            editCorreo.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("Se requiere contraseña");
            editPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editPassword.setError("La contraseña es de mínimo 6 caracteres");
            editPassword.requestFocus();
            return;
        }
        verficarUsuario(email, password);

    }

    /**
     * Verificar que las credenciales sean válidas y de ser asi, iniciar sesión
     * @param email
     * @param password
     */
    public void verficarUsuario(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Usuarios");
                assert usuario != null;
                idUsuario = usuario.getUid();

                reference.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Usuario perfilUsuario = snapshot.getValue(Usuario.class);


                        FirebaseDatabase.getInstance().getReference("Usuarios")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(perfilUsuario);

                        startActivity(new Intent(getBaseContext(), barra_navegacion_activity.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(inicio_sesion_activity.this, "Error en el inicio ", Toast.LENGTH_LONG).show();
                    }
                });

            }
            else{
                Toast.makeText(inicio_sesion_activity.this, "Error en el inicio de sesión, revisa tus credenciales", Toast.LENGTH_LONG).show();

            }
        });

    }

}