package de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma;

import java.util.List;

import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;

public class GroupPharmaBasicInfo {
	private Integer id;
	private String name;
	private String type;
	private boolean active;
	private List<BasePharmaInfo> basePharma;
	private String shops;
	public GroupPharmaBasicInfo() {
		super();
	}
	public GroupPharmaBasicInfo(GroupPharmaDetail item) {
		super();
		this.id = item.getId();
		this.name = item.getName();
		this.active = item.isActive();
		this.type = item.getType();
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
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<BasePharmaInfo> getBasePharma() {
		return basePharma;
	}
	public void setBasePharma(List<BasePharmaInfo> basePharma) {
		this.basePharma = basePharma;
	}
	public String getShops() {
		return shops;
	}
	public void setShops(String shops) {
		this.shops = shops;
	}
	
	
}
