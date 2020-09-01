package com.haulmont.testtask.model;

import java.util.Date;

public class Recipe {
    private Long id;
    private String description;
    private Long patient;
    private Long doctor;
    private Date creationDate;
    private Date validity;
    private Priority priority;

    public Recipe(long id, String description, long patient, long doctor, Date creationDate, Date validity, Priority priority) {
        this.id = id;
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.creationDate = creationDate;
        this.validity = validity;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public Long getPatient() {
        return patient;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public Long getDoctor() {
        return doctor;
    }

    public void setDoctor(Long doctor) {
        this.doctor = doctor;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
