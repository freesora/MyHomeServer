package com.dochi.MyHomeServer.Domain;

public class ConfigProperty {

	String runningMin;
	String intervalMin;
	String wannaTemp;
	String highTemp;
	String lowTemp;
	public String getRunningMin() {
		return runningMin;
	}
	public void setRunningMin(String runningMin) {
		this.runningMin = runningMin;
	}
	public String getIntervalMin() {
		return intervalMin;
	}
	public void setIntervalMin(String intervalMin) {
		this.intervalMin = intervalMin;
	}
	public String getWannaTemp() {
		return wannaTemp;
	}
	public void setWannaTemp(String wannaTemp) {
		this.wannaTemp = wannaTemp;
	}
	public String getHighTemp() {
		return highTemp;
	}
	public void setHighTemp(String highTemp) {
		this.highTemp = highTemp;
	}
	public String getLowTemp() {
		return lowTemp;
	}
	public void setLowTemp(String lowTemp) {
		this.lowTemp = lowTemp;
	}
	@Override
	public String toString() {
		return "ConfigProperty [runningMin=" + runningMin + ", intervalMin=" + intervalMin + ", wannaTemp=" + wannaTemp
				+ ", highTemp=" + highTemp + ", lowTemp=" + lowTemp + "]";
	}
	
	
	
}
