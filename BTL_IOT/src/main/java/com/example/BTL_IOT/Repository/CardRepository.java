package com.example.BTL_IOT.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.BTL_IOT.Model.Card;
@Repository
public interface CardRepository extends CrudRepository<Card, String>{

}
