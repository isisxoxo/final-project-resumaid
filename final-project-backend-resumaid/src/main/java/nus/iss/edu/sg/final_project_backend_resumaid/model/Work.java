package nus.iss.edu.sg.final_project_backend_resumaid.model;

import java.util.Date;

public class Work {

    private String wName;
    private String wCountry;
    private String wRole;
    private Date wFrom;
    private Boolean wCurrent;
    private Date wTo;
    private String wPoints;

    public Work() {
    }

    public Work(String wName, String wCountry, String wRole, Date wFrom, Boolean wCurrent, Date wTo, String wPoints) {
        this.wName = wName;
        this.wCountry = wCountry;
        this.wRole = wRole;
        this.wFrom = wFrom;
        this.wCurrent = wCurrent;
        this.wTo = wTo;
        this.wPoints = wPoints;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public String getwCountry() {
        return wCountry;
    }

    public void setwCountry(String wCountry) {
        this.wCountry = wCountry;
    }

    public String getwRole() {
        return wRole;
    }

    public void setwRole(String wRole) {
        this.wRole = wRole;
    }

    public Date getwFrom() {
        return wFrom;
    }

    public void setwFrom(Date wFrom) {
        this.wFrom = wFrom;
    }

    public Boolean getwCurrent() {
        return wCurrent;
    }

    public void setwCurrent(Boolean wCurrent) {
        this.wCurrent = wCurrent;
    }

    public Date getwTo() {
        return wTo;
    }

    public void setwTo(Date wTo) {
        this.wTo = wTo;
    }

    public String getwPoints() {
        return wPoints;
    }

    public void setwPoints(String wPoints) {
        this.wPoints = wPoints;
    }

}
