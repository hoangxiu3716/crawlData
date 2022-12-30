package de.gimik.apps.parsehub.backend.web.viewmodel.parsehub;

import java.util.Date;

import de.gimik.apps.parsehub.backend.model.ParsehubDetail;

public class ParsehubDetailBasicInfo {
	private Integer id;
	private String name;
	private String pzn;
	private Date creationTime;
	private Integer sequenceByKeyword;
	private Double price;
	private String quantity;
	private String keyword;
	private Integer parsehubId;
	private boolean active;
	public ParsehubDetailBasicInfo() {
		super();
	}
	public ParsehubDetailBasicInfo(ParsehubDetail item) {
		super();
		this.id = item.getId();
		this.name = item.getName();
		this.pzn = item.getPzn();
		this.price = item.getPrice();
		this.sequenceByKeyword = item.getSequenceByKeyword();
		this.quantity = item.getQuantity();
		this.keyword = item.getKeyword();
		this.active = item.isActive();
		this.creationTime = item.getCreatedDate();
		this.parsehubId = item.getParsehubSetting() == null ? -1 : item.getParsehubSetting().getId();
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
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Integer getSequenceByKeyword() {
		return sequenceByKeyword;
	}
	public void setSequenceByKeyword(Integer sequenceByKeyword) {
		this.sequenceByKeyword = sequenceByKeyword;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getParsehubId() {
		return parsehubId;
	}
	public void setParsehubId(Integer parsehubId) {
		this.parsehubId = parsehubId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
}
