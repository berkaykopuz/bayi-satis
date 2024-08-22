package com.etiya.bayi_satis.dto;


import com.etiya.bayi_satis.entity.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BasketDto {
    @NotNull
    private long deviceId;
    @NotNull
    private String deviceName;
    @NotNull
    private double devicePrice;
    @NotNull
    private int deviceStock;
    @NotNull
    private int quantity;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

}
