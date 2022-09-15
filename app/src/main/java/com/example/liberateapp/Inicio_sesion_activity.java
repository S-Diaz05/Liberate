package com.example.liberateapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Inicio_sesion_activity extends AppCompatActivity {
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
    }
/*
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
                            Toast.makeText(Inicio_sesion_activity.this, "Error en el inicio ", Toast.LENGTH_LONG).show();
                        }
                    });


                }
                else{
                    Toast.makeText(Inicio_sesion_activity.this, "Error en el login a la app, revisa tus credenciales", Toast.LENGTH_LONG).show();

                }
            }
        });
        startActivity(new Intent(this, Home_activity.class));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                userLogin();
                break;
        }
    }*/
}