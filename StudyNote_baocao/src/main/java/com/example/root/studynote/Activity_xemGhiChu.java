package com.example.root.studynote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_xemGhiChu extends AppCompatActivity {
    /**
     * Activity xem tất cả ghi chú có trong cơ sở dữ liệu
     * Màn hình xem ghi chú dạng ListView
     */
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ListView lvGhiChu;
    EditText edtNoiDungSua;
    Button btnEdit;
    Button btnDel;
    ArrayList<String> arrIdGhiChu = new ArrayList<>();
    ArrayList<String> arrNoiDungGhiChu = new ArrayList<>();
    int sizeArrIdGhiChu;
    int position = -1;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_ghi_chu);

        lvGhiChu = findViewById(R.id.lvGhiChu);
        btnEdit = findViewById(R.id.btnEdit);
        btnDel = findViewById(R.id.btnDel);
        edtNoiDungSua = findViewById(R.id.edtNoiDungSua);

        /**
         * Nhận dữ liệu từ MainActivity
         */
        Intent intent = getIntent();
        final String iduser = intent.getStringExtra("iduser");
        final String ngayGhiChu = intent.getStringExtra("NgayXemGhiChu");



        /**
         * Kiểm tra database ghi chú của user theo ngày ghi chú
         * Kết quả trả về mảng arrIdGhiChu - Nếu không tồn tại ghi chú thì trả về mảng có 0 phần tử
         */
        database = database.child("GhiChu").child(iduser);
        database.orderByChild("ngay").equalTo(ngayGhiChu)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    arrIdGhiChu.add(childSnapshot.getKey());
                    arrNoiDungGhiChu.add(childSnapshot.child("noiDung").getValue().toString());
                }
                sizeArrIdGhiChu = arrIdGhiChu.size();

                if (sizeArrIdGhiChu != 0) {
                    arrayAdapter = new ArrayAdapter(
                            Activity_xemGhiChu.this,
                            android.R.layout.simple_list_item_1,
                            arrNoiDungGhiChu);
                    lvGhiChu.setAdapter(arrayAdapter);
                }
                else {
                    Toast.makeText(
                            Activity_xemGhiChu.this,
                            "Không có ghi chú!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lvGhiChu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtNoiDungSua.setText(arrNoiDungGhiChu.get(i));
                position = i;
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance().getReference();
                arrNoiDungGhiChu.set(position, edtNoiDungSua.getText().toString());
                String idGhiChu = arrIdGhiChu.get(position);
                arrayAdapter.notifyDataSetChanged();
                database.child("GhiChu").child(iduser).child(idGhiChu).child("noiDung").setValue(arrNoiDungGhiChu.get(position));
                arrIdGhiChu = new ArrayList<>();
                arrNoiDungGhiChu = new ArrayList<>();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance().getReference();
                arrNoiDungGhiChu.remove(position);
                String idGhiChu = arrIdGhiChu.get(position);
                arrayAdapter.notifyDataSetChanged();
                database.child("GhiChu").child(iduser).child(idGhiChu).removeValue();
                arrIdGhiChu = new ArrayList<>();
                arrNoiDungGhiChu = new ArrayList<>();
            }
        });
    }
}
