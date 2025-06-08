package net.ourahma.bdccfsspringmvc.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.ourahma.bdccfsspringmvc.entities.Product;
import net.ourahma.bdccfsspringmvc.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller @AllArgsConstructor
public class ProductController {
    private ProductRepository productRepository;


    @GetMapping("/user/index")
    public String index(Model model){
        List<Product> products = productRepository.findAll();
        model.addAttribute("productsList", products);
        return "products";
    }
    // la page par défaut
    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }

    // la page d'ajout des nouveaus produite
    @GetMapping("/admin/newProduct")
    public String newProduct(Model model){
        model.addAttribute("product", new Product());
        return "new-product";
    }

    // sauvegarder un produit
    /**
     * @Valid va vérifier la validation des données
     * Les résultat sont stocké dans un objet BindingResult
     * lorsque on saisit les données elles sont stockées automatiquement dans le modèle donc il faut le déclarer
     * ***/
    @PostMapping("/admin/saveProduct")
    public String saveProduct(@Valid Product product, BindingResult bindingResult, Model model){
        System.out.println("save product"+product);
        if(bindingResult.hasErrors()){
            return "new-product";
        }else{
            productRepository.save(product);
            return "redirect:/admin/newProduct";
        }

    }
    // supprimer des produits
    @GetMapping("/admin/delete")
    public String delete(@RequestParam(name="id") Long id){
        productRepository.deleteById(id);
        return "redirect:/index";
    }
}
