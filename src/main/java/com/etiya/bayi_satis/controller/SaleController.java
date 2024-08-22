package com.etiya.bayi_satis.controller;

import com.etiya.bayi_satis.dto.SaleDto;
import com.etiya.bayi_satis.service.SaleService;
import com.etiya.bayi_satis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpClient;
import java.security.Principal;
import java.util.List;

@Controller
public class SaleController {
    private final UserService userService;
    private final SaleService saleService;

    public SaleController(UserService userService, SaleService saleService) {
        this.userService = userService;
        this.saleService = saleService;
    }



    @GetMapping("/yourorder")
    public String getSales(Model model,
                           HttpClient.Redirect Attributes,
                           Principal principal){

        String username = principal.getName();

        if(username == null || username.equals("")){
            return "devices";
        }

        List<SaleDto> saleDtos = saleService.getSalesByUser(username);

        model.addAttribute("sales", saleDtos);



        return "yourorder";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam("saleId") Long id,
                              Model model,
                              HttpClient.Redirect Attributes,
                              Principal principal){
        boolean isCancelled = saleService.cancelSale(id);

        if(isCancelled){
            String username = principal.getName();
            List<SaleDto> saleDtos = saleService.getSalesByUser(username);

            model.addAttribute("sales", saleDtos);

            if(principal.getName() == null){
                return "devices";
            }

            return "yourorder";

        }else{
            return "fail";
        }
    }
}
