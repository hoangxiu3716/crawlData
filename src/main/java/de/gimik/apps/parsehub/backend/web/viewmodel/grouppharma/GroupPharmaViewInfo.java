package de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma;

import java.util.Map;

import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;

public class GroupPharmaViewInfo extends GroupPharmaBasicInfo{
	
	private Map<Integer, Boolean> shopDetail;
	private Map<String, Boolean> shopUrls;
	public GroupPharmaViewInfo() {
		super();
	}
	public GroupPharmaViewInfo(GroupPharmaDetail item) {
		super(item);
		this.shopDetail = ParsehubSetting.createParsehubSettingMaps(item.getShops());
		this.shopUrls = ParsehubSetting.createParsehubSettingSelected(item.getShops());
	}
	public Map<Integer, Boolean> getShopDetail() {
		return shopDetail;
	}
	public void setShopDetail(Map<Integer, Boolean> shopDetail) {
		this.shopDetail = shopDetail;
	}
	public Map<String, Boolean> getShopUrls() {
		return shopUrls;
	}
	public void setShopUrls(Map<String, Boolean> shopUrls) {
		this.shopUrls = shopUrls;
	}

	
}
