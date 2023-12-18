package com.example.BTL_IOT.Service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BTL_IOT.Model.Category;
import com.example.BTL_IOT.Repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	
	public ArrayList<Category> getAllCategory(){
		return (ArrayList<Category>) categoryRepository.findAll();
	}
	
	public Optional<Category> getCategoryById(long id) {
		return categoryRepository.findById(id);
	}
	
	public ArrayList<Category> getAllBySort(long id){
		ArrayList<Category> list = new ArrayList<>();
		Optional<Category> optinalCategory = categoryRepository.findById(id);
		list.add(optinalCategory.get());
		ArrayList<Category> list1 = (ArrayList<Category>) categoryRepository.findAllCategoriesExceptId(id);
		list.addAll(list1);
		return list;
	}
}
