package ru.isu.productsaccounting.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.isu.productsaccounting.exception.InvalidFormation;
import ru.isu.productsaccounting.exception.InvalidReference;
import ru.isu.productsaccounting.model.User;
import ru.isu.productsaccounting.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/listUsers")
    public String showUsers(Model model) {
        model.addAttribute("listUsers", userService.getAllUsers());
        return "list_users";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "add_user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, Errors errors) throws InvalidFormation, InvalidReference {
        if (errors.hasErrors()) {
            return "add_user";
        }
        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            throw new InvalidFormation("Пользователь с таким наименованием уже существует!");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        }
        return "redirect:/user/listUsers";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable(value = "id") User user, Model model) throws InvalidReference {
        if (user == null) {
            throw new InvalidReference("По указанному id пользователь не найден!");
        } else {
            model.addAttribute("user", user);
        }
        return "update_user";
    }


    @PostMapping("/updateUser")
    public String saveUpdatedUser(@ModelAttribute("user") @Valid User user, Errors errors) throws InvalidFormation {
        if (errors.hasErrors()) {
            return "update_user";
        }
        userService.saveUser(user);
        return "redirect:/user/listUsers";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") User user) throws InvalidReference {
        if (user == null) {
            throw new InvalidReference("По указанному id пользователь не найден!");
        } else {
            userService.deleteUser(user);
        }
        return "redirect:/user/listUsers";
    }
}
