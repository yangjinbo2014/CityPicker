package com.yang.picker.address;

import java.util.ArrayList;

public class City {
	private String areaId;
	private String areaName;
	private ArrayList<County> counties = new ArrayList<County>();
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public ArrayList<County> getCounties() {
		return counties;
	}
	public void setCounties(ArrayList<County> counties) {
		this.counties = counties;
	}
	
}
