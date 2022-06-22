package com.example.demo.Model;

public class Report {
    private long id;
    private long correntHash;
    private long hashcode;
    private String username;
    private String report;
    private boolean canceled = false;
    private String title;
    private String creationdate;
    private boolean display = true;

    public Report() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Report(String username, String report, boolean canceled, long id) {
        this.username = username;
        this.report = report;
        this.canceled = canceled;
        this.id = id;
    }

    public Report(String username, String report, boolean canceled, long id, String title) {
        this.username = username;
        this.report = report;
        this.canceled = canceled;
        this.id = id;
        this.title = title;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public Report(String username, String report, boolean canceled, long id, long hashCode, long correntHash) {
        this.username = username;
        this.report = report;
        this.canceled = canceled;
        this.hashcode = hashCode;
        this.correntHash = correntHash;
    }

    public long getCorrentHash() {
        return correntHash;
    }

    public void setCorrentHash(long correntHash) {
        this.correntHash = correntHash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHashcode() {
        return hashcode;
    }

    public void setHashcode(long hashcode) {
        this.hashcode = hashcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int reportsHash() {
        return this.report.hashCode();
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean getDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
