package com.example.demo.Service;

import java.sql.SQLException;
import java.util.Collection;


import com.example.demo.Model.Report;

public interface ReportService {
    public abstract void createReport(Report report) throws SQLException;
    public abstract void updateReport(long id ,boolean canceled);
    public abstract Collection<Report> getUserReports(String username);
    public abstract void updateReportEssence(long id , String newtext,String newtitle) throws SQLException;
    public abstract void setReportVisibility(Boolean visible,Long id) throws SQLException;

}
