package com.haulmont.testtask.model;

public class Doctor {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private String Specialization;

    public Doctor(long id, String name, String firstName, String lastname, String Specialization) {
        this.id = id;
        this.name = name;
        this.surname = firstName;
        this.lastname = lastname;
        this.Specialization = Specialization;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }

}
