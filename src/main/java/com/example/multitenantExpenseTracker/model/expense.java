package com.example.multitenantExpenseTracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class expense {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private long amt;
    private String category;
    private LocalDate date;

    public expense() {
    }

    public expense(@JsonProperty("id") long id,@JsonProperty("title") String title,@JsonProperty("amt") long amt,@JsonProperty("category") String category, @JsonProperty("date") LocalDate date) {
        this.id = id;
        this.title = title;
        this.amt = amt;
        this.category = category;
        this.date = date;
        //this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAmt() {
        return amt;
    }

    public void setAmt(long amt) {
        this.amt = amt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    private user user;

}
