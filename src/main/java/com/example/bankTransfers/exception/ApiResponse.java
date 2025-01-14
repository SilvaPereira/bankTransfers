package com.example.bankTransfers.exception;

import java.time.LocalDateTime;

public class ApiResponse {

    private int status;
    private String path;
    private LocalDateTime timestamp;
    
	public ApiResponse(int status, String path) {
		this.status = status;
		this.path = path;
		this.timestamp = LocalDateTime.now();
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
