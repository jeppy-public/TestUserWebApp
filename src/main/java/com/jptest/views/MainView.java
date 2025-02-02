package com.jptest.views;

import com.jptest.entity.User;
import com.jptest.enums.UserStatus;
import com.jptest.service.UserService;
import com.jptest.util.ReportGenerator;
import com.jptest.views.form.UserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * The MainView class represents the main user interface for displaying and managing users.
 * It includes a grid displaying user data, a form for adding and updating users,
 * and a delete functionality.
 * The view integrates with the UserService to fetch user data and perform CRUD operations.
 */
@Route
@UIScope
@Component
public class MainView extends VerticalLayout {
    private final UserService userService;
    private final Grid<User> userGrid = new Grid<>(User.class);
    private final UserForm userForm;
    private User selectedUser;

    /**
     * Constructs a MainView instance with the given UserService and ReportGenerator.
     *
     * @param userService the service responsible for user operations
     * @param reportGenerator the report generator for user-related reports
     */
    @Autowired
    public MainView(UserService userService, ReportGenerator reportGenerator) {
        this.userService = userService;
        this.userForm = new UserForm(userService, this::updateGrid, reportGenerator);

        // Initialize layout
        setSizeFull();
        configureGrid();
        add(userGrid, userForm);
        updateGrid();
    }

    /**
     * Configures the grid by setting up columns, date columns, delete button, and selection listener.
     */
    private void configureGrid() {
        configureColumns();
        configureCreateDateColumn();
        configureUpdateDateColumn();
        configureDeleteButtonColumn();
        configureSelectionListener();
    }

    /**
     * Configures the basic columns of the grid including userID, name, email, status, createBy, and updateBy.
     */
    private void configureColumns(){
        userGrid.setColumns("userID", "name", "email", "status", "createBy", "updateBy");

        userGrid.getColumnByKey("userID").setWidth("80px");
        userGrid.getColumnByKey("name").setWidth("200px");
        userGrid.getColumnByKey("email").setAutoWidth(true);
        userGrid.getColumnByKey("status").setWidth("100px");
        userGrid.getColumnByKey("createBy").setWidth("80px");
        userGrid.getColumnByKey("updateBy").setWidth("80px");
    }

    /**
     * Configures the "Create Date" column to display formatted dates and allow sorting.
     */
    private void configureCreateDateColumn() {
        userGrid.addColumn(user -> user.getCreateDate() != null ? user.getCreateDate().format(UserForm.FORMATTER) : "")
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

    /**
     * Configures the "Update Date" column to display formatted dates and allow sorting.
     */
    private void configureUpdateDateColumn() {
        userGrid.addColumn(user -> user.getUpdateDate() != null ? user.getUpdateDate().format(UserForm.FORMATTER) : "")
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

    /**
     * Configures a "Delete" button column for each user in the grid.
     * The button allows marking a user as deleted.
     */
    private void configureDeleteButtonColumn() {
        userGrid.addComponentColumn(user -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(event -> handleDeleteUser(user));
            return deleteButton;
        });
    }

    /**
     * Handles the deletion of a user by marking their status as DELETED and updating their details.
     * It also refreshes the grid and clears the form.
     *
     * @param user the user to be deleted
     */
    protected void handleDeleteUser(User user) {
        user.setStatus(UserStatus.DELETED);
        user.setUpdateDate(LocalDateTime.now());
        user.setUpdateBy("admin");
        userService.save(user);
        updateGrid();
        userForm.clearForm();
        Notification.show("User marked as Deleted");
    }

    /**
     * Configures a listener to handle user selection from the grid.
     * When a user is selected, the form is populated with the user's details.
     */
    private void configureSelectionListener() {
        userGrid.asSingleSelect().addValueChangeListener(
                event -> {
                    selectedUser = event.getValue();
                    if(selectedUser != null){
                        userForm.populateForm(selectedUser);
                    } else {
                        userForm.clearForm();
                    }
                }
        );
    }

    /**
     * Updates the grid by setting its items to the list of all users fetched from the user service.
     */
    private void updateGrid() {
        userGrid.setItems(userService.findAllUser());
    }
}