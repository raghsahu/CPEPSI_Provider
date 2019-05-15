package com.provider.admin.cpepsi_provider.Model;

import java.io.Serializable;

public class SubCategoryModel implements Serializable {

    private String id;
    private String service;
    private String type;
    private String serviceSubCategory;
    private String serviceCharge;
    private String status;
    private boolean isSelected;

    public SubCategoryModel(String id, String service, String type, String serviceSubCategory, String serviceCharge, String status) {
        this.id = id;
        this.service = service;
        this.type = type;
        this.serviceSubCategory = serviceSubCategory;
        this.serviceCharge = serviceCharge;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceSubCategory() {
        return serviceSubCategory;
    }

    public void setServiceSubCategory(String serviceSubCategory) {
        this.serviceSubCategory = serviceSubCategory;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
