package net.ourahma.bdccfsspringmvc.repository;

import net.ourahma.bdccfsspringmvc.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
