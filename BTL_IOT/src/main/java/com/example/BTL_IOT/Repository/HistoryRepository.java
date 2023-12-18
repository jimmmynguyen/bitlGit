package com.example.BTL_IOT.Repository;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BTL_IOT.Model.History;
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> ,PagingAndSortingRepository<History, Long>{
	public ArrayList<History> getHistoryByDate(Date date);
	@Query("SELECT h FROM History h WHERE YEAR(h.date) = :year AND MONTH(h.date) = :month AND h.card.id = :idCard")
	ArrayList<History> findAllHistoryByYearAndMonthAndIdCard(@Param("year") int year, @Param("month") int month, @Param("idCard") String idCard);


}
