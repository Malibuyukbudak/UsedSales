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
        car.setCategoryType("Araba");
        categoryService.save(car);

        Category electronic=new Category();
        electronic.setCategoryType("Elektronik");
        categoryService.save(electronic);

        Category furniture=new Category();
        furniture.setCategoryType("Ev Eşyaları");
        categoryService.save(furniture);


        //Users
        //User1
        User user1=new User();
        user1.setFirstName("Mali");
        user1.setLastName("Buyukbudak");
        user1.setEmail("malibuyukbudak@gmail.com");
        user1.setPassword("user1");
        userService.save(user1);

        //User2
        User user2=new User();
        user2.setFirstName("Yusuf");
        user2.setLastName("Mirza");
        user2.setEmail("yusufmirza@gmail.com");
        user2.setPassword("user2");
        userService.save(user2);

        //User3
        User user3=new User();
        user3.setFirstName("Mustafa");
        user3.setLastName("Atmaca");
        user3.setEmail("mustafaatmaca@gmail.com");
        user3.setPassword("user3");
        userService.save(user3);

        //User4
        User user4=new User();
        user4.setFirstName("Burak");
        user4.setLastName("Yılmaz");
        user4.setEmail("burakyılmaz@gmail.com");
        user4.setPassword("user4");
        userService.save(user4);



        //Products

        //Product1
        Product product1=new Product();
        product1.setAddress("Istanbul Fatih address");
        product1.setCity("Istanbul");
        product1.setCityDistrict("Fatih");
        product1.setDate(LocalDate.now());
        product1.setDescription("Car description : fiat egea 2018 ");
        product1.setPrice(100000.0);
        product1.setCategory(car);
        product1.setUser(user1);
        product1.setNumberOfViews(0);
        productService.save(product1);

        //Product2

        Product product2=new Product();
        product2.setAddress("Edirne Merkez address");
        product2.setCity("Edirne");
        product2.setCityDistrict("Merkez");
        product2.setDate(LocalDate.now());
        product2.setDescription("Furniture descriptions : armchair");
        product2.setPrice(250.0);
        product2.setCategory(furniture);
        product2.setUser(user2);
        product2.setNumberOfViews(0);
        productService.save(product2);

        //Product3

        Product product3=new Product();
        product3.setAddress("Balıkesir Burhaniye address");
        product3.setCity("Balıkesir");
        product3.setCityDistrict("Burhaniye");
        product3.setDate(LocalDate.now());
        product3.setDescription("Electronic descriptions: computer");
        product3.setPrice(4600.0);
        product3.setCategory(electronic);
        product3.setUser(user3);
        product3.setNumberOfViews(0);
        productService.save(product3);

        //Product4

        Product product4=new Product();
        product4.setAddress("Izmir Konak address");
        product4.setCity("Izmir");
        product4.setCityDistrict("Konak");
        product4.setDate(LocalDate.now());
        product4.setDescription("Electronic descriptions: headphone");
        product4.setPrice(450.0);
        product4.setCategory(furniture);
        product4.setUser(user4);
        product4.setNumberOfViews(0);
        productService.save(product4);




    }
}
