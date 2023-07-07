package com.rodrigo.tucano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rodrigo.tucano.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> {

            try {
                mAuth.signInWithEmailAndPassword(binding.edtTextEmail.getText().toString(), binding.edtTextSenha.getText().toString())
                        .addOnCompleteListener(
                                this, task -> {
                                    if (task.isSuccessful()) {
                                        Log.e("Teste", "Sucesso");
                                        startActivity(new Intent(this, MainActivity.class));
                                        Toast.makeText(this, "login com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        try {

                                            Log.e("Teste", "Falha");
                                            Log.e("Teste", task.getResult().toString());
                                            Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e){
                                            Toast.makeText(this, "Revise os campos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
            } catch (Exception e){
                Toast.makeText(this, "Revise os campos", Toast.LENGTH_SHORT).show();
            }
        });

    }
}