package com.provider.admin.cpepsi_provider.Model;

public class SubServiceModel {
    private String sub_ser_id;
    private String sub_service_name;

    public SubServiceModel(String sub_ser_id, String sub_service_name) {
        this.sub_ser_id=sub_ser_id;
        this.sub_service_name=sub_service_name;
    }

    public String getSub_ser_id() {
        return sub_ser_id;
    }

    public String getSub_service_name() {
        return sub_service_name;
    }

    public void setSub_service_name(String sub_service_name) {
        this.sub_service_name = sub_service_name;
    }

    public void setSub_ser_id(String sub_ser_id) {
        this.sub_ser_id = sub_ser_id;
    }
}
