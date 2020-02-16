package com.example.Fooddeliverysystem.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the confirmationtoken database table.
 */
@ApiModel
@Entity
@Table(name = "ConfirmationToken")
@NamedQuery(name = "Confirmationtoken.findAll", query = "SELECT c FROM Confirmationtoken c")
public class Confirmationtoken implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;

    private String confirmationToken;

    @Temporal(TemporalType.DATE)
    private Date confirmedDate;

    @Temporal(TemporalType.DATE)
    private Date createdDate;

    //bi-directional many-to-one association to User
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Confirmationtoken() {
    }

    public int getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return this.confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getConfirmedDate() {
        return this.confirmedDate;
    }

    public void setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}