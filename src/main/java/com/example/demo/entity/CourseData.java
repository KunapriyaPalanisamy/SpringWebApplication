package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

public class CourseData {

	public CourseData() {

	}
	
	private MultipartFile file;
	private String data;
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "CourseData [file=" + file + ", data=" + data + "]";
	}
	public CourseData(MultipartFile file, String data) {
		super();
		this.file = file;
		this.data = data;
	}
}
