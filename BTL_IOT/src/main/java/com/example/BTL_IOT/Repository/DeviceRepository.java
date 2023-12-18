package com.example.BTL_IOT.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BTL_IOT.Model.Device;
import com.example.BTL_IOT.Model.DeviceDto;
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, PagingAndSortingRepository<Device, Long>{

	Device save(Device device);

	boolean existsById(Long id);

	void deleteById(Long id);
	
	@Query("SELECT COUNT(d) FROM Device d WHERE d.id <= :deviceId")
    long getOrderNumberById(@Param("deviceId") long deviceId);
	
	ArrayList<Device> getDeviceByDay(Date day);
}
