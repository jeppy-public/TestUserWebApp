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

/**
 * The UserForm class represents a form for creating, updating, and clearing user information.
 * It includes fields for user ID, name, and email, along with buttons for adding, updating,
 * clearing the form, and generating a user report.
 * The form integrates with the UserService to perform user operations and the ReportGenerator
 * to create and download a user report.
 */
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

    /**
     * Constructs a UserForm with the given UserService, Runnable to update the grid,
     * and ReportGenerator for generating user reports.
     *
     * @param userService the service responsible for user operations
     * @param onGridUpdate a callback that triggers a grid update after user operations
     * @param reportGenerator the generator responsible for creating user reports
     */
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

    /**
     * Configures the data binder to bind form fields to the User object.
     */
    private void configureBinder() {
        userBinder.bindFields(userIDField, nameField, emailField);
    }

    /**
     * Configures the theme variants for the buttons in the form.
     */
    private void configureButtonTheme(){
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        clearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Configures the form by adding click listeners to the buttons, setting the button layout,
     * and making sure the update button is initially disabled.
     */
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

    /**
     * Handles the adding of a new user. The user is validated, created, and saved
     * using the UserService. The form is cleared and the grid is updated afterward.
     */
    private void handleAddUser() {
        if(userBinder.validate()){
            if (userService.findByUserIDAndStatusActive(userIDField.getValue()).isPresent()) {
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

    /**
     * Handles updating an existing user's information. The selected user is validated and saved,
     * and the form is cleared afterward.
     */
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

    /**
     * Generates a report for all users, creating a PDF file and making it available for download.
     * The report is generated using the ReportGenerator and the download link is displayed.
     */
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

    /**
     * Populates the form with the details of the selected user. The form will be in "update" mode.
     *
     * @param user the user whose details will be populated into the form
     */
    public void populateForm(User user) {
        selectedUser = user;
        userBinder.readBean(user);
        userIDField.setEnabled(false);
        updateButton.setEnabled(true);
    }

    /**
     * Clears the form and resets the state. It also disables the update button.
     */
    public void clearForm() {
        userBinder.clear();
        selectedUser = null;
        userIDField.setEnabled(true);
        updateButton.setEnabled(false);
    }
}