package com.example.darpan.tester;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.GONE;

public class Main3Activity extends AppCompatActivity {
    EditText logemail,logpass;
    private static final String TAG = "Main3Activity";
    ProgressBar progressBar2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        logemail=findViewById(R.id.Log_email);
        logpass=findViewById(R.id.Log_password);
        progressBar2=findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(View view) {
        progressBar2.setVisibility(View.VISIBLE);
        String email=logemail.getText().toString().trim();
        String password=logpass.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "Welcome back!");
                            progressBar2.setVisibility(View.GONE);
                            Intent i=new Intent(Main3Activity.this,Main5Activity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(Main3Activity.this, "cannot login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
