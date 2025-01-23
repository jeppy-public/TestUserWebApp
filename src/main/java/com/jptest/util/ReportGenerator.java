package com.jptest.util;

import com.jptest.entity.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportGenerator {
    public byte[] generateUserReport(List<User> userList) throws JRException {
        // Load the JasperReport template
        InputStream reportStream = getClass().getResourceAsStream("/reports/user_report.jrxml");

        // Compile the JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Create a data source from the user list
        JRBeanCollectionDataSource userListDataSource = new JRBeanCollectionDataSource(userList);

        // Add parameters if required (e.g., title, additional info)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reportTitle", "USER REPORT");
        parameters.put("userListDataSet", userListDataSource);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        // Export the report to a PDF byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        return outputStream.toByteArray();
    }
}