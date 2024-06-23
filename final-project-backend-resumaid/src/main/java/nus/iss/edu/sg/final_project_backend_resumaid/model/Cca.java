package nus.iss.edu.sg.final_project_backend_resumaid.model;

import java.util.Date;

public class Cca {

    private String cName;
    private String cCountry;
    private String cTitle;
    private Date cFrom;
    private Boolean cCurrent;
    private Date cTo;
    private String cPoints;

    public Cca() {
    }

    public Cca(String cName, String cCountry, String cTitle, Date cFrom, Boolean cCurrent, Date cTo, String cPoints) {
        this.cName = cName;
        this.cCountry = cCountry;
        this.cTitle = cTitle;
        this.cFrom = cFrom;
        this.cCurrent = cCurrent;
        this.cTo = cTo;
        this.cPoints = cPoints;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcCountry() {
        return cCountry;
    }

    public void setcCountry(String cCountry) {
        this.cCountry = cCountry;
    }

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public Date getcFrom() {
        return cFrom;
    }

    public void setcFrom(Date cFrom) {
        this.cFrom = cFrom;
    }

    public Boolean getcCurrent() {
        return cCurrent;
    }

    public void setcCurrent(Boolean cCurrent) {
        this.cCurrent = cCurrent;
    }

    public Date getcTo() {
        return cTo;
    }

    public void setcTo(Date cTo) {
        this.cTo = cTo;
    }

    public String getcPoints() {
        return cPoints;
    }

    public void setcPoints(String cPoints) {
        this.cPoints = cPoints;
    }

}
