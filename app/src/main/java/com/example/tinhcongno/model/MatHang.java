package com.example.tinhcongno.model;

public class MatHang{
    private int id;
    private String name;
    private float menhGia;

    public MatHang(){

    }
    public MatHang(String name, float menhGia) {
        this.name = name;
        this.menhGia = menhGia;
    }

    public float getMenhGia() {
        return menhGia;
    }

    public void setMenhGia(float menhGia) {
        this.menhGia = menhGia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
