package com.example.root.studynote;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Activity_chinhsuaTKB extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinhsua_tkb);
        Intent intent = getIntent();
        final String iduser = intent.getStringExtra("iduser");

        final TableLayout t2 = findViewById(R.id.t2);
        final TableLayout t3 = findViewById(R.id.t3);
        final TableLayout t4 = findViewById(R.id.t4);
        final TableLayout t5 = findViewById(R.id.t5);
        final TableLayout t6 = findViewById(R.id.t6);
        final TableLayout t7 = findViewById(R.id.t7);
        final TableLayout t8 = findViewById(R.id.t8);

        final Button btnYes = findViewById(R.id.btnYes);
        final Button btnNo = findViewById(R.id.btnNo);

        /**
         * Khởi tạo mảng 2 chiều tkb[][] chứa thời khóa biểu
         */
        final String tkb[][] = new String[11][9];


        btnYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                putDataToArray(t2, 2, tkb);
                putDataToArray(t3, 3, tkb);
                putDataToArray(t4, 4, tkb);
                putDataToArray(t5, 5, tkb);
                putDataToArray(t6, 6, tkb);
                putDataToArray(t7, 7, tkb);
                putDataToArray(t8, 8, tkb);

                database.child("TKB").child(iduser).setValue(mapTKB(tkb));

                database.child("TKB").child(iduser).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Toast.makeText(Activity_chinhsuaTKB.this,
                                "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Toast.makeText(Activity_chinhsuaTKB.this,
                                "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        finish();
                    }
                });
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void putDataToArray (TableLayout layoutThu, int thu, String tkb[][]) {
        /**
         * Hàm chuyển dữ liệu từ người dùng nhập vào edit text sang mảng 2 chiều tkb[][]
         */
        for (int i = 2; i < 7; i++) {
            TableRow rowTiet = (TableRow) layoutThu.getChildAt(i);
            EditText tmp_tiet = (EditText) rowTiet.getChildAt(1);
            tkb[i - 1][thu] = tmp_tiet.getText().toString();
        }
        for (int i = 5; i < 10; i++) {
            TableRow rowTiet = (TableRow) layoutThu.getChildAt(i - 3);
            EditText tmp_tiet = (EditText) rowTiet.getChildAt(2);
            tkb[i + 1][thu] = tmp_tiet.getText().toString();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Map mapTKB (String tkb[][]) {
        /**
         * Hàm chuyển thời khóa biểu từ mảng sang map
         */
        Map<String, Map> tkbResult = new HashMap<>();

        for (int k = 2; k <= 8; k++) {
            Map<String, String> thu = new HashMap<>();
            for (int j = 1; j <= 10; j++) {
                if (Objects.equals(tkb[j][k], "")) {
                    thu.put(j + "", "");
                }
                else {
                    thu.put(j + "", tkb[j][k]);
                }
            }
            tkbResult.put("T" + k, thu);
        }

        return tkbResult;
    }
}
