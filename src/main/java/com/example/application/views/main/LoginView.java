package com.example.application.views.main;

import com.example.application.models.User;
import com.example.application.services.UserService;
import com.vaadin.flow.component.UI;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("/login")
public class LoginView extends VerticalLayout {

    private  final UserService userService;

    public LoginView(UserService userService){

        this.userService=userService;

        LoginForm loginForm= new LoginForm();

        loginForm.addLoginListener(loginEvent -> {
            User result=userService.login(loginEvent.getUsername(),loginEvent.getPassword());

            if(result.getId()!=null){
                VaadinSession.getCurrent().getSession().setAttribute("LoggedInUserId",result.getId());
                UI.getCurrent().getPage().setLocation("/");
            }else{
                loginForm.setError(true);
            }
        });
        add(loginForm);
    }
}
