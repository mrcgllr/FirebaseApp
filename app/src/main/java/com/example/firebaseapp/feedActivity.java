package com.example.firebaseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class feedActivity extends AppCompatActivity {

    ListView listView;
    PostClass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String > userCommentFromFB;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_post)
        {
            Intent intent = new Intent(getApplicationContext(),uploadActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView=findViewById(R.id.listview);

        userCommentFromFB = new ArrayList<String>();
        userEmailFromFB = new ArrayList<String>();
        userImageFromFB = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new PostClass(userEmailFromFB,userCommentFromFB,userImageFromFB,this);
        listView.setAdapter(adapter);
        getDataFromFirebase();
    }

    public void getDataFromFirebase(){
        DatabaseReference newReferece = firebaseDatabase.getReference("Posts");
        newReferece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             /*   System.out.println("FBV  children "+dataSnapshot.getChildren());
                System.out.println("FBV  KEY : "+dataSnapshot.getKey());
                System.out.println("FBV  value : "+dataSnapshot.getValue());
                System.out.println("FBV  priority : "+dataSnapshot.getPriority());*/
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    //ds.getValue();
                   // System.out.println("fbv ds value"+ds.getValue());
                    HashMap<String,String> hashMap = (HashMap<String, String>) ds.getValue();

                    userEmailFromFB.add(hashMap.get("useremail"));
                    userCommentFromFB.add(hashMap.get("comment"));
                    userImageFromFB.add(hashMap.get("downloadURL"));
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
