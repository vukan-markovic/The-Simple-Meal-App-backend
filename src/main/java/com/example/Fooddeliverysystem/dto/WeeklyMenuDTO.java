package com.example.Fooddeliverysystem.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class WeeklyMenuDTO {
    @NotNull
    private Date from;
    @NotNull
    private Date to;
    @NotNull
    private String image;

    public WeeklyMenuDTO() {
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}