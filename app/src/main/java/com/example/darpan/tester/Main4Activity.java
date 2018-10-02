package com.example.darpan.tester;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Main4Activity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID = user.getEmail();
    private static final String TAG = "Main4Activity";
    ListView chat;
    TextView chat2;
    EditText msg;
    ImageButton send;
    boolean flag=false;
    DocumentReference docref = db.collection("Register").document(userID);
    Long chatindex;

    //final ArrayList<String> chatlist=new ArrayList<>();
    //String[] chatlistt = {"asdsdasadsdsadsd", "dasdsdadasddf", "sedfghfg"};
    //Timestamp timestamp;
    //int date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        msg = findViewById(R.id.msg_name);
        send = findViewById(R.id.img_btn);
        Intent i = getIntent();
        final String receiverID = i.getStringExtra("userID");
        docref.collection(receiverID).document("today").
                addSnapshotListener(Main4Activity.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(Main4Activity.this, "Snapshot Listener running" + chatindex, Toast.LENGTH_SHORT).show();
                            //StringBuilder texts = new StringBuilder();
                           if (flag){
                               Long tempchatindex;
                               tempchatindex=documentSnapshot.getLong("Noof_msg");
                               String message=documentSnapshot.getString("message"+tempchatindex);
                               String type=documentSnapshot.getString("type"+tempchatindex);
                               final LinearLayout textarea;
                               textarea=findViewById(R.id.textarea);
                               final TextView tv=new TextView(Main4Activity.this);
                               tv.setText(type+": "+message);
                               tv.setPadding(6,10,6,10);
                               tv.setTextSize(22);
                               ShapeDrawable sd = new ShapeDrawable(new OvalShape());
                               sd.getPaint().setColor(Color.WHITE);

                               tv.setBackground(sd);
                               textarea.addView(tv);
                           }

                            //chat2.setText(texts);
                            //final ArrayAdapter<String> adapter=new ArrayAdapter<>(Main4Activity.this,android.R.layout.simple_list_item_2,chatlistt);

                            //chat.setAdapter(adapter);

                        } else {
                            Toast.makeText(Main4Activity.this, "Snapshop listener error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        //chat2=findViewById(R.id.chat2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        final String receiverID = i.getStringExtra("userID");
        final DocumentReference docref1 = db.collection("Register").document(receiverID);

        docref.collection(receiverID).document("today").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            chatindex = documentSnapshot.getLong("Noof_msg");
                            final LinearLayout textarea;
                            textarea=findViewById(R.id.textarea);
                            for (int i=1;i<=chatindex;i++){
                                String message=documentSnapshot.getString("message"+i);
                                String type=documentSnapshot.getString("type"+i);
                                TextView tv=new TextView(Main4Activity.this);
                                tv.setPadding(6,10,6,10);
                                tv.setText(type+": "+message);
                                tv.setTextSize(22);
                                textarea.addView(tv);
                                //Toast.makeText(Main4Activity.this, type+":"+message, Toast.LENGTH_SHORT).show();
                                //chatlist.add(type+":"+message);
                                // texts.append(type+":"+message+"\n\n");
                            }


                            Toast.makeText(Main4Activity.this, "Loading Existing chats you" + chatindex, Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> firstdoc = new HashMap<>();
                            firstdoc.put("Noof_msg", 0);
                            docref.collection(receiverID).document("today").set(firstdoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Main4Activity.this, "Chat created you", Toast.LENGTH_SHORT).show();
                                            Map<String, Object> firstdoc = new HashMap<>();
                                            firstdoc.put("Noof_msg", 0);
                                            docref1.collection(userID).document("today").set(firstdoc)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            chatindex = 0L;
                                                            Toast.makeText(Main4Activity.this, "Chat created him" + chatindex, Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Main4Activity.this, "restart activity him", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Main4Activity.this, "restart activity you", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });


    }

    public void sendMessage(View view) {

        Intent i = getIntent();
        final String receiverID = i.getStringExtra("userID");
        final DocumentReference docref1 = db.collection("Register").document(receiverID);
        String msgvar = msg.getText().toString().trim();
        flag=true;
        chatindex++;
        Map<String, Object> postMe = new HashMap<>();
        postMe.put("Noof_msg", chatindex);
        postMe.put("message" + chatindex, msgvar);
        postMe.put("type" + chatindex, "to");

        docref.collection(receiverID).document("today").set(postMe, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main4Activity.this, "msg sent to my database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main4Activity.this, "msg not sent to my database", Toast.LENGTH_SHORT).show();
                    }
                });

        Map<String, Object> postHim = new HashMap<>();
        postHim.put("Noof_msg", chatindex);
        postHim.put("message" + chatindex, msgvar);
        postHim.put("type" + chatindex, "from");
        docref1.collection(userID).document("today").set(postHim, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main4Activity.this, "sent to him/her database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main4Activity.this, "msg not sent to his/her database", Toast.LENGTH_SHORT).show();
                    }
                });




    }



}
