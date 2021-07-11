package com.example.root.studynote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_guiThongBao extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    List<String> listDSlop = new ArrayList<>();
    List<String> listDSHS = new ArrayList<>();
    List<String> listIdDSHS = new ArrayList<>();
    List<String> listDSHS_guiThongBao = new ArrayList<>();
    List<String> listIdDSHS_guiThongBao = new ArrayList<>();
    String idCLass;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_thong_bao);
        final EditText edtNoiDung = findViewById(R.id.edtNoiDungThongBao);

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NoiDung = edtNoiDung.getText().toString();
                for (int i = 0; i < listIdDSHS_guiThongBao.size(); i++) {
                    database.child("ThongBao").child(listIdDSHS_guiThongBao.get(i).toString()).setValue(NoiDung);
                }
                Toast.makeText(Activity_guiThongBao.this, "Gửi thành công!", Toast.LENGTH_SHORT).show();
            }
        });

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
                SingleSpinnerSearch searchSingleSpinner = findViewById(R.id.searchSingleSpinner);
                searchSingleSpinner.setItems(listArrayDSlop, -1, new SpinnerListener() {

                    @Override
                    public void onItemsSelected(List<KeyPairBoolData> items) {
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).isSelected()) {
                                idCLass = items.get(i).getName();
                                listDSHS = getNameArrayFromDB(idCLass);
                                listIdDSHS = getIdArrayFromDB(idCLass);

                                addDSHSToSpinner(listDSHS, idCLass, listIdDSHS);

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

    public void addDSHSToSpinner (final List<String> listDSHS, final String idCLass, final List<String> listIdDSHS) {
        /**
         * Add danh sách học sinh vào spinner
         */

        database.child("DSLop").child(idCLass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<KeyPairBoolData> listArrayDSHS = new ArrayList<>();
                for (int i = 0; i < listDSHS.size(); i++) {
                    KeyPairBoolData h = new KeyPairBoolData();
                    h.setId(i + 1);
                    h.setName(listDSHS.get(i));
                    h.setSelected(false);
                    h.setIdUser(listIdDSHS.get(i));
                    listArrayDSHS.add(h);
                }
                MultiSpinnerSearch searchMultiSpinnerUnlimited = findViewById(R.id.searchMultiSpinnerUnlimited);
                searchMultiSpinnerUnlimited.setItems(listArrayDSHS, -1, new SpinnerListener() {

                    @Override
                    public void onItemsSelected(List<KeyPairBoolData> items) {
                        listDSHS_guiThongBao = new ArrayList<>();
                        listIdDSHS_guiThongBao = new ArrayList<>();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).isSelected()) {
                                listDSHS_guiThongBao.add(items.get(i).getName());
                                listIdDSHS_guiThongBao.add(items.get(i).getIdUser());
                                Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
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

    public ArrayList<String> getNameArrayFromDB (String idCLass) {
        final ArrayList<String> arrName = new ArrayList<>();
        database.child("DSLop").child(idCLass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    arrName.add(childSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return arrName;
    }

    public ArrayList<String> getIdArrayFromDB (String idCLass) {
        final ArrayList<String> arrName = new ArrayList<>();
        database.child("DSLop").child(idCLass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    arrName.add(childSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return arrName;
    }







    public void sendThongBaoToDSHS (List listIdDSHS_guiThongBao, String NoiDung) {
        database = FirebaseDatabase.getInstance().getReference();
        for (int i = 0; i < listIdDSHS_guiThongBao.size(); i++) {
            database.child("ThongBao").child(listIdDSHS_guiThongBao.get(i).toString()).setValue(NoiDung);
        }
    }
}
