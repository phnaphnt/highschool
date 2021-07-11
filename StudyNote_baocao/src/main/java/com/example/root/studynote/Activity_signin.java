package com.example.root.studynote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Activity_signin extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    String idUser;
    String typeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        final EditText edtPhone = findViewById(R.id.edtPhone);
        final EditText edtPw = findViewById(R.id.edtPw);
        TextView txtLoadSignin = findViewById(R.id.txtLoadSignin);
        Button btnSignin = findViewById(R.id.btnSignin);

        final Intent intent = new Intent(Activity_signin.this, MainActivity.class);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtPhone.getText().toString();
                String pw = edtPw.getText().toString();
                if (checkInputIsOk(phone, pw)) {
                    database.child("User")
                            .orderByChild("phoneNumber")
                            .equalTo(edtPhone.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                /**
                                 * Khi user nhap phoneNumber va pw
                                 * Dung phoneNumber de xac dinh iduser, dong thoi lay du lieu user
                                 */
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        idUser = childSnapshot.getKey();

                                        /**
                                         * Gui loai user de kiem tra user dang nhap la GV hay HS
                                         * Neu la HS bat man hinh activity_main
                                         * Neu la HV bat man hinh activity_main_GV
                                         **/
                                        database.child("User").child(idUser).child("loai")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        typeUser = dataSnapshot.getValue().toString();
                                                        intent.putExtra("typeUser", typeUser);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                        /**
                                         * So sanh pw dang nhap va pw cua database
                                         * Chuyen sang MainActivity
                                         */
                                        database.child("User").child(idUser).child("pw")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (Objects.equals(dataSnapshot.getValue().toString(),
                                                                edtPw.getText().toString())) {
                                                            Toast.makeText(Activity_signin.this,
                                                                    "Đăng nhập thành công!",
                                                                    Toast.LENGTH_SHORT).show();
                                                            intent.putExtra("iduser", idUser);
                                                            startActivity(intent);
                                                        }
                                                        else {
                                                            Toast.makeText(Activity_signin.this,
                                                                    "Số điện thoại hoặc mật khẩu sai!",
                                                                    Toast.LENGTH_SHORT).show();
                                                            edtPw.setText(null);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
                else {
                    Toast.makeText(Activity_signin.this, "Điền đầy đủ thông tin!",
                            Toast.LENGTH_SHORT).show();
                    edtPw.setText(null);
                    edtPhone.setText(null);
                    edtPw.setHintTextColor(Color.parseColor("#FF0000"));
                    edtPhone.setHintTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        /**
         * Chuyen sang man hinh dang ki neu chua co tai khoan
         */
        txtLoadSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Activity_signin.this, Activity_signup.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkInputIsOk (String phone, String pw) {
        if ((phone != "") && (pw != "")) {
            return true;
        }
        else {
            return false;
        }
    }
}