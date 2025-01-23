package com.jptest.views.binder;

import com.jptest.entity.User;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import lombok.Getter;

/**
 * The UserBinder class provides validation and binding functionality for user-related form fields.
 * It uses Vaadin's Binder class to perform field validation for fields such as user ID, name, and email.
 * It is designed to be used for binding user form fields to the User object, ensuring proper validation
 * is applied before saving the user data.
 */
@Getter
public class UserBinder {
    private final Binder<User> binder;

    /**
     * Constructs a new UserBinder that initializes the Binder for the User class.
     * The binder will be used to bind fields and validate the user form inputs.
     */
    public UserBinder() {
        binder = new Binder<>(User.class);
    }

    /**
     * Binds the provided form fields to the User object and applies validation rules.
     * The following fields are validated:
     * - UserID: Must be between 5 and 9 characters long and contain only alphanumeric characters.
     * - Name: Must be between 3 and 35 characters long and contain only alphabetic characters and spaces.
     * - Email: Must be a valid email address.
     *
     * @param userIDField the field for the user ID
     * @param nameField the field for the user name
     * @param emailField the field for the user email
     */
    public void bindFields(TextField userIDField, TextField nameField, TextField emailField) {
        // UserID validation: Minimum 5, maximum 9 characters
        binder.forField(userIDField)
                .asRequired("User ID is required")
                .withValidator(userID -> userID.length() >= 5 && userID.length() <= 9,
                        "User ID must be between 5 and 9 characters")
                .withValidator(userID -> userID.matches("^[a-zA-Z0-9]+$"),
                        "User ID must not contain special characters or spaces")
                .bind(User::getUserID, User::setUserID);

        // Name validation: Minimum 3, maximum 35 characters
        binder.forField(nameField)
                .asRequired("Name is required")
                .withValidator(name -> name.length() >= 3 && name.length() <= 35,
                        "Name must be between 3 and 35 characters")
                .withValidator(name -> name.matches("^[a-zA-Z ]+$"),
                        "Name must not contain special characters or numbers")
                .bind(User::getName, User::setName);

        // Email validation: Valid email format
        binder.forField(emailField)
                .asRequired("Email is required")
                .withValidator(new EmailValidator("Invalid email address"))
                .bind(User::getEmail, User::setEmail);
    }

    /**
     * Reads the data from the provided User object and sets it in the binder.
     * This method is typically used to populate the form fields with the user data.
     *
     * @param user the User object whose data will be read into the binder
     */
    public void readBean(User user) {
        binder.readBean(user);
    }

    /**
     * Writes the data from the form fields into the provided User object.
     * This method is used to save the user data after validation.
     *
     * @param user the User object to which the form data will be written
     * @throws ValidationException if the form data is invalid
     */
    public void writeBean(User user) throws ValidationException {
        binder.writeBean(user);
    }

    /**
     * Validates the current form data. Returns true if all fields are valid, otherwise false.
     *
     * @return true if the form is valid, false otherwise
     */
    public boolean validate() {
        return binder.validate().isOk();
    }

    /**
     * Clears the current data in the binder, resetting all form fields.
     */
    public void clear() {
        binder.readBean(null);
    }
}