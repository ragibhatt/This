package com.example.darpan.tester;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main5Activity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser() ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail=user.getEmail();
    ListView contactList;
    //String[] contacts;
    //String [] contactsss={"asdsdsad","asdasd","zxcxcczx","qwee"};
    //attach contect here
    ArrayList<String> list=new ArrayList<String>() ;
    DocumentReference docref=db.collection("Register").document(userEmail);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        docref.collection("contacts").document("list1").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Long j=documentSnapshot.getLong("numOfUsers");
                            //contacts=new String[j.intValue()];
                            //Toast.makeText(Main5Activity.this, j, Toast.LENGTH_SHORT).show();
                            //int jk=Integer.parseInt(j);
                            for (int i=1;i<=j;i++){
                                //list.add(documentSnapshot.getString("user"+i));
                               // contacts[i-1]=documentSnapshot.getString("user"+i);
                                list.add(documentSnapshot.getString("user"+i));
                               Toast.makeText(Main5Activity.this,list.get(i-1), Toast.LENGTH_SHORT).show();

                            }
                            final ArrayAdapter<String> adapter = new ArrayAdapter<>(Main5Activity.this, android.R.layout.simple_list_item_1,list);
                            contactList = findViewById(R.id.contacts_list);
                            contactList.setAdapter(adapter);
                            contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String value = adapter.getItem(position);
                                    Intent i= new Intent(Main5Activity.this,Main4Activity.class);
                                    i.putExtra("userID",value);
                                    startActivity(i);
                                }
                            });
                        }
                    }
                });


    }


}
