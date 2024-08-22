package com.etiya.bayi_satis.dto;

import com.etiya.bayi_satis.entity.Device;

public record DeviceDto(Long id,
                        String name,
                        String description,
                        double price,
                        String imageUrl,
                        int stock) {
    public static DeviceDto convert(Device from){
        return new DeviceDto(from.getId(), from.getName(), from.getDescription(), from.getPrice(), from.getImageUrl(),
                from.getStock());
    }
}
