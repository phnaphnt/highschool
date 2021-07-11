package com.example.root.studynote;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_ghichuThoiGian extends AppCompatActivity {
    DatabaseReference database;
    int thu;
    Calendar calendar_Boss = Calendar.getInstance();
    PendingIntent pendingIntent;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghichu_thoi_gian);

        final EditText edtNoiDung = findViewById(R.id.edtNoiDung);
        final EditText edtNgayThucHien = findViewById(R.id.edtNgayThucHien);
        final EditText edtGioThucHien = findViewById(R.id.edtGioThucHien);
        final EditText edtNgayNhacNho = findViewById(R.id.edtNgayNhacNho);
        final EditText edtGioNhacNho = findViewById(R.id.edtGioNhacNho);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        edtGioNhacNho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGioNhacNho(edtGioNhacNho);
            }
        });

        edtGioThucHien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGio(edtGioThucHien);
            }
        });

        edtNgayNhacNho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgayNhacNho(edtNgayNhacNho);
            }
        });

        edtNgayThucHien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay(edtNgayThucHien);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isNhacNho;
                if (edtGioNhacNho.getText().toString() != "") {
                    isNhacNho = true;
                }
                else {
                    isNhacNho = false;
                }

                if (isNhacNho) {
                    objGhiChuThoiGian objGhiChuThoiGian = new objGhiChuThoiGian();
                    objGhiChuThoiGian.setNoiDung(edtNoiDung.getText().toString());
                    objGhiChuThoiGian.setGio(edtGioThucHien.getText().toString());
                    objGhiChuThoiGian.setNgay(edtNgayThucHien.getText().toString());
                    objGhiChuThoiGian.setGioNhacNho(edtGioNhacNho.getText().toString());
                    objGhiChuThoiGian.setNgayNhacNho(edtNgayNhacNho.getText().toString());
                    addGhiChu(objGhiChuThoiGian);
                    startAlarm(calendar_Boss, edtNoiDung.getText().toString());
                }
                else {
                    objGhiChuThoiGian objGhiChuThoiGian = new objGhiChuThoiGian();
                    objGhiChuThoiGian.setNoiDung(edtNoiDung.getText().toString());
                    objGhiChuThoiGian.setGio(edtGioThucHien.getText().toString());
                    objGhiChuThoiGian.setNgay(edtNgayThucHien.getText().toString());
                    addGhiChu(objGhiChuThoiGian);
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ChonGio (final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(0, 0, 0, i, i1);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, gio, phut, true);
        timePickerDialog.show();
    }

    private void ChonNgay(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                editText.setText(simpleDateFormat.format(calendar.getTime()));
                thu = calendar.get(Calendar.DAY_OF_WEEK);
                thu = (thu == 1)? 8:thu;
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    private void ChonNgayNhacNho (final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        final int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        editText.setText(simpleDateFormat.format(calendar.getTime()));
                        thu = calendar.get(Calendar.DAY_OF_WEEK);
                        thu = (thu == 1)? 8:thu;
                        calendar_Boss.set(i, i1, i2);
                    }
                }, nam, thang, ngay);
        datePickerDialog.show();
    }

    private void ChonGioNhacNho (final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(0, 0, 0, i, i1);
                calendar_Boss.set(calendar_Boss.HOUR_OF_DAY, i);
                calendar_Boss.set(calendar_Boss.MINUTE, i1);
                calendar_Boss.set(calendar_Boss.SECOND, 0);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, gio, phut, true);
        timePickerDialog.show();
    }

    private void addGhiChu (objGhiChuThoiGian objGhiChuThoiGian) {
        Intent intent = getIntent();
        String iduser = intent.getStringExtra("iduser");
        database = FirebaseDatabase.getInstance().getReference();
        database.child("GhiChu").child(iduser).push().setValue(objGhiChuThoiGian);
        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void startAlarm (Calendar calendar, String noiDung) {
        i = i+1;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =
                new Intent(Activity_ghichuThoiGian.this, AlarmNotificationReceiver.class);
        intent.putExtra("noiDung", noiDung);
        pendingIntent = PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
