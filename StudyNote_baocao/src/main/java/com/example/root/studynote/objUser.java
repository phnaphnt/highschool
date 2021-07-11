package com.example.root.studynote;

/**
 * Created by root on 12/1/17.
 */

public class objUser {
    /**
     * 1 user co 5 thuoc tinh
     */
    private String Name = null;
    private String phoneNumber = null;
    private String idClass = null;
    private String pw = null;
    private int Loai;

    /**
     * Loai 1 - GV
     * Loai 0 - HS
     */

    public String getName() {
        if ((Name != "") && (!Name.isEmpty())) {
            return Name;
        }
        else
            return null;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        if ((phoneNumber != "") && (!phoneNumber.isEmpty())) {
            return phoneNumber;
        }
        else
            return null;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdClass() {
        if ((idClass != "") && (!idClass.isEmpty())) {
            return idClass;
        }
        else
            return null;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getPw() {
        if ((pw != "") && (!pw.isEmpty())) {
            return pw;
        }
        else
            return null;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public int getLoai() {
        return Loai;
    }

    public void setLoai(int loai) {
        this.Loai = loai;
    }
}
