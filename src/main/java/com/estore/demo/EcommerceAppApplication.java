package com.estore.demo;

import com.estore.demo.common.domain.UserInfo;
import com.estore.demo.notification.domain.ChannelType;
import com.estore.demo.notification.domain.EventName;
import com.estore.demo.notification.domain.Template;
import com.estore.demo.notification.repo.ITemplateRepository;
import com.estore.demo.product.domain.ProductCategory;
import com.estore.demo.product.domain.ProductImage;
import com.estore.demo.product.repo.IProductCategoryRepository;
import com.estore.demo.product.repo.IProductImageRepository;
import com.estore.demo.user.domain.RoleBasedCapabilityAccess;
import com.estore.demo.user.domain.User;
import com.estore.demo.user.repo.IRBACRepository;
import com.estore.demo.user.repo.IUserInfoRepository;
import com.estore.demo.user.repo.IUserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jms.annotation.EnableJms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Method to bootstrap Spring Boot application
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.estore.demo.**.repo")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.estore.demo"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJms
public class EcommerceAppApplication implements CommandLineRunner {
    @Autowired
    private IUserInfoRepository userInfoRepository;

    @Autowired
    private IProductImageRepository imageRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRBACRepository rbacRepository;

    @Autowired
    private ITemplateRepository templateRepository;

    @Autowired
    private IProductCategoryRepository productCategoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceAppApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        clearDatabases();
		/*
		add users in Mongo (would be removed with any LDAP provider)
		 */
        List<UserInfo> usersInLDAP = createUsersInLDAP();
        userInfoRepository.save(usersInLDAP);

        List<ProductImage> images = createImages();
        imageRepository.save(images);

        List<User> users = createUsers();
        for (User user : users) {
            User fetchedUser = userRepository.findById(user.getId());
            if (null == fetchedUser) {
                userRepository.save(user);
            }
        }

        List<RoleBasedCapabilityAccess> capabilitiesList = createRBAC();
        rbacRepository.save(capabilitiesList);

        List<ProductCategory> categories = createProductCategories();
        productCategoryRepository.save(categories);

