package com.example.bankTransfers.exception;

import java.time.LocalDateTime;
import com.example.bankTransfers.model.Account;
import com.example.bankTransfers.model.Transfer;

public class ApiResponse {

    private int status;
    private String path;
    private LocalDateTime timestamp;
	private Object data;

	public ApiResponse(int status, String path) {
		this.status = status;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}

//	public ApiResponse(int status, String path, Transfer data) {
//		this.status = status;
//		this.path = path;
//		this.timestamp = LocalDateTime.now();
//		this.data = data;
//	}
	
	public ApiResponse(int status, String path, Object data) {
		this.status = status;
		this.path = path;
		this.timestamp = LocalDateTime.now();
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ApiResponse() {
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	  
	  
}
