package com.example.application.views.main;

import com.example.application.models.Product;
import com.example.application.services.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Route("/firstpage")
public class FirstPageView extends VerticalLayout {
    private final ProductService productService;
    Grid<Product> grid = new Grid<>(Product.class);
    Button signInButton=new Button();

    public FirstPageView(ProductService productService) {
        this.productService = productService;
        grid.setColumns("user.firstName", "category.categoryType", "city", "cityDistrict", "address", "price","description", "date", "numberOfViews");
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

        Dialog clickDialog1=new Dialog();
        clickDialog1.setHeight("1000px");
        clickDialog1.setWidth("500px");

        grid.addItemClickListener(productItemClickEvent -> {
            TextArea txtUser1 = new TextArea();
            txtUser1.setLabel("İsim");
            txtUser1.setValue(productItemClickEvent.getItem().getUser().getFirstName().toString());
            txtUser1.setReadOnly(true);

            TextArea txtCity1 = new TextArea();
            txtCity1.setLabel("Şehir");
            txtCity1.setValue(productItemClickEvent.getItem().getCity().toString());
            txtCity1.setReadOnly(true);

            TextArea txtCategory1 = new TextArea();
            txtCategory1.setLabel("Kategori");
            txtCategory1.setValue(productItemClickEvent.getItem().getCategory().getCategoryType().toString());
            txtCategory1.setReadOnly(true);

            TextArea txtAdress1 = new TextArea();
            txtAdress1.setLabel("Adres");
            txtAdress1.setValue(productItemClickEvent.getItem().getAddress().toString());
            txtAdress1.setReadOnly(true);

            TextArea txtCityDistrict1 = new TextArea();
            txtCityDistrict1.setLabel("İlçe");
            txtCityDistrict1.setValue(productItemClickEvent.getItem().getCityDistrict().toString());
            txtCityDistrict1.setReadOnly(true);

            TextArea txtPrice1 = new TextArea();
            txtPrice1.setLabel("Fiyat");
            txtPrice1.setValue(productItemClickEvent.getItem().getPrice().toString());
            txtPrice1.setReadOnly(true);

            TextArea txtDescription1 = new TextArea();
            txtDescription1.setLabel("Açıklama");
            txtDescription1.setValue(productItemClickEvent.getItem().getDescription().toString());
            txtDescription1.setReadOnly(true);

            TextArea txtNumberofView1 = new TextArea();
            txtNumberofView1.setLabel("Görüntülenme Sayısı");
            txtNumberofView1.setValue(productItemClickEvent.getItem().getNumberOfViews().toString());
            txtNumberofView1.setReadOnly(true);


            Image image1 = new Image();
            image1.setWidth("100px");
            image1.setHeight("100px");

            if (productItemClickEvent.getItem().getImageFileName() != null) {
                StreamResource resource = new StreamResource(productItemClickEvent.getItem().getImageFileName(), () -> new ByteArrayInputStream((productItemClickEvent.getItem().getImage())));
                image1.setSrc(resource);

            } else {
                image1.setSrc("");
            }
            clickDialog1.open();
            Button cancelBtnFirst=new Button("Kapat");
            VerticalLayout clickHorizontal=new VerticalLayout();
            clickHorizontal.add(txtUser1, txtCategory1, txtCity1, txtCityDistrict1, txtAdress1, txtPrice1, txtDescription1, txtNumberofView1, image1, cancelBtnFirst);
            clickDialog1.add(clickHorizontal);

            cancelBtnFirst.addClickListener(buttonClickEvent -> {

                clickDialog1.remove(clickHorizontal);
                clickDialog1.close();
            });
        });


        add(horizontalLayout,grid);


    }


    private void refreshData() {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.findAll());
        grid.setItems(productList);
    }


}

