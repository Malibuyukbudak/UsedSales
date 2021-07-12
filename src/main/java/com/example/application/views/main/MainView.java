package com.example.application.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


@Route
public class MainView extends VerticalLayout {
    public MainView(){

        if(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId")==null){
            if (UI.getCurrent() != null) {
                UI.getCurrent().getPage().setLocation("/firstpage");
            }
        }
    }
}
