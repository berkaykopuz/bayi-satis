package com.etiya.bayi_satis.dto;

import com.etiya.bayi_satis.entity.Device;
import com.etiya.bayi_satis.entity.PaymentMethod;
import com.etiya.bayi_satis.entity.Sale;
import com.etiya.bayi_satis.entity.User;

public record SaleDto(
        Long id,
        User user,
        int quantity,
        Device device,
        PaymentMethod paymentMethod
) {
    public static SaleDto convert(Sale from){
        return new SaleDto(from.getId(), from.getUser(), from.getQuantity(), from.getDevice(), from.getPaymentMethod());
    }
}
