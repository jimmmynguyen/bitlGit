package com.example.BTL_IOT.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.BTL_IOT.Model.Device;
import com.example.BTL_IOT.Model.DeviceDto;
import com.example.BTL_IOT.Repository.DeviceRepository;
@Service
public class DeviceService {
	@Autowired
	private DeviceRepository deviceRepository;
	
	public long getQuantityDevice() {
		return deviceRepository.count();
	}
	
	public Page<Device> getAllDevices(int pageNumber) {
    	Pageable pageable = PageRequest.of(pageNumber-1, 8);
        return  deviceRepository.findAll(pageable);
    }
	
	public Page<DeviceDto> getAllDeviceforManage(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        Page<Device> devicePage = deviceRepository.findAll(pageable);
        
        // Chuyển đổi Page<Device> thành Page<DeviceDto>
        Page<DeviceDto> deviceDtoPage = devicePage.map(device -> 
            new DeviceDto(device.getId(), device.getName(), device.getSerial(), device.getImage(), device.getDay(), device.getCategory().getName()));
        
        return deviceDtoPage;
    }
	
	public Optional<Device> getDeviceById(long id){
		return deviceRepository.findById(id);
	}
	
	public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Device updateDevice(Long id, Device updatedDevice) {
        if (deviceRepository.existsById(id)) {
            updatedDevice.setId(id);
            return deviceRepository.save(updatedDevice);
        }
        return null; // Trả về null nếu không tìm thấy đối tượng cần sửa
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
    
    public long getIndexOfDeviceById(long id) {
    	return deviceRepository.getOrderNumberById(id);
    }
    
    public ArrayList<Device> getDeviceByDayUsed(Date day){
    	return deviceRepository.getDeviceByDay(day);
    }
}
