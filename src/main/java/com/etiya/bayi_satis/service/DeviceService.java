package com.etiya.bayi_satis.service;

import com.etiya.bayi_satis.dto.DeviceDto;
import com.etiya.bayi_satis.entity.Device;
import com.etiya.bayi_satis.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceDto> getAllDeviceDtos(){

        List<Device> devices = deviceRepository.findAll();

        return devices.stream().map(DeviceDto::convert).collect(Collectors.toList());

    }
    public List<Device> getAllDevices(){
        List<Device> devices = deviceRepository.findAll();

        return devices;
    }
    public DeviceDto getDevice(Long id){
        return DeviceDto.convert(deviceRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    public void saveDevice(Device device){
        deviceRepository.save(device);
    }

    public void updateDevice(Device device){
        deviceRepository.save(device);
    }
}
