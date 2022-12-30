package de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail;

public class PharmacyBasicData {
	private String name;
	private String pzn;
	private String preis;
	private String date;
	private String menge;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPzn() {
		return pzn;
	}
	public void setPzn(String pzn) {
		this.pzn = pzn;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPreis() {
		return preis;
	}
	public void setPreis(String preis) {
		this.preis = preis;
	}
	public String getMenge() {
		return menge;
	}
	public void setMenge(String menge) {
		this.menge = menge;
	}
	
}
