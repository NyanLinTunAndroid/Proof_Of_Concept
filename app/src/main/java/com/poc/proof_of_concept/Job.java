package com.poc.proof_of_concept;

/**
 * Created by Nyan Linn Htun on 10/20/2018.
 */

public class Job {

    int id, job_id;
    String  priority, company,address;
    Double lat, lng;

    public Job(int id, int job_id, String priority, String company, String address, Double lat, Double lng) {
        this.id = id;
        this.job_id = job_id;
        this.priority = priority;
        this.company = company;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
