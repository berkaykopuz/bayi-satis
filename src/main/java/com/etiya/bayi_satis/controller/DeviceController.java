package com.etiya.bayi_satis.controller;

import com.etiya.bayi_satis.dto.BasketDto;
import com.etiya.bayi_satis.dto.DeviceDto;
import com.etiya.bayi_satis.entity.Device;
import com.etiya.bayi_satis.entity.PaymentMethod;
import com.etiya.bayi_satis.service.DeviceService;
import com.etiya.bayi_satis.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;
import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
public class DeviceController {
    private final DeviceService deviceService;
    private final SaleService saleService;

    public DeviceController(DeviceService deviceService, SaleService saleService) {
        this.deviceService = deviceService;
        this.saleService = saleService;
    }

    @GetMapping("/")
    public String getDefaultPage(){
        return "home";
    }

    @GetMapping("/devices")
    public String getDevicesPage(Model model,
                                 HttpClient.Redirect Attributes){
        List<DeviceDto> deviceDtos = deviceService.getAllDeviceDtos();
        Iterator<DeviceDto> iterator = deviceDtos.iterator();

        while (iterator.hasNext()) {
            DeviceDto deviceDto = iterator.next();

            if(deviceDto.stock() <= 0) iterator.remove();
        }

        model.addAttribute("devices", deviceDtos);

        return "devices";
    }

    @PostMapping("/basket")
    public String getBasket(
            @RequestParam("deviceId") String deviceId,
            @RequestParam("deviceName") String deviceName,
            @RequestParam("devicePrice") String devicePrice,
            @RequestParam("deviceStock") String deviceStock,
            Model model) {

        BasketDto basketDto = new BasketDto();
        basketDto.setDeviceId(Long.parseLong(deviceId));
        basketDto.setDeviceName(deviceName);
        basketDto.setDevicePrice(Double.parseDouble(devicePrice));
        basketDto.setDeviceStock(Integer.parseInt(deviceStock));

        List<PaymentMethod> paymentMethods = Arrays.asList(PaymentMethod.values());
        model.addAttribute("paymentMethods", paymentMethods);

        model.addAttribute("basketDto", basketDto);
        return "basket";
    }

    @PostMapping("/basket/order")
    public String orderDevice(@Valid @ModelAttribute("basketDto") BasketDto basketDto,
                              BindingResult result,
                              Model model,
                              Principal principal){
        if(result.hasErrors()){
            model.addAttribute("basketDto", basketDto);
        }

        if(principal == null){
            return "devices";
        }

        String username = principal.getName();
        saleService.saveSale(basketDto, username);

        return "success";
    }

    @GetMapping("/device-upload")
    public String getDeviceUpload(Model model){
        Device device = new Device();
        model.addAttribute("device", device);
        return "device-upload";
    }
    @PostMapping("/device/upload")
    public String getDeviceUpload(@ModelAttribute("device") Device device, BindingResult result, Model model){
        deviceService.saveDevice(device);
        return "redirect:/devices";
    }

    @GetMapping("/device-panel")
    public String getDevicePanel(Model model){
        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        return "device-panel";
    }

    @PostMapping("/device/update")
    public String uploadDevice(@ModelAttribute("devices") Device device, Model model){
        deviceService.updateDevice(device);

        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        return "redirect:/device-panel";
    }
}
