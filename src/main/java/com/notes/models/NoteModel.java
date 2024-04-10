package com.notes.models;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name ="note_data")
public class NoteModel extends BaseEntityModel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2536512788290727809L;
	
	public static final String DEFAULT_ORDER_BY = "created_at";

//	@Id
//	@UuidGenerator
//	@Column(name ="id")
//	private String id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name="content")
	private String content;
	
	@Column(name ="user_id")
	private String userId;	
		
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
