package com.provider.admin.cpepsi_provider.Model;

public class ServiceModel {
    private String ser_id;
    private String service_name;

    public ServiceModel(String ser_id, String service_name) {
        this.ser_id=ser_id;
        this.service_name=service_name;

    }

    public String getSer_id() {
        return ser_id;
    }

    public void setSer_id(String ser_id) {
        this.ser_id = ser_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }
}