        List<Template> templates = createTemplates();
        templateRepository.save(templates);
    }

    /*
    creates template metadata in collection to send notification
     */
    private List<Template> createTemplates() {
        List<Template> templates = new ArrayList<Template>() {{
            add(new Template(EventName.POSTORUPDATEPRODUCT, "PostOrUpdateProduct", ChannelType.EMAIL));
            add(new Template(EventName.POSTORUPDATEPRODUCT, "PostOrUpdateProduct_sms", ChannelType.TEXT));
            add(new Template(EventName.PLACEORDER, "PlaceAnOrder", ChannelType.EMAIL));
            add(new Template(EventName.PLACEORDER, "PlaceAnOrder_sms", ChannelType.TEXT));
        }};
        return templates;
    }

    /*
    creates product categories metadata in collection
     */
    private List<ProductCategory> createProductCategories() {
        List<ProductCategory> categories = new ArrayList<>();
        ProductCategory category = new ProductCategory();
        category.setName("Electronics");
        category.setParent(null);
        category.setAncestors(null);
        category.setTags(Arrays.asList("Electronics", "Electronic Equipments", "Technology"));
        categories.add(category);

        category = new ProductCategory();
        category.setName("Mobiles");
        category.setParent("Electronics");
        category.setAncestors(null);
        category.setTags(Arrays.asList("Mobile", "Technology", "Gadget"));
        categories.add(category);

        category = new ProductCategory();
        category.setName("TouchScreen Mobiles");
        category.setParent("Mobiles");
        category.setAncestors(Arrays.asList("Electronics"));
        category.setTags(Arrays.asList("Mobile", "Touch Screen"));
        categories.add(category);

        category = new ProductCategory();
        category.setName("Basic Mobiles");
        category.setParent("Mobiles");
        category.setAncestors(Arrays.asList("Electronics"));
        category.setTags(Arrays.asList("Mobile", "Basic", "Button"));
        categories.add(category);

        category = new ProductCategory();
        category.setName("Home Appliances");
        category.setParent("Electronics");
        category.setAncestors(null);
        category.setTags(Arrays.asList("Technology", "Day-to-Day Utilities", "Utilties"));
        categories.add(category);

        return categories;
    }

    /*
    creates role based user permissions and capabilites in collection
     */
    private List<RoleBasedCapabilityAccess> createRBAC() {
        List<RoleBasedCapabilityAccess> list = new ArrayList<>();
        RoleBasedCapabilityAccess r1 = new RoleBasedCapabilityAccess();
        r1.setCapabilityId("Cart.AddToCart");
        r1.setRole("USER");
        r1.setPermissions(Arrays.asList("UPDATE"));
        list.add(r1);
        r1 = new RoleBasedCapabilityAccess();
        r1.setCapabilityId("Product.AddProduct");
        r1.setRole("SELLER");
        r1.setPermissions(Arrays.asList("UPDATE"));
        list.add(r1);
        r1 = new RoleBasedCapabilityAccess();
        r1.setCapabilityId("Order.PlaceOrder");
        r1.setRole("USER");
        r1.setPermissions(Arrays.asList("UPDATE"));
        list.add(r1);
        r1 = new RoleBasedCapabilityAccess();
        r1.setCapabilityId("Dashboard.SearchProduct");
        r1.setRole("USER");
        r1.setPermissions(Arrays.asList("READ"));
        list.add(r1);

        return list;
    }

    /*
    creates user related information in collection
     */
    private List<User> createUsers() throws IOException {
        String usrStr = "[{\n" +
                "  \"id\":\"user01\",\n" +
                "  \"firstName\":\"Nikhil\",\n" +
                "  \"lastName\" : \"Raj\",\n" +
                "  \"email\":\"nikhil123@yahoo.com\",\n" +
                "  \"addressList\":[\n" +
                "    {\n" +
                "      \"addressType\":\"BILLING\",\n" +
                "      \"address1\":\"Test\",\n" +
                "      \"state\":\"Maharashtra\",\n" +
                "      \"country\":\"India\",\n" +
                "      \"zipCode\":842001\n" +
                "    }\n" +
                "  ],\n" +
                "  \"contactsList\":[\n" +
                "    {\n" +
                "      \"contactType\":\"MOBILE\",\n" +
                "      \"phoneNumber\":9175472189\n" +
                "    }],\n" +
                "  \"type\":\"buyer\" \n" +
                "}, \n" +
                "{\n" +
                "  \"id\":\"seller01\",\n" +
                "  \"firstName\":\"Awesome\",\n" +
                "  \"lastName\" : \"Seller\",\n" +
                "  \"email\":\"seller456@yahoo.com\",\n" +
                "  \"addressList\":[\n" +
                "    {\n" +
                "      \"addressType\":\"BILLING\",\n" +
                "      \"address1\":\"SellerTest\",\n" +
                "      \"state\":\"Bihar\",\n" +
                "      \"country\":\"India\",\n" +
                "      \"zipCode\":842001\n" +
                "    }\n" +
                "  ],\n" +
                "  \"contactsList\":[\n" +
                "    {\n" +
                "      \"contactType\":\"MOBILE\",\n" +
                "      \"phoneNumber\":9019782012\n" +
                "    }],\n" +
                "  \"type\":\"seller\",\n" +
                "  \"companyId\":\"COM01\" \n" +
                "}," +
                "{\n" +
                "  \"id\":\"seller02\",\n" +
                "  \"firstName\":\"Awesome\",\n" +
                "  \"lastName\" : \"Seller2\",\n" +
                "  \"email\":\"seller123@yahoo.com\",\n" +
                "  \"addressList\":[\n" +
                "    {\n" +
                "      \"addressType\":\"BILLING\",\n" +
                "      \"address1\":\"SellerTest\",\n" +
                "      \"state\":\"Bihar\",\n" +
                "      \"country\":\"India\",\n" +
                "      \"zipCode\":842001\n" +
                "    }\n" +
                "  ],\n" +
                "  \"contactsList\":[\n" +
                "    {\n" +
                "      \"contactType\":\"MOBILE\",\n" +
                "      \"phoneNumber\":9019782012\n" +
                "    }],\n" +
                "  \"type\":\"seller\",\n" +
                "  \"companyId\":\"COM01\" \n" +
                "}]";

        ObjectMapper objectMapper = new ObjectMapper();

        List<User> users = objectMapper.readValue(usrStr, new TypeReference<List<User>>() {
        });
        return users;
    }

    /*
    creates product images metadata in collection
     */
    private List<ProductImage> createImages() {
        List<ProductImage> images = new ArrayList<ProductImage>() {{
            add(new ProductImage("Oppo V5 Image1"));
            add(new ProductImage("Oppo V5 Image2"));
            add(new ProductImage("LG G1 Image1"));
            add(new ProductImage("LG G2 Image2"));
        }};
        return images;
    }

    /*
    clears database at the startup so that any corrupted data is cleaned again. Cleaning is only allowed for static data which
    is not going to change
     */
    private void clearDatabases() {
        userInfoRepository.deleteAll();
        imageRepository.deleteAll();
        rbacRepository.deleteAll();
        productCategoryRepository.deleteAll();
        userRepository.deleteAll();
        templateRepository.deleteAll();
    }

    /*
    creates users in collection. Temporary replacement for IDP
     */
    private List<UserInfo> createUsersInLDAP() {
        List<UserInfo> list = new ArrayList<>();
        UserInfo c1 = new UserInfo();
        c1.setUsername("nikhil@estore.com");
        c1.setRoles("USER");
        c1.setPassword("Nikhil");
        c1.setId("user01");
        list.add(c1);

        UserInfo c2 = new UserInfo();
        c2.setUsername("seller1@estore.com");
        c2.setRoles("SELLER");
        c2.setPassword("Seller");
        c2.setId("seller01");
        list.add(c2);

        UserInfo c3 = new UserInfo();
        c3.setUsername("seller2@estore.com");
        c3.setRoles("SELLER");
        c3.setPassword("Seller");
        c3.setId("seller02");
        list.add(c3);

        return list;
    }
}
