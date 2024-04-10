package com.notes.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotesResponse implements Serializable {

	private static final long serialVersionUID = 2075650774658379385L;
	
	/** The time stamp value **/
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime timestamp;
	
	/** The status code. */
	private int code;
	
	/** The status. */
	private String status;
	
	/** The status. */
	private String message;
	
	/** The data. */
	private Object data;
	
	
	/** additional info map. */
	private Map<String, Object> additionalInfoMap;
	

	public NotesResponse() {
       timestamp = LocalDateTime.now();
    }
	
	public NotesResponse(int code, String status, String message) {
		timestamp = LocalDateTime.now();
		this.code = code;
		this.status = status;
		this.message = message;
	}
	public NotesResponse(int code, String status, String message, Object object) {
		timestamp = LocalDateTime.now();
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = object;
	}



	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getAdditionalInfoMap() {
		return additionalInfoMap;
	}

	public void setAdditionalInfoMap(Map<String, Object> additionalInfoMap) {
		this.additionalInfoMap = additionalInfoMap;
	}

		
}

