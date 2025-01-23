package com.jptest.views.binder;

import com.jptest.entity.User;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import lombok.Getter;

@Getter
public class UserBinder {
    private final Binder<User> binder;

    public UserBinder() {
        binder = new Binder<>(User.class);
    }

    public void bindFields(TextField userIDField, TextField nameField, TextField emailField) {
        // UserID validation: Minimum 5, maximum 9 characters
        binder.forField(userIDField)
                .asRequired("User ID is required")
                .withValidator(userID -> userID.length() >= 5 && userID.length() <= 9,
                        "User ID must be between 5 and 9 characters")
                .bind(User::getUserID, User::setUserID);

        // Name validation: Minimum 3, maximum 35 characters
        binder.forField(nameField)
                .asRequired("Name is required")
                .withValidator(name -> name.length() >= 3 && name.length() <= 35,
                        "Name must be between 3 and 35 characters")
                .bind(User::getName, User::setName);

        // Email validation: Valid email format
        binder.forField(emailField)
                .asRequired("Email is required")
                .withValidator(new EmailValidator("Invalid email address"))
                .bind(User::getEmail, User::setEmail);
    }

    public void readBean(User user) {
        binder.readBean(user);
    }

    public void writeBean(User user) throws ValidationException {
        binder.writeBean(user);
    }

    public boolean validate() {
        return binder.validate().isOk();
    }

    public void clear() {
        binder.readBean(null);
    }
}