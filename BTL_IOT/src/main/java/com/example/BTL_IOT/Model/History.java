package com.example.BTL_IOT.Model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "history")
public class History {
	@Id
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ac", referencedColumnName = "id")
	private Account account;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_card", referencedColumnName = "id")
	private Card card;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_device", referencedColumnName = "id")
	private Device device;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="time_in")
	private String time_in;
	
	public History() {
		super();
	}

	public History(long id, int acId, String cardId, long deviceId, Date date, String time_in) {
		super();
		this.id = id;
		this.account = new Account(acId);
		this.card = new Card(cardId);
		this.device = new Device(deviceId);
		this.date = date;
		this.time_in = time_in;
	}
	
	public History( int acId, String cardId, long deviceId, Date date, String time_in) {
		super();
		this.account = new Account(acId);
		this.card = new Card(cardId);
		this.device = new Device(deviceId);
		this.date = date;
		this.time_in = time_in;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime_in() {
		return time_in;
	}

	public void setTime_in(String time_in) {
		this.time_in = time_in;
	}

	@Override
	public String toString() {
		return "History [id=" + id + ", account=" + account + ", card=" + card + ", device=" + device + ", date=" + date
				+ ", time_in=" + time_in  + "]";
	}
		
}
