package com.example.a0411;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import telas.Apresentacao;
import telas.TelaInicio;

public class MainActivity extends AppCompatActivity {

    private Button avanca;
    private EditText dataNiver, nome;
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference dadosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avanca = findViewById(R.id.buttonAvancarCad);
        dataNiver = (EditText) findViewById(R.id.dataNiver);
        nome = (EditText) findViewById(R.id.textNome);

        if (usuario.getCurrentUser()==null) {

        }else{
            telaPricipal();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(getColor(R.color.notificacao));
        }

    }

    public void telaPricipal(){

        Intent intent = new Intent(this, TelaInicio.class);
        startActivity(intent);
        finish();

    }

    public void apresentacao(View view){

        if(nome.getText().toString().equals("")){
            Toast.makeText(this, "Adicione o nome.", Toast.LENGTH_SHORT).show();
        }else{

        if (dataNiver.getText().toString().equals("")){
            Toast.makeText(this, "Adicione a data do aniversário.", Toast.LENGTH_SHORT).show();
            }else{

            salvarNomeNiver();

            usuario.signInWithEmailAndPassword("henlima12@gmail.com","1365812177").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.i("signIN","Sucesso ao logar");
                    }else{
                        Log.i("signIN","Erro ao logar");
                    }
                }
            });

            Intent intent = new Intent(this, Apresentacao.class);
            startActivity(intent);
            finish();
        }
    }
    }


    public void salvarNomeNiver(){

        dadosUsuario = databaseReference.child("usuario");

        dadosUsuario.child("nome").setValue(nome.getText().toString());
        dadosUsuario.child("data aniversario").setValue(dataNiver.getText().toString());
    }
}