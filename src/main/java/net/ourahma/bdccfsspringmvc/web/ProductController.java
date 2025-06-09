package net.ourahma.bdccfsspringmvc.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.ourahma.bdccfsspringmvc.entities.Product;
import net.ourahma.bdccfsspringmvc.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller @AllArgsConstructor
public class ProductController {
    private ProductRepository productRepository;


    @GetMapping("/user/index")
    @PreAuthorize("hasRole('USER')")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "0")int page,
                        @RequestParam(name="size",defaultValue = "5")int size,
                        @RequestParam(name="keyword",defaultValue = "")String keyword){
        Page<Product> pageProduct = productRepository.findByNameContains(keyword, PageRequest.of(page,size));

        //List<Product> products = productRepository.findAll();
        model.addAttribute("productsList", pageProduct.getContent());
        model.addAttribute("pages",new int[pageProduct.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "products";
    }
    // la page par défaut
    @GetMapping("/")
    public String home(@RequestParam(name="page",defaultValue = "0")int page,
                       @RequestParam(name="size",defaultValue = "5")int size,
                       @RequestParam(name="keyword",defaultValue = "")String keyword){
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    // la page d'ajout des nouveaus produite
    @GetMapping("/admin/newProduct")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public String saveProduct(@Valid Product product, BindingResult bindingResult, Model model,
                              @RequestParam(name = "keyword", defaultValue = "") String keyword,
                              @RequestParam(name = "page", defaultValue = "0") int page){
        System.out.println("save product"+product);
        if(bindingResult.hasErrors()){
            return "new-product";
        }else{
            productRepository.save(product);
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
            return "redirect:/admin/newProduct";
        }

    }
    // supprimer des produits
    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@RequestParam(name="id") Long id,
                         @RequestParam(name="page",defaultValue = "0")int page,
                         @RequestParam(name="size",defaultValue = "5")int size,
                         @RequestParam(name="keyword",defaultValue = "")String keyword){
        productRepository.deleteById(id);
        return "redirect:/user/index?page=" +page+ "&keyword="+keyword;
    }

    // la page d'erreur
    @GetMapping("/notAuthorized")
    public String notAuthorized(){
        return "notAuthorized";
    }
    // la page login personnélisée
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // la fontionnalité logout
    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:login";
    }

    //retoutner la page de modification
    @GetMapping("/admin/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProduct(Model model, Long id, String keyword, int page){
        Product product = productRepository.findById(id).orElse(null);
        if(product == null)throw new RuntimeException("Product introuvable");
        model.addAttribute("product", product);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editProduct";
    }

}
