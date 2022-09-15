package com.example.liberateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.liberateapp.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class inicio_sesion_activity extends AppCompatActivity {

    Button logIn;
    EditText editPassword;
    EditText editCorreo ;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String idUsuario;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        // logIn.setOnClickListener(this);
        editCorreo = (EditText) findViewById(R.id.editText_correo);
        editPassword = (EditText) findViewById(R.id.editText_password);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        mAuth = FirebaseAuth.getInstance();
        logIn = (Button) findViewById(R.id.login_btn);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    public void userLogin(){
        String email = editCorreo.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        if(email.isEmpty()){
            editCorreo.setError("Se requiere correo");
            editCorreo.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editCorreo.setError("Porfavor ingrese un correo valido");
            editCorreo.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("Se requiere contraseña");
            editPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editPassword.setError("La contraseña es de minimo 6 caracteres");
            editPassword.requestFocus();
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //verificar correo
                    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Usuarios");
                    idUsuario = usuario.getUid();

                    reference.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario perfilUsuario = snapshot.getValue(Usuario.class);


                            FirebaseDatabase.getInstance().getReference("Usuarios")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(perfilUsuario);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(inicio_sesion_activity.this, "Error en el inicio ", Toast.LENGTH_LONG).show();
                        }
                    });


                }
                else{
                    Toast.makeText(inicio_sesion_activity.this, "Error en el login a la app, revisa tus credenciales", Toast.LENGTH_LONG).show();

                }
            }
        });
        startActivity(new Intent(this, home_activity.class));
    }

}