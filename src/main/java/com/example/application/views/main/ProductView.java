package com.example.application.views.main;


import com.example.application.models.Category;
import com.example.application.models.Product;
import com.example.application.services.CategoryService;
import com.example.application.services.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Route
public class ProductView extends VerticalLayout {
    private final ProductService productService;
    private final CategoryService categoryService;
    Grid<Product> grid = new Grid<>(Product.class);



    public ProductView(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;


        Dialog dialog = new Dialog();
        dialog.setModal(true);

        //Properties

        TextField textDate = new TextField("Date", "Enter Your Date");
        textDate.setValue(String.valueOf(LocalDate.now()));

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
        formLayout.add(textDate, selectCity, textCityDistrict, textAddress, textPrice, textImage, textDescription, selectCategory, textUser);
        //Seach
        Button btnFilter = new Button("Seach", VaadinIcon.SEARCH.create());

        TextField textFilter = new TextField();
        textFilter.setPlaceholder("Key");

        HorizontalLayout filterGroup = new HorizontalLayout();

        filterGroup.add(textFilter, btnFilter);

        btnFilter.addClickListener(buttonClickEvent -> {
            refreshData(textFilter.getValue());
        });
        //Save-Cancel
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
            product.setDate(LocalDate.parse(textDate.getValue()));
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


        //Add Product click to dialog
        Button btnEkle = new Button("Add Product", VaadinIcon.INSERT.create());

        btnEkle.addClickListener(buttonClickEvent -> {

            dialog.open();
        });


        grid.removeColumnByKey("id");
        grid.setColumns("user", "city", "cityDistrict", "address", "price", "image", "description", "category", "date");


        refreshData();
        add(btnEkle, filterGroup, grid);
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
