package com.example.BTL_IOT.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BTL_IOT.Model.Category;
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>{
	 @Query("SELECT c FROM Category c WHERE c.id != :id")
	  List<Category> findAllCategoriesExceptId(@Param("id") long id);
}
