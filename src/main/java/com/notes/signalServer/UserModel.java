package com.notes.signalServer;

import java.util.LinkedHashMap;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

	private String userName; 
	
	private LinkedHashMap<String, Object> offer;
	
	private String password;
	
	private LinkedHashMap<String, Object> iceCandidate;
	
	private String answererUserName;
	
	private LinkedHashMap<String, Object> answer;
	
	private LinkedHashMap<String, Object> answererIceCandidates;

	private boolean didIOffer;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LinkedHashMap<String, Object> getIceCandidate() {
		return iceCandidate;
	}

	public void setIceCandidate(LinkedHashMap<String, Object> iceCandidate) {
		this.iceCandidate = iceCandidate;
	}

	public boolean isDidIOffer() {
		return didIOffer;
	}

	public void setDidIOffer(boolean didIOffer) {
		this.didIOffer = didIOffer;
	}

	public LinkedHashMap<String, Object> getOffer() {
		return offer;
	}

	public void setOffer(LinkedHashMap<String, Object> offer) {
		this.offer = offer;
	}

	public String getAnswererUserName() {
		return answererUserName;
	}

	public void setAnswererUserName(String answererUserName) {
		this.answererUserName = answererUserName;
	}

	public LinkedHashMap<String, Object> getAnswer() {
		return answer;
	}

	public void setAnswer(LinkedHashMap<String, Object> answer) {
		this.answer = answer;
	}

	public LinkedHashMap<String, Object> getAnswererIceCandidates() {
		return answererIceCandidates;
	}

	public void setAnswererIceCandidates(LinkedHashMap<String, Object> answererIceCandidates) {
		this.answererIceCandidates = answererIceCandidates;
	}

	
}
