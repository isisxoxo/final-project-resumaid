package nus.iss.edu.sg.final_project_backend_resumaid.model;

import java.util.Date;

public class Education {

    private String eName;
    private String eCountry;
    private String eCert;
    private Date eFrom;
    private Boolean eCurrent;
    private Date eTo;
    private String ePoints;

    public Education() {
    }

    public Education(String eName, String eCountry, String eCert, Date eFrom, Boolean eCurrent, Date eTo,
            String ePoints) {
        this.eName = eName;
        this.eCountry = eCountry;
        this.eCert = eCert;
        this.eFrom = eFrom;
        this.eCurrent = eCurrent;
        this.eTo = eTo;
        this.ePoints = ePoints;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteCountry() {
        return eCountry;
    }

    public void seteCountry(String eCountry) {
        this.eCountry = eCountry;
    }

    public String geteCert() {
        return eCert;
    }

    public void seteCert(String eCert) {
        this.eCert = eCert;
    }

    public Date geteFrom() {
        return eFrom;
    }

    public void seteFrom(Date eFrom) {
        this.eFrom = eFrom;
    }

    public Boolean geteCurrent() {
        return eCurrent;
    }

    public void seteCurrent(Boolean eCurrent) {
        this.eCurrent = eCurrent;
    }

    public Date geteTo() {
        return eTo;
    }

    public void seteTo(Date eTo) {
        this.eTo = eTo;
    }

    public String getePoints() {
        return ePoints;
    }

    public void setePoints(String ePoints) {
        this.ePoints = ePoints;
    }

}
