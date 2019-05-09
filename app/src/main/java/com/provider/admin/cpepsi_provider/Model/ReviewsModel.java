package com.provider.admin.cpepsi_provider.Model;

public class ReviewsModel {

    private String id;
    private String customerId;
    private String providerId;
    private String name;
    private String email;
    private String contactno;
    private String address;
    private String feedbackservice;

    public ReviewsModel(String id, String customerId, String providerId, String name, String email, String contactno, String address, String feedbackservice) {
        this.id = id;
        this.customerId = customerId;
        this.providerId = providerId;
        this.name = name;
        this.email = email;
        this.contactno = contactno;
        this.address = address;
        this.feedbackservice = feedbackservice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFeedbackservice() {
        return feedbackservice;
    }

    public void setFeedbackservice(String feedbackservice) {
        this.feedbackservice = feedbackservice;
    }

}
