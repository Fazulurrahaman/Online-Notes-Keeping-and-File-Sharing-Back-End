package com.notes.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityModel implements Serializable, Cloneable{

	/**
	 * Base class for all the model to extend,which requires id,createAt,updatedAt in the table were the entity is mapped.
	 */
	private static final long serialVersionUID = 5175418784424889917L;
	
	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false)
	//@Convert(converter = UUIDConvertion.class)
	private String id;
	
	@CreatedDate
	@Column(name="created_at", updatable = false)
	@JsonIgnore
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	@CreatedBy
	@Column(name="created_by",  updatable = false)
	private String createdBy;
	
	@LastModifiedBy
	@Column(name="updated_by")
	private String updatedBy;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
	public BaseEntityModel() {
		
	}
	
	public BaseEntityModel(String id, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "BaseEntityModel [id=" + id + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	

}
