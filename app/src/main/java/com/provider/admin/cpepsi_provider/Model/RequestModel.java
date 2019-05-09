package com.provider.admin.cpepsi_provider.Model;

import java.io.Serializable;

public class RequestModel implements Serializable {

    private String prId;
    private String customerId;
    private String providerId;
    private String discription;
    private String date;
    private String prostatus;
    private String id;
    private String name;
    private String email;
    private String contact;
    private String password;
    private String address;
    private String status;
    private String paymentStatus;
    private String paymentAmount;
    private String image;

    public RequestModel(String prId, String customerId, String providerId, String discription, String date, String prostatus, String id, String name, String email, String contact, String password, String address, String status, String paymentStatus, String paymentAmount, String image) {
        this.prId = prId;
        this.customerId = customerId;
        this.providerId = providerId;
        this.discription = discription;
        this.date = date;
        this.prostatus = prostatus;
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
        this.address = address;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
        this.image = image;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProstatus() {
        return prostatus;
    }

    public void setProstatus(String prostatus) {
        this.prostatus = prostatus;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
