package com.example.model;

public class Module implements BaseModel {
    private int id;
    private String name;
    private Teacher teacher;

    public Module() {}

    public Module(int id, String name, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
    }

    @Override
    public int getId() { return id; }
    @Override
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
}