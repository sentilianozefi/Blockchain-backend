package com.example.demo.Controller;

import java.sql.SQLException;

import com.example.demo.Model.Report;
import com.example.demo.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000/", "http://192.168.70.108:3000/" })

@RestController
public class ReportController {
    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/CreateReport", method = RequestMethod.POST)
    public ResponseEntity<Object> createReport(@RequestBody Report report) throws SQLException {
        reportService.createReport(report);
        return new ResponseEntity<>("User has been created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/GetReports/{username}", method = RequestMethod.GET)
    public ResponseEntity<Object> GetReports(@PathVariable("username") String username) {
        return new ResponseEntity<>(reportService.getUserReports(username), HttpStatus.OK);

    }

    @RequestMapping(value = "/UpdateReport/{id}/{canceled}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @PathVariable("canceled") Boolean canceled) {
        reportService.updateReport(id, canceled);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @RequestMapping(value = "/UpdateReportEssence/{id}/{newText}/{newtitle}", method = RequestMethod.PUT)

    public ResponseEntity<Object> updateUserEss(@PathVariable("id") Long id, @PathVariable("newText") String newText,@PathVariable("newtitle")String newtitle)
            throws SQLException {

        reportService.updateReportEssence(id, newText,newtitle);

        return new ResponseEntity<>("Updated", HttpStatus.OK);

    }

    @RequestMapping(value = "/Updatevisibility/{visible}/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser(@PathVariable("visible") Boolean visible, @PathVariable("id") long id)
            throws SQLException {
        reportService.setReportVisibility(visible, id);
        return new ResponseEntity<>("Updated", HttpStatus.OK);

    }
}
