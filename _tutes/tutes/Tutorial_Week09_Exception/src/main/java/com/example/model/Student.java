package com.example.model;

public class Student implements BaseModel {
    private int id;
    private String name;

    public Student() {}

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() { return id; }
    @Override
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}