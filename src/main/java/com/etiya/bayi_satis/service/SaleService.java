package com.etiya.bayi_satis.service;

import com.etiya.bayi_satis.dto.BasketDto;
import com.etiya.bayi_satis.dto.DeviceDto;
import com.etiya.bayi_satis.dto.SaleDto;
import com.etiya.bayi_satis.entity.Device;
import com.etiya.bayi_satis.entity.Sale;
import com.etiya.bayi_satis.entity.User;
import com.etiya.bayi_satis.repository.DeviceRepository;
import com.etiya.bayi_satis.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final DeviceRepository deviceRepository;
    private final UserService userService;
    private final DeviceService deviceService;

    public SaleService(SaleRepository saleRepository, DeviceRepository deviceRepository, UserService userService, DeviceService deviceService) {
        this.saleRepository = saleRepository;
        this.deviceRepository = deviceRepository;
        this.userService = userService;
        this.deviceService = deviceService;
    }

    public void saveSale(BasketDto basketDto, String username){
        Optional<User> user = userService.findByUsername(username);
        if(!user.isPresent()){
            throw new RuntimeException("User is not found with username:" + username);
        }

        DeviceDto deviceDto = deviceService.getDevice(basketDto.getDeviceId());
        Device device = deviceRepository.findById(basketDto.getDeviceId())
                .orElseThrow(RuntimeException::new);

        if(basketDto.getQuantity() > deviceDto.stock()){
            throw new RuntimeException();
        }else{
            device.setStock(deviceDto.stock() - basketDto.getQuantity());
            deviceService.updateDevice(device);
        }

        Sale sale = new Sale();
        sale.setQuantity(basketDto.getQuantity());
        sale.setUser(user.get());
        sale.setDevice(device);
        sale.setPaymentMethod(basketDto.getPaymentMethod());

        saleRepository.save(sale);
    }

    public List<SaleDto> getSalesByUser(String username){
        Optional<User> user = userService.findByUsername(username);

        if(user.isPresent()){
           List<Sale> sales = saleRepository.findByUser(user.get());
           return sales.stream().map(SaleDto::convert).collect(Collectors.toList());
        }
        else{
            throw new RuntimeException();
        }
    }

    public boolean cancelSale(Long id){
        Optional<Sale> sale = saleRepository.findById(id);

        if(sale.isPresent()){
            Device device = sale.get().getDevice();
            device.setStock(device.getStock() + sale.get().getQuantity());
            deviceService.updateDevice(device);

            saleRepository.deleteById(id);
            return true;
        }
        else{
            throw new RuntimeException("Sale not found with id: " + id);
        }

    }
}
