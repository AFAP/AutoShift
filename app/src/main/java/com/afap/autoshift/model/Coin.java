package com.afap.autoshift.model;


public class Coin {
    private String id;
    private String name;
    private int resId;

    public Coin(String id, String name, int resId) {
        this.id = id;
        this.name = name;
        this.resId = resId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resId=" + resId +
                '}';
    }
}
