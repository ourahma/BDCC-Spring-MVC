package net.ourahma.bdccfsspringmvc;

import net.ourahma.bdccfsspringmvc.entities.Product;
import net.ourahma.bdccfsspringmvc.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
public class BdccFsSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(BdccFsSpringMvcApplication.class, args);
    }

    // premier test pour exécuter des traitements au démarrage
    @Bean
    CommandLineRunner start(ProductRepository productrepository) {
        return args -> {
            //ajouter des produite
            productrepository.save(Product.builder()
                    .name("Computer")
                    .price(5400)
                    .quantity(12)
                    .build());

            productrepository.save(Product.builder()
                    .name("Printer")
                    .price(1200)
                    .quantity(11)
                    .build());

            productrepository.save(Product.builder()
                    .name("Smartphone")
                    .price(1200)
                    .quantity(33)
                    .build());

            productrepository.findAll().forEach(System.out::println);
        };
    }
}
