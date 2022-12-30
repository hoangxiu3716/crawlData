package de.gimik.apps.parsehub.backend.web.viewmodel.forebet;

public class PredictionTodayInfo {
	private String country;
	private String dateTime;
	private String encounter;
	private String real1;
	private String real2;
	private String real3;
	private String tip;
	private String forecast;
	private String avgGoals;
	
	public PredictionTodayInfo() {
		super();
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getEncounter() {
		return encounter;
	}
	public void setEncounter(String encounter) {
		this.encounter = encounter;
	}
	public String getReal1() {
		return real1;
	}
	public void setReal1(String real1) {
		this.real1 = real1;
	}
	public String getReal2() {
		return real2;
	}
	public void setReal2(String real2) {
		this.real2 = real2;
	}
	public String getReal3() {
		return real3;
	}
	public void setReal3(String real3) {
		this.real3 = real3;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getForecast() {
		return forecast;
	}
	public void setForecast(String forecast) {
		this.forecast = forecast;
	}
	public String getAvgGoals() {
		return avgGoals;
	}
	public void setAvgGoals(String avgGoals) {
		this.avgGoals = avgGoals;
	}
	
}
