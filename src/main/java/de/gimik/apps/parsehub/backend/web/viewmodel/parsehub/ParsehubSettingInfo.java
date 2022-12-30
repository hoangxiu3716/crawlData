package de.gimik.apps.parsehub.backend.web.viewmodel.parsehub;

import de.gimik.apps.parsehub.backend.model.ParsehubSetting;

public class ParsehubSettingInfo {
	private Integer id;
	private String name;
	private String apiKey;
	private String type;
	private String url;
	private boolean active;
	private String projectToken;
	public ParsehubSettingInfo() {
		super();
	}
	public ParsehubSettingInfo(ParsehubSetting item) {
		super();
		this.id = item.getId();
		this.name = item.getName();
		this.active = item.isActive();
		this.apiKey = item.getApiKey();
		this.type = item.getType();
		this.url = item.getUrl();
		this.projectToken = item.getProjectToken();
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
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getProjectToken() {
		return projectToken;
	}
	public void setProjectToken(String projectToken) {
		this.projectToken = projectToken;
	}
	
}
