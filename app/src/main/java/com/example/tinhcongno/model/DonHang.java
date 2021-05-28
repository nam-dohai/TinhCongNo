package com.example.tinhcongno.model;

public class DonHang{
    private int id;
    private MatHang matHang;
    private float soLuong;
    private String congTy;
    private String ngayThang;

    public DonHang(){

    }
    public DonHang(MatHang matHang, float soLuong, String congTy, String ngayThang) {
        this.matHang = matHang;
        this.soLuong = soLuong;
        this.congTy = congTy;
        this.ngayThang = ngayThang;
    }

    public float getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(float soLuong) {
        this.soLuong = soLuong;
    }

    public MatHang getMatHang() {
        return matHang;
    }

    public void setMatHang(MatHang matHang) {
        this.matHang = matHang;
    }

    public String getCongTy() {
        return congTy;
    }

    public void setCongTy(String congTy) {
        this.congTy = congTy;
    }

    public String getNgayThang() {
        return ngayThang;
    }

    public void setNgayThang(String ngayThang) {
        this.ngayThang = ngayThang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
