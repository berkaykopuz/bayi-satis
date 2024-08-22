package com.etiya.bayi_satis.controller;

import com.etiya.bayi_satis.dto.RegistrationDto;
import com.etiya.bayi_satis.entity.User;
import com.etiya.bayi_satis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.net.http.HttpClient;
import java.security.Principal;
import java.util.Optional;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDto user,
                           BindingResult result, Model model) {

        Optional<User> existingUserEmail = userService.findByEmail(user.getEmail());
        if(existingUserEmail.isPresent() &&
                existingUserEmail.get().getEmail() != null &&
                !existingUserEmail.get().getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }

        Optional<User> existingUserUsername = userService.findByUsername(user.getUsername());
        if(existingUserUsername.isPresent() &&
                existingUserUsername.get().getUsername() != null &&
                !existingUserUsername.get().getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }

        if(!user.getPassword().equals(user.getConfirmPassword())){
            return "redirect:/register?fail"; //password fail message should be thrown
        }

        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        userService.saveUser(user);

        return "redirect:/login";
    }

    @GetMapping("/account")
    public String getAccount(Model model,
                             Principal principal
                             ){

        String username = principal.getName();
        Optional<User> user = userService.findByUsername(principal.getName());

        if(user.isPresent()){
            model.addAttribute("user", user.get());
            return "account";
        }else{
            throw new RuntimeException();
        }
    }
    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute("user") User user, Principal principal){

        Optional<User> existingUserEmail = userService.findByEmail(user.getEmail());
        if(existingUserEmail.isPresent() &&
                existingUserEmail.get().getEmail() != null &&
                !existingUserEmail.get().getEmail().isEmpty()
        ){
            if(existingUserEmail.get().getEmail().equals(user.getEmail().toString())) {
                return "update-already-fail";
            }
        }

        Optional<User> existingUserUsername = userService.findByUsername(user.getUsername());
        if(existingUserUsername.isPresent() &&
                existingUserUsername.get().getUsername() != null &&
                !existingUserUsername.get().getUsername().isEmpty()) {
            if(existingUserUsername.get().getUsername().equals(user.getUsername().toString())) {
                return "update-already-fail";
            }
        }

        userService.updateAccount(user);

        return "redirect:/logout";
    }
}
