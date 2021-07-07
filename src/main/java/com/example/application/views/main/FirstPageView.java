package com.example.application.views.main;

import com.example.application.models.Product;
import com.example.application.services.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

@Route("/firstpage")
public class FirstPageView extends VerticalLayout {
    private final ProductService productService;
    Grid<Product> grid = new Grid<>(Product.class);
    Button signInButton=new Button();

    public FirstPageView(ProductService productService) {
        this.productService = productService;
        grid.setColumns("user.firstName", "category.categoryType", "city", "cityDistrict", "address", "price", "image", "description", "date", "numberOfViews");
        refreshData();
        Button signInButton=new Button("Giriş Yap");
        Button signUpButton=new Button("Kayıt Ol");
        signInButton.addClickListener(buttonClickEvent -> {
            UI.getCurrent().getPage().setLocation("/login");
        });
        signUpButton.addClickListener(buttonClickEvent -> {
            UI.getCurrent().getPage().setLocation("/register");
        });
        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.add(signInButton,signUpButton);

        add(horizontalLayout,grid);


    }


    private void refreshData() {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.findAll());
        grid.setItems(productList);
    }


}

