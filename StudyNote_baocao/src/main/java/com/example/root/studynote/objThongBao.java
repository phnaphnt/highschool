package com.example.root.studynote;

import java.util.ArrayList;

/**
 * Created by root on 1/4/18.
 */

public class objThongBao {
    private String noiDungThongBao;
    private String Lop;
    private ArrayList<String> DSHocSinh;

    public String getNoiDungThongBao() {
        return noiDungThongBao;
    }

    public void setNoiDungThongBao(String noiDungThongBao) {
        this.noiDungThongBao = noiDungThongBao;
    }

    public String getLop() {
        return Lop;
    }

    public void setLop(String lop) {
        Lop = lop;
    }

    public ArrayList<String> getDSHocSinh() {
        return DSHocSinh;
    }

    public void setDSHocSinh(ArrayList<String> DSHocSinh) {
        this.DSHocSinh = DSHocSinh;
    }
}
