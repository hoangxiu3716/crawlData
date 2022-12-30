package de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail;

import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.util.DateTimeUtility;

public class PharmaProductBasicInfo {
	private Integer id;
	private String name;
	private String pzn;
	private String creationTime;
	private boolean active;
	public PharmaProductBasicInfo() {
		super();
	}
	public PharmaProductBasicInfo(PharmaProduct item) {
		super();
		this.id = item.getId();
		this.name = item.getName();
		this.pzn = item.getPzn();
		this.creationTime = DateTimeUtility.formatInputDate(item.getCreatedDate());
		this.active = item.isActive();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
