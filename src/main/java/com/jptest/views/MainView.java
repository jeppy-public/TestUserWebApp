package com.jptest.views;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import com.jptest.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route
@UIScope // Ensures a separate instance for each UI
@Component
public class MainView extends VerticalLayout {
    private final UserService userService;
    private final Grid<User> userGrid = new Grid<>(User.class);
    private final TextField userIDField = new TextField("User ID");
    private final TextField nameField = new TextField("Name");
    private final TextField emailField = new TextField("Email");
    private final TextField statusField = new TextField("Status");
    private final Button addButton = new Button("Add User");
    private final Button updateButton = new Button("Update User");
    private final Button clearButton = new Button("Clear");

    private User selectedUser;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    @Autowired
    public MainView(UserService userService) {
        this.userService = userService;

        // Initialize layout
        setSizeFull();
        configureGrid();
        configureForm();

        add(userGrid, new FormLayout(userIDField, nameField, emailField, addButton, updateButton, clearButton));

        updateGrid();
    }

    private void configureGrid() {
        configureButtonTheme();
        configureColumns();
        configureCreateDateColumn();
        configureUpdateDateColumn();
        configureDeleteButtonColumn();
        configureSelectionListener();
    }

    private void configureButtonTheme(){
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        clearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void configureColumns(){
        userGrid.setColumns("userID", "name", "email", "status", "createBy", "updateBy");
    }

    private void configureCreateDateColumn() {
        userGrid.addColumn(user -> user.getCreateDate() != null ? user.getCreateDate().format(formatter) : "")
                .setHeader("Create Date")
                .setWidth("200px")
                .setFlexGrow(0)
                .setSortable(true)
                .setKey("createDate")
                .setComparator((user1, user2) -> {
                    if (user1.getCreateDate() == null) return -1;
                    if (user2.getCreateDate() == null) return 1;
                    return user1.getCreateDate().compareTo(user2.getCreateDate());
                });
    }

    private void configureUpdateDateColumn() {
        userGrid.addColumn(user -> user.getUpdateDate() != null ? user.getUpdateDate().format(formatter) : "")
                .setHeader("Update Date")
                .setWidth("200px")
                .setFlexGrow(0)
                .setSortable(true)
                .setKey("updateDate")
                .setComparator((user1, user2) -> {
                    if (user1.getUpdateDate() == null) return -1;
                    if (user2.getUpdateDate() == null) return 1;
                    return user1.getUpdateDate().compareTo(user2.getUpdateDate());
                });
    }

    private void configureDeleteButtonColumn() {
        userGrid.addComponentColumn(user -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(event -> handleDeleteUser(user));
            return deleteButton;
        });
    }

    private void handleDeleteUser(User user) {
        user.setStatus(UserStatus.DELETED);
        user.setUpdateDate(LocalDateTime.now());
        user.setUpdateBy("admin");
        userService.save(user);
        updateGrid();
        clearForm();
        Notification.show("User marked as Deleted");
    }

    private void configureSelectionListener() {
        //userGrid.asSingleSelect().addValueChangeListener(event -> handleUserSelection(event.getValue()));
        userGrid.asSingleSelect().addValueChangeListener(
                event -> {
                    selectedUser = event.getValue();
                    if(selectedUser != null){
                        populateForm(selectedUser);
                    } else {
                        clearForm();
                    }
                }
        );
    }

    private void populateForm(User user) {
        userIDField.setValue(user.getUserID());
        userIDField.setEnabled(false);
        nameField.setValue(user.getName());
        emailField.setValue(user.getEmail());
        statusField.setValue(user.getStatus().getLabel());
        updateButton.setEnabled(true);
    }

    private void configureForm() {
        addButton.addClickListener(event -> {
            if (!nameField.isEmpty() && !emailField.isEmpty()) {

                if(userService.findByUserIDAndStatusActive(userIDField.getValue()) != null){
                    Notification.show("UserID already exists", 3000, Notification.Position.MIDDLE);
                    return;
                }

                User user = new User();
                user.setUserID(userIDField.getValue());
                user.setName(nameField.getValue());
                user.setEmail(emailField.getValue());
                user.setStatus(UserStatus.ACTIVE);
                user.setCreateDate(LocalDateTime.now());
                user.setCreateBy("admin");
                userService.save(user);
                updateGrid();
                clearForm();
                Notification.show("User added");
            } else {
                Notification.show("Please fill in all fields", 3000, Notification.Position.MIDDLE);
            }
        });

        updateButton.addClickListener( event ->{
            if(selectedUser != null){
                selectedUser.setName(nameField.getValue());
                selectedUser.setEmail(emailField.getValue());
                selectedUser.setUpdateDate(LocalDateTime.now());
                selectedUser.setUpdateBy("Admin");
                userService.save(selectedUser);
                updateGrid();
                clearForm();
                Notification.show("User updated");
            }

        });

        clearButton.addClickListener(event -> clearForm());

        updateButton.setEnabled(false);
    }

    private void updateGrid() {
        userGrid.setItems(userService.findAllUser());
    }

    private void clearForm(){
        userIDField.clear();
        nameField.clear();
        emailField.clear();
        selectedUser = null;
        userIDField.setEnabled(true);
        updateButton.setEnabled(false);
    }
}