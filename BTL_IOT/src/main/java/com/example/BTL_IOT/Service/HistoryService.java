package com.example.BTL_IOT.Service;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.BTL_IOT.Model.Device;
import com.example.BTL_IOT.Model.DeviceDto;
import com.example.BTL_IOT.Model.History;
import com.example.BTL_IOT.Model.HistoryDto;
import com.example.BTL_IOT.Repository.HistoryRepository;

@Service
public class HistoryService {
	@Autowired
	private HistoryRepository historyRepository;
	
	public Page<HistoryDto> getAllPageHistory(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        Page<History> historyPage = historyRepository.findAll(pageable);
        
        // Chuyển đổi Page<Device> thành Page<DeviceDto>
        Page<HistoryDto> historyDtoPage = historyPage.map(history -> 
            new HistoryDto(history.getId(), history.getAccount().getEmployee().getName(), 
            		history.getDevice().getId(), history.getCard().getId(), 
            		history.getAccount().getEmployee().getDepartment(), 
            		history.getDate(),history.getTime_in()));
        
        return historyDtoPage;
    }
	
	public ArrayList<HistoryDto> getAllHistory(){
		ArrayList<History> list = (ArrayList<History>) historyRepository.findAll();
		ArrayList<HistoryDto> historyDtoList = new ArrayList<>();
		for (History history : list) {
	        HistoryDto historyDto = new HistoryDto();
	        historyDto.setId(history.getId());
	        historyDto.seteName(history.getAccount().getEmployee().getName());
	        historyDto.setDeviceId(history.getDevice().getId());;
	        historyDto.setCardId(history.getCard().getId());;
	        historyDto.setDepartment(history.getAccount().getEmployee().getDepartment());
	        historyDto.setDate(history.getDate());
	        historyDto.setTime_in(history.getTime_in());
	        // Sao chép các giá trị từ History sang HistoryDto

	        historyDtoList.add(historyDto);
	    }
		return historyDtoList;
	}
	
	public History saveHistory(History history) {
		return historyRepository.save(history);
	}
	
	public ArrayList<History> getAllHistoryByDate(Date date){
		return historyRepository.getHistoryByDate(date);
	}
	
	public ArrayList<History> findAllHistoryByYearAndMonth(int year,int month,String card){
		return historyRepository.findAllHistoryByYearAndMonthAndIdCard(year, month, card);
	}
}
