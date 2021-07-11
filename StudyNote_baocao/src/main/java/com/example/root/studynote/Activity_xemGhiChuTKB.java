package com.example.root.studynote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_xemGhiChuTKB extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> arrNoiDungGhiChu = new ArrayList<>();
    ArrayList<String> arrIdGhiChu = new ArrayList<>();
    int sizeArrIdGhiChu;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_ghi_chu_tkb);
        final ListView listView = findViewById(R.id.lv);
        Intent intent = getIntent();
        int thu = intent.getIntExtra("thu", 0);
        final int tiet = intent.getIntExtra("tiet", 0);
        String iduser = intent.getStringExtra("iduser");
        database.child("GhiChu").child(iduser).orderByChild("thu").equalTo(thu)
                .addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                   int tietDB = Integer.parseInt(childSnapshot.child("tiet").getValue().toString());
                                                   if (tiet == tietDB) {
                                                       arrIdGhiChu.add(childSnapshot.getKey());
                                                       arrNoiDungGhiChu.add(childSnapshot.child("noiDung").getValue().toString());
                                                   }
                                                   else {

                                                   }
                                               }
                                               sizeArrIdGhiChu = arrIdGhiChu.size();

                                               if (sizeArrIdGhiChu != 0) {
                                                   arrayAdapter = new ArrayAdapter(
                                                           Activity_xemGhiChuTKB.this,
                                                           android.R.layout.simple_list_item_1,
                                                           arrNoiDungGhiChu);
                                                   listView.setAdapter(arrayAdapter);
                                               }
                                               else {
                                                   arrNoiDungGhiChu.add("Không có ghi chú");
                                                   arrayAdapter = new ArrayAdapter(
                                                           Activity_xemGhiChuTKB.this,
                                                           android.R.layout.simple_list_item_1,
                                                           arrNoiDungGhiChu);
                                                   listView.setAdapter(arrayAdapter);
                                               }

                                           }

                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {

                                           }
                                       });

    }
}
