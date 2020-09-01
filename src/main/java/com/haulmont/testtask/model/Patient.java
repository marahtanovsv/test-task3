package com.haulmont.testtask.model;


public class Patient {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private String phone;

    public Patient(Long id, String name, String surname, String lastname, String phone) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
