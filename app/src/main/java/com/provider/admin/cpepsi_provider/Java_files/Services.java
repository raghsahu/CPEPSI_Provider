package com.provider.admin.cpepsi_provider.Java_files;

public class Services {
    String Service_doc;
    int service_id;
    String Email;
    int str_service_id;
    String Doc_date;

//
//    public Reports(String report_Doc, String aContinue, String view_profile) {
//        Report_Doc = report_Doc;
//        Continue = aContinue;
//        View_profile = view_profile;
//    }


    public int getservice_id() {
        return str_service_id;
    }

    public void setDoc_date(String doc_date) {
        Doc_date = doc_date;
    }

//    public TextView getDownload() {
//        return Download;
//    }

    public void setservice_id(int service_id) {
        this.service_id = service_id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


//    public String getContinue() {
//        return Continue;
//    }
//
//    public void setContinue(String aContinue) {
//        Continue = aContinue;
//    }

    public Services(String services_doc, int id) {
        this.Service_doc = services_doc;
        this.str_service_id = id;
    }

    public String getService_doc() {
        return Service_doc;
    }

    public void setService_doc(String report_Doc) {
        Service_doc = report_Doc;
    }
}