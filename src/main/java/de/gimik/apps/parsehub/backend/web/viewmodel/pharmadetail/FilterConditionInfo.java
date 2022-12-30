package de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class FilterConditionInfo {
	private Date fromTime;
	private Date toTime;
	private Integer groupId;
	private List<String> pzns;
	private List<String> types;
	private Double fromPrice;
	private Double toPrice;
	private String pharmaKeyword;
	private String parsehubKeyword;
	private String keywordName;
	private Integer shopId;
	private String toolId;
	private String typeOfPharma;
	private Integer pharmaId;
	private List<Integer> listShopId;
	private List<String> listToolId;
	private String shopUrl;
	private List<String> shopUrls;
	private Integer childrenId;
	private Integer parentId;
	public FilterConditionInfo() {
		super();
	}
	public Date getFromTime() {
		return fromTime;
	}
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}
	public Date getToTime() {
		return toTime;
	}
	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public List<String> getPzns() {
		return pzns;
	}
	public void setPzns(List<String> pzns) {
		this.pzns = pzns;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
	public Double getFromPrice() {
		return fromPrice;
	}
	public void setFromPrice(Double fromPrice) {
		this.fromPrice = fromPrice;
	}
	public Double getToPrice() {
		return toPrice;
	}
	public void setToPrice(Double toPrice) {
		this.toPrice = toPrice;
	}
	public String getPharmaKeyword() {
		return pharmaKeyword;
	}
	public void setPharmaKeyword(String pharmaKeyword) {
		this.pharmaKeyword = pharmaKeyword;
	}
	public String getParsehubKeyword() {
		return parsehubKeyword;
	}
	public void setParsehubKeyword(String parsehubKeyword) {
		this.parsehubKeyword = parsehubKeyword;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public String getToolId() {
		return toolId;
	}
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	public String getTypeOfPharma() {
		return typeOfPharma;
	}
	public void setTypeOfPharma(String typeOfPharma) {
		this.typeOfPharma = typeOfPharma;
	}
	public Integer getPharmaId() {
		return pharmaId;
	}
	public void setPharmaId(Integer pharmaId) {
		this.pharmaId = pharmaId;
	}
	public List<Integer> getListShopId() {
		return listShopId;
	}
	public void setListShopId(List<Integer> listShopId) {
		this.listShopId = listShopId;
	}
	public List<String> getListToolId() {
		return listToolId;
	}
	public void setListToolId(List<String> listToolId) {
		this.listToolId = listToolId;
	}
	public String getShopUrl() {
		return shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
	public List<String> getShopUrls() {
		return shopUrls;
	}
	public void setShopUrls(List<String> shopUrls) {
		this.shopUrls = shopUrls;
	}
	public String getKeywordName() {
		return keywordName;
	}
	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}
	public Integer getChildrenId() {
		return childrenId;
	}
	public void setChildrenId(Integer childrenId) {
		this.childrenId = childrenId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
}
