package com.example.application.views.main;


import com.example.application.models.Category;
import com.example.application.models.Message;
import com.example.application.models.Product;
import com.example.application.services.CategoryService;
import com.example.application.services.MessageService;
import com.example.application.services.ProductService;
import com.example.application.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Route
public class ProductView extends VerticalLayout {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final MessageService messageService;
    Grid<Product> grid = new Grid<>(Product.class);
    Dialog messageDialog= new Dialog();
    Dialog dialog = new Dialog();



    public ProductView(ProductService productService, CategoryService categoryService, UserService userService, MessageService messageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService=userService;
        this.messageService = messageService;


        dialog.setModal(true);
        messageDialog.setModal(true);
        messageDialog.setWidth("300px");
        messageDialog.setHeight("300px");


        //Properties

        DatePicker valueDatePicker = new DatePicker();
        LocalDate now = LocalDate.now();
        valueDatePicker.setValue(now);

        ComboBox selectCity = new ComboBox<>("City");
        String[] cities = new String[]{"Adana", "Adiyaman", "Afyon", "Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin", "Aydin",
                "Balikesir", "Bartin", "Batman", "Bayburt", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur", "Bursa", "Canakkale", "Cankiri",
                "Corum", "Denizli", "Diyarbakir", "Duzce", "Edirne", "Elazig", "Erzincan", "Erzurum", "Eskisehir", "Gaziantep", "Giresun",
                "Gumushane", "Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul", "Izmir", "Kahramanmaras", "Karabuk", "Karaman", "Kars",
                "Kastamonu", "Kayseri", "Kilis", "Kirikkale", "Kirklareli", "Kirsehir", "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa",
                "Mardin", "Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt",
                "Sinop", "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak"};

        selectCity.setItems(cities);
        selectCity.setPlaceholder("Enter City");

        TextField textCityDistrict = new TextField("City District", "Enter Your City District");
        TextField textAddress = new TextField("Address", "Enter Your Address");
        TextField textPrice = new TextField("Price", "Enter Your Price");
        TextField textImage = new TextField("Image", "Enter Your Image");
        TextField textDescription = new TextField("Description", "Enter Your Description");


        ComboBox selectCategory = new ComboBox<>("Category");
        List<Category> categories = categoryService.findAll();
        List<String> categoryFor = new ArrayList<>();
        for (Category categoryType : categories) {
            categoryFor.add(categoryType.getCategoryType());
        }

        selectCategory.setItems(categoryFor);
        selectCategory.setPlaceholder("Enter Category");

        TextField textUser = new TextField("User", "Enter Your User");


        FormLayout formLayout = new FormLayout();
        formLayout.add(valueDatePicker, selectCity, textCityDistrict, textAddress, textPrice, textImage, textDescription, selectCategory, textUser);

        //Properties Last


        //Filter Begin
        Button btnFilter = new Button("Seach", VaadinIcon.SEARCH.create());

        TextField textFilter = new TextField();
        textFilter.setPlaceholder("Key");

        HorizontalLayout filterGroup = new HorizontalLayout();

        filterGroup.add(textFilter, btnFilter);

        btnFilter.addClickListener(buttonClickEvent -> {
            refreshData(textFilter.getValue());
        });
        //Fİlter Last

        // Properties Save-Cancel Begin
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);

        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");


        btnSave.addClickListener(buttonClickEvent -> {
            Product product = new Product();
            Category category = new Category();

            category.setCategoryType(selectCategory.getValue().toString());
            product.setPrice(Double.valueOf(textPrice.getValue()));
            product.setCityDistrict(textCityDistrict.getValue());

            categoryService.save(category);

            product.setCategory(category);
            product.setAddress(textAddress.getValue());
            product.setDescription(textDescription.getValue());
            product.setDate(valueDatePicker.getValue());
            product.setCity(String.valueOf(selectCity.getValue()));
            //product.setUser();

            productService.save(product);
            refreshData(textFilter.getValue());
            dialog.close();

        });

        btnCancel.addClickListener(buttonClickEvent -> {
            dialog.close();
        });


        horizontalLayout.add(btnSave, btnCancel);
        dialog.add(formLayout, horizontalLayout);

        // Properties Save-Cancel Last


        //Add Product click to dialog
        Button btnEkle = new Button("Add Product", VaadinIcon.INSERT.create());

        btnEkle.addClickListener(buttonClickEvent -> {

            dialog.open();
        });

        //message begin

        TextArea textMessage=new TextArea();
        textMessage.setWidth("200px");
        textMessage.setWidth("200px");
            //message send
        Button sendMessageBtn=new Button("Send");
        sendMessageBtn.addClickListener(buttonClickEvent -> {
            Message message1=new Message();
            message1.setMessageText(textMessage.getValue());
            //message1.setUser();
            messageService.save(message1);
            messageDialog.close();

        });

            //message cancel
        Button cancelMessageBtn=new Button("Cancel");
        cancelMessageBtn.addClickListener(buttonClickEvent -> {
            messageDialog.close();
        });

        messageDialog.add(textMessage,sendMessageBtn,cancelMessageBtn);
            //
        //message last

        grid.removeColumnByKey("id");
        grid.setColumns("user", "city", "cityDistrict", "address", "price", "image", "description", "category", "date");
        grid.addComponentColumn(item -> createMessageButton()).setHeader("Message");

        refreshData();
        add(btnEkle, filterGroup, grid);
    }


    private Button createMessageButton() {
        @SuppressWarnings("unchecked")
        Button button = new Button("Message");
        button.addClickListener(buttonClickEvent -> {
            messageDialog.open();

        });
        return button;
    }

    private void refreshData() {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.getList());
        grid.setItems(productList);
    }

    private void refreshData(String filter) {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.getList(filter));
        grid.setItems(productList);
    }
}
