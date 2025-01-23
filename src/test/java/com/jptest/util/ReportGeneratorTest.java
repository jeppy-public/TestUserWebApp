package com.jptest.util;

import com.jptest.entity.User;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("report")
class ReportGeneratorTest {
    @Autowired
    private ReportGenerator reportGenerator;

    @Test
    void testGenerateUserReport() throws JRException, IOException {
        // Arrange: Prepare dummy user data
        List<User> dummyUsers = UserHelper.getDummyUser(); // Assuming this method generates test users

        // Act: Generate the user report
        byte[] reportBytes = reportGenerator.generateUserReport(dummyUsers);

        // Assert: Verify the output
        assertNotNull(reportBytes, "Report bytes should not be null");
        assertTrue(reportBytes.length > 0, "Report bytes should not be empty");

        // Optionally, check if the PDF is valid
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(reportBytes)) {
            // Simple check: Read the PDF header bytes
            byte[] pdfHeader = new byte[4];
            inputStream.read(pdfHeader);

            String pdfSignature = new String(pdfHeader);
            assertEquals("%PDF", pdfSignature, "Generated file should be a valid PDF");
        }
    }
}