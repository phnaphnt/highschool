package com.example.root.studynote;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Activity_ghichu extends AppCompatActivity {
    /**
     * Activity nhập ghi chú theo thời khóa biểu
     * Màn hình nhập ghi chú
     */
    DatabaseReference database;
    int thu;
    Calendar calendar_Boss = Calendar.getInstance();
    PendingIntent pendingIntent;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghichu);

        final objGhiChuTKB GhiChu = new objGhiChuTKB();
        final EditText edtNoiDung = findViewById(R.id.edtNoiDung);
        final EditText edtNgay = findViewById(R.id.edtNgay);
        final EditText edtTiet = findViewById(R.id.edtTiet);
        final EditText edtTgian = findViewById(R.id.edtTgian);
        final EditText edtMon = findViewById(R.id.edtMon);
        final EditText edtNgayNhacNho = findViewById(R.id.edtNgayNhacNho);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        final RadioGroup radioGroupKT = findViewById(R.id.radioGroupKT);
        final RadioButton radioButton15 = findViewById(R.id.radioButton15);
        final RadioButton radioButton1T = findViewById(R.id.radioButton1T);

        edtNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay(edtNgay);
            }
        });
        edtNgayNhacNho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgayNhacNho(edtNgayNhacNho);
            }
        });
        edtTgian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGioNhacNho(edtTgian);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (radioGroupKT.getCheckedRadioButtonId() != -1) {
                    if (radioButton15.isChecked()) {
                        GhiChu.setKt15(true);
                    }
                    else {
                        GhiChu.setKt15(false);
                    }
                    if (radioButton1T.isChecked()) {
                        GhiChu.setKt1t(true);
                    }
                    else {
                        GhiChu.setKt1t(false);
                    }
                }
                else {

                }
                String tmp = edtTgian.getText().toString();
                if (Objects.equals(tmp, "")) {
                    /**
                     * Nếu không cài nhắc nhở
                     */
                    GhiChu.setNoiDung(edtNoiDung.getText().toString());
                    GhiChu.setNgay(edtNgay.getText().toString());
                    GhiChu.setTiet(Integer.parseInt(edtTiet.getText().toString()));
                    GhiChu.setMon(edtMon.getText().toString());
                    GhiChu.setThu(thu);

                    addGhiChu(GhiChu);
                    finish();
                }
                else {
                    /**
                     * Nếu cài nhắc nhở - Kèm theo thời gian và ngày nhắc nhở
                     */
                    GhiChu.setNoiDung(edtNoiDung.getText().toString());
                    GhiChu.setNgay(edtNgay.getText().toString());
                    GhiChu.setTiet(Integer.parseInt(edtTiet.getText().toString()));
                    GhiChu.setMon(edtMon.getText().toString());
                    GhiChu.setThu(thu);
                    GhiChu.setTgian(tmp);
                    GhiChu.setNgayNhacNho(edtNgayNhacNho.getText().toString());

                    addGhiChu(GhiChu);

                    /**
                     * Cài đặt nhắc nhở khi nhập ghi chú
                     */
                    startAlarm(calendar_Boss, edtNoiDung.getText().toString());
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

    private void addGhiChu (objGhiChuTKB objGhiChuTKB) {
        Intent intent = getIntent();
        String iduser = intent.getStringExtra("iduser");
        database = FirebaseDatabase.getInstance().getReference();
        database.child("GhiChu").child(iduser).push().setValue(objGhiChuTKB);
        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
    }

    private void startAlarm (Calendar calendar, String noiDung) {
        i = i+1;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =
                new Intent(Activity_ghichu.this, AlarmNotificationReceiver.class);
        intent.putExtra("noiDung", noiDung);
        pendingIntent = PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
