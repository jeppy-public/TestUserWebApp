package com.jptest.views.form;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import com.jptest.service.UserService;
import com.jptest.util.ReportGenerator;
import com.jptest.views.binder.UserBinder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.StreamResource;
import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserForm extends FormLayout {
    private final TextField userIDField = new TextField("User ID");
    private final TextField nameField = new TextField("Name");
    private final TextField emailField = new TextField("Email");

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Anchor downloadLink = new Anchor();

    private final Button addButton = new Button("Add User");
    private final Button updateButton = new Button("Update User");
    private final Button clearButton = new Button("Clear");
    private final Button reportButton = new Button("Generate Report");

    private final UserBinder userBinder = new UserBinder();

    private final ReportGenerator reportGenerator;

    private final UserService userService;
    private final Runnable onGridUpdate;
    private User selectedUser;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    public UserForm(UserService userService, Runnable onGridUpdate, ReportGenerator reportGenerator) {
        this.userService = userService;
        this.onGridUpdate = onGridUpdate;
        this.reportGenerator = reportGenerator;

        configureBinder();
        configureButtonTheme();
        configureForm();

        // Initialize the download link
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.setText("Download Report");
        downloadLink.setVisible(false); // Initially hidden

        add(userIDField, nameField, emailField, buttonLayout, downloadLink);
    }

    private void configureBinder() {
        userBinder.bindFields(userIDField, nameField, emailField);
    }

    private void configureButtonTheme(){
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        clearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void configureForm() {
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        clearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addButton.addClickListener(event -> handleAddUser());
        updateButton.addClickListener(event -> handleUpdateUser());
        clearButton.addClickListener(event -> clearForm());
        reportButton.addClickListener(event -> generateReport());

        updateButton.setEnabled(false);

        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        buttonLayout.setSpacing(false);
        buttonLayout.add(new HorizontalLayout(addButton, updateButton, reportButton), clearButton);
    }

    private void handleAddUser() {
        if(userBinder.validate()){
            if (userService.findByUserIDAndStatusActive(userIDField.getValue()) != null) {
                Notification.show("UserID already exists", 3000, Notification.Position.MIDDLE);
                return;
            }

            User user = new User();
            try{
                userBinder.writeBean(user); // Automatically writes values to the user object
                user.setStatus(UserStatus.ACTIVE);
                user.setCreateDate(LocalDateTime.now());
                user.setCreateBy("admin");

                userService.save(user);

                onGridUpdate.run();
                clearForm();
                Notification.show("User added");
            }catch(ValidationException e){
                Notification.show("Validation failed", 3000, Notification.Position.MIDDLE);
            }
        } else {
            Notification.show("Please fill in all fields", 3000, Notification.Position.MIDDLE);
        }
    }

    private void handleUpdateUser() {
        if (selectedUser != null && userBinder.validate()) {
            try{
                userBinder.writeBean(selectedUser); // Automatically writes values to the selected user
                selectedUser.setUpdateDate(LocalDateTime.now());
                selectedUser.setUpdateBy("admin");

                userService.save(selectedUser);

                onGridUpdate.run();
                clearForm();
                Notification.show("User updated");
            }catch (ValidationException e) {
                Notification.show("Validation failed", 3000, Notification.Position.MIDDLE);
            }
        }
    }

    private void generateReport() {
        try {
            // Fetch the list of users
            List<User> userList = userService.findAllUser();

            // Generate the PDF report
            byte[] pdfReport = reportGenerator.generateUserReport(userList);

            // Trigger download in the browser
            StreamResource resource = new StreamResource("UserReport.pdf",
                    () -> new ByteArrayInputStream(pdfReport));
            downloadLink.setHref(resource);
            downloadLink.setVisible(true);

            Notification.show("Report generated successfully!");
            add(downloadLink);
        } catch (JRException e) {
            Notification.show("Error generating report: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    public void populateForm(User user) {
        selectedUser = user;
        userBinder.readBean(user);
        userIDField.setEnabled(false);
        updateButton.setEnabled(true);
    }

    public void clearForm() {
        userBinder.clear();
        selectedUser = null;
        userIDField.setEnabled(true);
        updateButton.setEnabled(false);
    }
}