package com.example.application.bootstrap;

import com.example.application.models.*;
import com.example.application.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BootstrapData implements CommandLineRunner {

    private final CategoryService categoryService;
    private final MessageService messageService;
    private final ProductService productService;

    private final UserService userService;

    public BootstrapData(CategoryService categoryService, MessageService messageService, ProductService productService, UserService userService) {
        this.categoryService = categoryService;
        this.messageService = messageService;
        this.productService = productService;
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {

        //CATEGORY
        Category car=new Category();
        car.setCategoryType("Car");
        categoryService.save(car);

        Category electronic=new Category();
        electronic.setCategoryType("Electronic");
        categoryService.save(electronic);

        Category furniture=new Category();
        furniture.setCategoryType("Furniture");
        categoryService.save(furniture);


        //User
        User user1=new User();
        user1.setFirstName("Mali");
        user1.setLastName("Buyukbudak");
        user1.setEmail("malibuyukbudak@gmail.com");
        user1.setPassword("user1password");
        userService.save(user1);



        //Product
        Product product1=new Product();
        product1.setAddress("urun adresi");
        product1.setCity("ıstanbul");
        product1.setCityDistrict("Fatih");
        product1.setDate(LocalDate.now());
        product1.setDescription("araba acıklamasi");
        product1.setPrice(1000.0);
        product1.setCategory(car);
        product1.setUser(user1);
        productService.save(product1);





    }
}
