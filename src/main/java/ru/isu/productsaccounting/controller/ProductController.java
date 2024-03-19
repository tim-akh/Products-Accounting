package ru.isu.productsaccounting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.isu.productsaccounting.exception.InvalidFormation;
import ru.isu.productsaccounting.exception.InvalidReference;
import ru.isu.productsaccounting.model.Product;
import ru.isu.productsaccounting.model.User;
import ru.isu.productsaccounting.service.ProductService;
import ru.isu.productsaccounting.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/listProducts")
    public String showProducts(Model model) {
        model.addAttribute("listProducts", productService.getAllProducts());
        return "list_products";
    }

    @GetMapping("/add")
    public String addProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "add_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") @Valid Product product, Errors errors) throws InvalidFormation, InvalidReference {
        if (errors.hasErrors()) {
            return "add_product";
        }
        if (productService.findProductByName(product.getName()).isPresent()) {
            throw new InvalidFormation("Продукт с таким наименованием уже существует!");
        } else {
            productService.saveProduct(product);
        }
        return "redirect:/product/listProducts";
    }

    @GetMapping("/update/{id}")
    public String updateProduct(@PathVariable(value = "id") Product product, Model model) throws InvalidReference {
        if (product == null) {
            throw new InvalidReference("По указанному id продукт не найден!");
        } else {
            model.addAttribute("product", product);
        }
        return "update_product";
    }

    @PostMapping("/updateProduct")
    public String saveUpdatedProduct(@ModelAttribute("product") @Valid Product product, Errors errors) throws InvalidFormation {
        if (errors.hasErrors()) {
            return "add_product";
        }

        productService.saveProduct(product);
        return "redirect:/product/listProducts";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(value = "id") Product product) throws InvalidReference {
        if (product == null) {
            throw new InvalidReference("По указанному id продукт не найден!");
        } else {
            productService.deleteProduct(product);
        }
        return "redirect:/product/listProducts";
    }
}
