package com.example.SpringbootJwtWings1T4.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="leave_request")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String category;
    @Column(name= "no_of_days")
    private int noOfDays;
    @Column
    private Date applied_on;
    @Column
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Leave() {
    }

    public Leave(int id, String category, Date applied_on, int noOfDays, String description, Status status, User user) {
        this.id = id;
        this.category = category;
        this.applied_on = applied_on;
        this.noOfDays = noOfDays;
        this.description = description;
        this.status = status;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", noOfDays=" + noOfDays +
                ", applied_on=" + applied_on +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", user=" + user +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public Date getApplied_on() {
        return applied_on;
    }

    public void setApplied_on(Date applied_on) {
        this.applied_on = applied_on;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
