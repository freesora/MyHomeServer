package com.dochi.MyHomeServer.Domain;

public class TempInfo {

	
	public TempInfo(String currTmp, String setTemp) {
		super();
		this.currTmp = currTmp;
		this.setTemp = setTemp;
	}
	String currTmp;
	String setTemp;
	public String getCurrTmp() {
		return currTmp;
	}
	public void setCurrTmp(String currTmp) {
		this.currTmp = currTmp;
	}
	public String getSetTemp() {
		return setTemp;
	}
	public void setSetTemp(String setTemp) {
		this.setTemp = setTemp;
	}
}
