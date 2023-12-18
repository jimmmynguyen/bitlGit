package com.example.BTL_IOT.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BTL_IOT.Model.Card;
import com.example.BTL_IOT.Repository.CardRepository;

@Service
public class CardService {
	@Autowired
	private CardRepository cardRepository;
	
	public Optional<Card> getCardById(String id) {
		return cardRepository.findById(id);
	}
}
