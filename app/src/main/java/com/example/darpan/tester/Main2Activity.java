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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    EditText email,name,password;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.Reg_email);
        name=findViewById(R.id.Reg_name);
        password=findViewById(R.id.Reg_password);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    public void createAccount(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String email1=email.getText().toString().trim();
        String pass=password.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email1, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            CreateInFireStore();
                            Log.d(TAG, "createUserWithEmail:success");
                            progressBar.setVisibility(View.GONE);
                            Intent i = new Intent(Main2Activity.this,Main3Activity.class);
                            startActivity(i);
                            //String uid = user.getEmail();
                        }else {
                            Toast.makeText(Main2Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
    public void CreateInFireStore(){
        Map<String,Object> upoload = new HashMap<>();
        upoload.put("name",name.getText().toString());
        upoload.put("email",email.getText().toString());
        upoload.put("password",password.getText().toString());

        db.collection("Register").document(email.getText().toString()).set(upoload)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Main2Activity.this, "Successfully added", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
        Map<String,Object> addFirstUser=new HashMap<>();
        addFirstUser.put("numOfUsers",1);
        addFirstUser.put("user1","rasheedulla97@gmail.com");
        DocumentReference dr=db.collection("Register").document(email.getText().toString());
        dr.collection("contacts").document("list1").set(addFirstUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main2Activity.this, "User added", Toast.LENGTH_SHORT).show();
                    }
                })
               .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
//