package com.example.root.studynote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_signup extends AppCompatActivity {
    private static final String TAG = "Activity_signup";
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();;
    objUser user = new objUser();
    List<String> listDSlop = new ArrayList<>();
    boolean checkUserExist;
    String idCLass = "";
    String iduser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dang_ki(user);
    }

    public void dang_ki(final objUser user) {
        user.setIdClass("");
        final EditText edtName = findViewById(R.id.edtName);
        final EditText edtPhone = findViewById(R.id.edtPhone);
        final EditText edtPw = findViewById(R.id.edtPw);
        final EditText edtConfirmPw = findViewById(R.id.edtConfirmPw);
        final Button btnSignup = findViewById(R.id.btnSignup);
        final RadioGroup radioGroupLoai = findViewById(R.id.radioGroupLoai);
        final RadioButton radioBtnGV = findViewById(R.id.radioBtnGV);
        getDSLopDBtoSpinner(user);



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idClass = idCLass;
                String name = edtName.getText().toString();
                String phone_number = edtPhone.getText().toString();
                String pw = edtPw.getText().toString();
                String confirm_pw = edtConfirmPw.getText().toString();

                if (checkIfEdtOK(name, phone_number, pw, confirm_pw)) {
                    if (checkConfirmPw(pw, confirm_pw)) {
                        if (radioGroupLoai.getCheckedRadioButtonId() != -1) {
                            if (radioBtnGV.isChecked()) {
                                iduser = null;
                                int loai = 1;
                                user.setLoai(loai);
                                user.setIdClass("");
                                user.setName(name);
                                user.setPhoneNumber(phone_number);
                                user.setPw(pw);

                                checkIfUserIsExist(user);



                            }
                            else {
                                iduser = null;
                                if (idClass == "") {
                                    Toast.makeText(Activity_signup.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    int loai = 0;
                                    user.setIdClass(idClass);
                                    user.setLoai(loai);
                                    user.setName(name);
                                    user.setPhoneNumber(phone_number);
                                    user.setPw(pw);
                                    checkIfUserIsExist(user);

                                }
                            }
                        }
                        else {
                            Toast.makeText(Activity_signup.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        edtPw.setText(null);
                        edtConfirmPw.setText(null);
                        edtPw.setHintTextColor(Color.parseColor("#FF0000"));
                        edtConfirmPw.setHintTextColor(Color.parseColor("#FF0000"));
                        Toast.makeText(Activity_signup.this,"Xác nhận mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Activity_signup.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void pushDATAtoDB(objUser user) {
        String phone_number= user.getPhoneNumber();
        String pw = user.getPw();
        int loai = user.getLoai();
        database.child("User").push().setValue(user);
        getIdFromPhoneAndPwAndPushTKB(phone_number, pw, loai);
        Toast.makeText(Activity_signup.this,"Đăng kí thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Activity_signup.this, Activity_signin.class);
        startActivity(intent);

    }

    public boolean checkIfEdtOK (String name, String phone_number, String pw, String confirm_pw) {
        boolean result;
        if (!name.equals("") && !phone_number.equals("") && !pw.equals("") && !confirm_pw.equals("")) {
            result = true;
        }
        else{
            Toast.makeText(Activity_signup.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    public void getDSLopDBtoSpinner (final objUser user) {
        database.child("DSLop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    listDSlop.add(childSnapshot.getKey());
                }

                /**
                 * Add danh sách lớp vào spinner
                 */
                final List<KeyPairBoolData> listArrayDSlop = new ArrayList<>();
                for (int i = 0; i < listDSlop.size(); i++) {
                    KeyPairBoolData h = new KeyPairBoolData();
                    h.setId(i + 1);
                    h.setName(listDSlop.get(i));
                    h.setSelected(false);
                    h.setIdUser(listDSlop.get(i));
                    listArrayDSlop.add(h);
                }
                SingleSpinnerSearch searchSingleSpinner = findViewById(R.id.SingleSpinnerDSLop);
                searchSingleSpinner.setItems(listArrayDSlop, -1, new SpinnerListener() {

                    @Override
                    public void onItemsSelected(List<KeyPairBoolData> items) {
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).isSelected()) {
                                idCLass = items.get(i).getName();
                                user.setIdClass(idCLass);
                                Log.i(TAG, i + " : " + idCLass + " : " + items.get(i).isSelected());
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean checkConfirmPw (String pw, String confirm_pw) {
        boolean result;
        if (pw.equals(confirm_pw)) {
            result = true;
        }
        else {
            result = false;
        }
        return result;
    }

    public Map<String, Map> create_null_tkb_for_user () {
        Map<String, Map> tkb = new HashMap<>();
        Map<String, String> thu = new HashMap<>();
        for (int k = 2; k <= 8; k++) {
            for (int j = 1; j <= 10; j++) {
                thu.put(j + "", "");
            }
            tkb.put("T" + k, thu);
        }
        return tkb;
    }

    public void getIdFromPhoneAndPwAndPushTKB (final String phone, final String pw, final int loai) {
        database.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String phoneDB = childSnapshot.child("phoneNumber").getValue().toString();
                    String pwDB = childSnapshot.child("pw").getValue().toString();
                    if (loai == 1) {
                        if (phone.equals(phoneDB) && pw.equals(pwDB)) {
                            String iduser = childSnapshot.getKey();
                            database.child("TKB").child(iduser).setValue(create_null_tkb_for_user());
                        }
                    }
                    else {
                        if (phone.equals(phoneDB) && pw.equals(pwDB)) {
                            String iduser = childSnapshot.getKey();
                            String idClassDB = childSnapshot.child("idClass").getValue().toString();
                            String nameDB = childSnapshot.child("name").getValue().toString();
                            database.child("ThongBao").child(iduser).setValue("");
                            database.child("DSLop").child(idClassDB).child(iduser).setValue(nameDB);
                            database.child("TKB").child(iduser).setValue(create_null_tkb_for_user());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkIfUserIsExist (final objUser user) {
        final String phone = user.getPhoneNumber();
        final ArrayList<String> arrPhone = new ArrayList<>();
        final String pw = user.getPw();
        database.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String phoneDB = childSnapshot.child("phoneNumber").getValue().toString();
                    arrPhone.add(phoneDB);
                }
                if (checkSDTinArr(phone, arrPhone)) {
                    Toast.makeText(Activity_signup.this, "...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activity_signup.this, Activity_signin.class);
                    startActivity(intent);
                }
                else {
                    pushDATAtoDB(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean checkSDTinArr (String phone, ArrayList<String> arrPhone) {
        boolean result = false;
        for (int i = 0; i < arrPhone.size(); i++) {
            if (phone == arrPhone.get(i)) {
                result = true;
                break;
            }
        }
        return result;
    }
}


