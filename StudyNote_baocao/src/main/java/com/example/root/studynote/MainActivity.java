package com.example.root.studynote;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    int thu, tiet;
    int sizeArrIdGhiChu;
    int i = 0;
    Calendar calendar_Boss = Calendar.getInstance();
    PendingIntent pendingIntent;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String iduser = intent.getStringExtra("iduser");
        String typeUser = intent.getStringExtra("typeUser");



        if (Objects.equals(typeUser, "1")) {
            /**
             * GV dang nhap - MainActivity ket noi voi activity_main_gv
             */
            setContentView(R.layout.activity_main_gv);
            ListView lvHomNay = findViewById(R.id.lvGhiChuHomNay);
            ListView lvNgayMai = findViewById(R.id.lvGhiChuNgayMai);
            final TableLayout layoutTKB = findViewById(R.id.TKB);
            getTKB(layoutTKB, iduser);
            for (int i = 2; i <= 8; i++) {
                for (int j = 1; j <= 10; j++) {
                    final TextView a = getTiet(i, j, layoutTKB);
                    final TableRow row = (TableRow) a.getParent();
                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tiet = layoutTKB.indexOfChild(row);
                            tiet = (tiet < 6) ? tiet:(tiet-1);
                            thu = row.indexOfChild(view) + 2;
                            Intent intent_tmp = new Intent(MainActivity.this, Activity_xemGhiChuTKB.class);
                            intent_tmp.putExtra("thu", thu); intent_tmp.putExtra("tiet", tiet);
                            intent_tmp.putExtra("iduser", iduser);
                            startActivity(intent_tmp);
                        }
                    });

                }
            }
            makeToDay(lvHomNay, iduser);
            makeTomorrow(lvNgayMai, iduser);
            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
        else {
            /**
             * HS dang nhap - MainActivity ket noi voi activity_main
             */
            setContentView(R.layout.activity_main);
            ListView lvHomNay = findViewById(R.id.lvGhiChuHomNay);
            ListView lvNgayMai = findViewById(R.id.lvGhiChuNgayMai);
            final TableLayout layoutTKB = findViewById(R.id.TKB);
            getTKB(layoutTKB, iduser);

            for (int i = 2; i <= 8; i++) {
                for (int j = 1; j <= 10; j++) {
                    final TextView a = getTiet(i, j, layoutTKB);
                    final TableRow row = (TableRow) a.getParent();
                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tiet = layoutTKB.indexOfChild(row);
                            tiet = (tiet < 6) ? tiet:(tiet-1);
                            thu = row.indexOfChild(view) + 2;
                            Intent intent_tmp = new Intent(MainActivity.this, Activity_xemGhiChuTKB.class);
                            intent_tmp.putExtra("thu", thu); intent_tmp.putExtra("tiet", tiet);
                            intent_tmp.putExtra("iduser", iduser);
                            startActivity(intent_tmp);
                        }
                    });

                }
            }
            nhanThongBao_hs(iduser);
            makeToDay(lvHomNay, iduser);
            makeTomorrow(lvNgayMai, iduser);
            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    public TextView getTiet (int thu, int tiet, TableLayout layoutTKB) {
        tiet = (tiet < 6) ? tiet:(++tiet);
        TableRow row = (TableRow) layoutTKB.getChildAt(tiet);
        return (TextView) row.getChildAt(thu-2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getTKB (TableLayout layoutTKB, String iduser) {
        for (int i = 2; i <= 8; i++) {
            for (int j = 1; j <= 10; j++) {
                final TextView a = getTiet(i, j, layoutTKB);
                database = FirebaseDatabase.getInstance().getReference();
                database.child("TKB").child(iduser).child("T"+i).child(j+"")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        a.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    public void makeToDay (final ListView listView, String iduser) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String ngay = simpleDateFormat.format(calendar.getTime());

        database = FirebaseDatabase.getInstance().getReference().child("GhiChu").child(iduser);
        database.orderByChild("ngay").equalTo(ngay)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<String> arrIdGhiChu = new ArrayList<>();
                        final ArrayList<String> arrNoiDungGhiChu = new ArrayList<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            arrIdGhiChu.add(childSnapshot.getKey());
                            arrNoiDungGhiChu.add(childSnapshot.child("noiDung").getValue().toString());
                        }

                        sizeArrIdGhiChu = arrIdGhiChu.size();

                        if (sizeArrIdGhiChu != 0) {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(
                                    MainActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    arrNoiDungGhiChu);
                            listView.setAdapter(arrayAdapter);
                        }
                        else {
                            ArrayList<String> tmp = new ArrayList<>();
                            tmp.add(" ");
                            tmp.add("Không có ghi chú");
                            ArrayAdapter arrayAdapter = new ArrayAdapter(
                                    MainActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    tmp);
                            listView.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void makeTomorrow (final ListView listView, String iduser) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String ngay = simpleDateFormat.format(calendar.getTime());

        database = FirebaseDatabase.getInstance().getReference().child("GhiChu").child(iduser);
        database.orderByChild("ngay").equalTo(ngay)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList arrIdGhiChu = new ArrayList<>();
                        final ArrayList arrNoiDungGhiChu = new ArrayList<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            arrIdGhiChu.add(childSnapshot.getKey());
                            arrNoiDungGhiChu.add(childSnapshot.child("noiDung").getValue().toString());
                        }

                        sizeArrIdGhiChu = arrIdGhiChu.size();

                        if (sizeArrIdGhiChu != 0) {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(
                                    MainActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    arrNoiDungGhiChu);
                            listView.setAdapter(arrayAdapter);
                        }
                        else {
                            ArrayList<String> tmp = new ArrayList<>();
                            tmp.add(" ");
                            tmp.add("Không có ghi chú");
                            ArrayAdapter arrayAdapter = new ArrayAdapter(
                                    MainActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    tmp);
                            listView.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void nhanThongBao_hs (final String iduser) {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("ThongBao").child(iduser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                startAlarm(calendar_Boss, dataSnapshot.getValue().toString());
                database.child("ThongBao").child(iduser).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startAlarm (Calendar calendar, String noiDung) {
        i = i+1;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =
                new Intent(MainActivity.this, AlarmNotificationReceiver.class);
        intent.putExtra("noiDung", noiDung);
        pendingIntent = PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ChinhSuaTKB : {
                Intent intent = getIntent();
                String iduser = intent.getStringExtra("iduser");
                Intent tmp_intent = new Intent(MainActivity.this,
                        Activity_chinhsuaTKB.class);
                tmp_intent.putExtra("iduser", iduser);
                startActivity(tmp_intent);
                break;
            }

            case R.id.ThemGhiChuTKB : {
                Intent intent = getIntent();
                String iduser = intent.getStringExtra("iduser");
                intent = new Intent(MainActivity.this,
                        Activity_ghichu.class);
                intent.putExtra("iduser", iduser);
                startActivity(intent);
                break;
            }

            case R.id.ThemGhiChuThoiGian : {
                Intent intent = getIntent();
                String iduser = intent.getStringExtra("iduser");
                intent = new Intent(MainActivity.this,
                        Activity_ghichuThoiGian.class);
                intent.putExtra("iduser", iduser);
                startActivity(intent);
                break;
            }

            case R.id.XemGhiChu : {
                final Calendar calendar = Calendar.getInstance();
                int ngay = calendar.get(Calendar.DATE);
                int thang = calendar.get(Calendar.MONTH);
                int nam = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat simpleDateFormat =
                                new SimpleDateFormat("dd/MM/yyyy");
                        Intent intent = getIntent();
                        String iduser = intent.getStringExtra("iduser");
                        intent = new Intent(
                                MainActivity.this, Activity_xemGhiChu.class);
                        intent.putExtra("iduser", iduser);
                        intent.putExtra(
                                "NgayXemGhiChu", simpleDateFormat.format(calendar.getTime()));
                        startActivity(intent);
                    }
                }, nam, thang, ngay);
                datePickerDialog.show();
                break;
            }

            case R.id.GuiThongBao: {
                /**
                 * Dành cho giáo viên - Gửi thông báo
                 */

                Intent intent = getIntent();
                String iduser = intent.getStringExtra("iduser");
                Intent tmp_intent = new Intent(MainActivity.this,
                        Activity_guiThongBao.class);
                tmp_intent.putExtra("iduser", iduser);
                startActivity(tmp_intent);
                break;
            }
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
