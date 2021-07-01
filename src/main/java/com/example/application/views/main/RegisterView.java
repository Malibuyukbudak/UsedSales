package com.example.application.views.main;

import com.example.application.models.User;
import com.example.application.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/register")
public class RegisterView extends VerticalLayout {
    private final UserService userService;

    public RegisterView(UserService userService) {

        this.userService = userService;

        TextField txtFirstName = new TextField("First Name");
        TextField txtLastName = new TextField("Last Name");
        TextField txtEmail = new TextField("Email");
        PasswordField txtPassword = new PasswordField("Password");

        Button signUpBtn = new Button("Sign Up");

        signUpBtn.addClickListener(buttonClickEvent -> {
            User user=new User();
            user.setFirstName(txtFirstName.getValue());
            user.setLastName(txtLastName.getValue());
            user.setPassword(txtPassword.getValue());
            user.setEmail(txtEmail.getValue());
            userService.save(user);
            UI.getCurrent().getPage().setLocation("/login");
        });
        Button signInBtn=new Button("Sign In");
        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.add(signUpBtn,signInBtn);

        signInBtn.addClickListener(buttonClickEvent -> {
            UI.getCurrent().getPage().setLocation("/login");
        });
        add(txtFirstName, txtLastName, txtEmail, txtPassword, horizontalLayout);


    }
}
