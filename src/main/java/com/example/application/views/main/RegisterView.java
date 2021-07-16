package com.example.application.views.main;

import com.example.application.models.User;
import com.example.application.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
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
        H2 header=new H2("Kayıt Ol");

        TextField txtFirstName = new TextField("İsim");
        TextField txtLastName = new TextField("Soyisim");
        TextField txtEmail = new TextField("Email");
        PasswordField txtPassword = new PasswordField("Şifre");

        Button signUpBtn = new Button("Kayıt Ol");

        signUpBtn.addClickListener(buttonClickEvent -> {
            User user=new User();
            user.setFirstName(txtFirstName.getValue());
            user.setLastName(txtLastName.getValue());
            user.setPassword(txtPassword.getValue());
            user.setEmail(txtEmail.getValue());
            userService.save(user);
            UI.getCurrent().getPage().setLocation("/login");
        });

        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.add(signUpBtn);

        add(header,txtFirstName, txtLastName, txtEmail, txtPassword, horizontalLayout);


    }
}
