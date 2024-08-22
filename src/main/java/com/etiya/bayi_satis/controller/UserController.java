package com.etiya.bayi_satis.controller;

import com.etiya.bayi_satis.entity.User;
import com.etiya.bayi_satis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Iterator;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-panel")
    public String getUsersPage(Model model){
        List<User> users = userService.getUsers();
        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()){
            User user = iterator.next();

            if(user.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getRolename()))) iterator.remove();
        }

        model.addAttribute("users", users);

        return "user-panel";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("userId") String userId,
                           Model model){
        boolean isDeleted = userService.deleteUser(userId);

        if(isDeleted){
            List<User> users = userService.getUsers();
            model.addAttribute("users", users);

            return "redirect:/user-panel";
        }else{
            return "fail"; //must be changed
        }
    }
}
