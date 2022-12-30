package de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail;

import java.util.Date;

import de.gimik.apps.parsehub.backend.model.ParsehubDetail;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.util.Constants;

public class PharmacyCrawlBasicInfo {
	private Integer id;
	private String name;
	private String pzn;
	private String url;
	private Date creationTime;
	private Integer sequenceByKeyword;
	private String price;
	private String fullPrice;
	private String quantity;
	private String keyword;
	private Integer pharmaId;
	private String crawlType;
	private Double priceInDouble;
	private Integer discount;
	private boolean active;
	private Integer parsehubId;
	private String parsehubName;
	private String toolId;
	private Date importDate;
	private Double avp;
	public PharmacyCrawlBasicInfo() {
		super();
	}
	public PharmacyCrawlBasicInfo(PharmaDetail item) {
		super();
		this.id = item.getId();
		this.name = item.getName();
		this.pzn = item.getPzn();
		this.price = item.getPrice();
		this.fullPrice = item.getFullPrice();
		this.creationTime = item.getCreatedDate();
		this.sequenceByKeyword = item.getSequenceByKeyword();
		this.quantity = item.getQuantity();
		this.keyword = item.getPharmaSetting() == null ? "" : item.getPharmaSetting().getKeyword();
		this.pharmaId = item.getPharmaSetting() == null ? -1 : item.getPharmaSetting().getId();
		this.crawlType = item.getCrawlType();
		this.priceInDouble = item.getPriceInDouble();
		this.discount = item.getDiscount();
		this.active = item.isActive();
		this.toolId = Constants.TOOLID.PHARMA_TOOL;
		this.parsehubId = item.getParsehubSetting() == null ? -1 : item.getParsehubSetting().getId();
		this.parsehubName = item.getParsehubSetting() == null ? "" : item.getParsehubSetting().getUrl();
		this.importDate = item.getCreatedDate();
		this.avp = item.getAvp();
	}
	public PharmacyCrawlBasicInfo(ParsehubDetail item) {
		super();
		this.id = item.getId();
		this.name = item.getName();
		this.pzn = item.getPzn();
		this.price = item.getPrice() == null ? "" : item.getPrice().toString();
		this.fullPrice = item.getFullPrice();
		this.creationTime = item.getCreatedDate();
		this.sequenceByKeyword = item.getSequenceByKeyword();
		this.quantity = item.getQuantity();
		this.keyword = item.getKeyword();
//		this.pharmaId = item.getPharmaSetting() == null ? -1 : item.getPharmaSetting().getId();
//		this.crawlType = item.getCrawlType();
		this.priceInDouble = item.getPrice();
//		this.discount = item.getDiscount();
		this.active = item.isActive();
		this.parsehubId = item.getParsehubSetting() == null ? -1 : item.getParsehubSetting().getId();
		this.parsehubName = item.getParsehubSetting() == null ? "" : item.getParsehubSetting().getUrl();
		this.toolId = Constants.TOOLID.PARSEHUB_TOOL;
		this.importDate = item.getImportDate();
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Integer getSequenceByKeyword() {
		return sequenceByKeyword;
	}
	public void setSequenceByKeyword(Integer sequenceByKeyword) {
		this.sequenceByKeyword = sequenceByKeyword;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFullPrice() {
		return fullPrice;
	}
	public void setFullPrice(String fullPrice) {
		this.fullPrice = fullPrice;
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
	public Integer getPharmaId() {
		return pharmaId;
	}
	public void setPharmaId(Integer pharmaId) {
		this.pharmaId = pharmaId;
	}
	public String getCrawlType() {
		return crawlType;
	}
	public void setCrawlType(String crawlType) {
		this.crawlType = crawlType;
	}
	public Double getPriceInDouble() {
		return priceInDouble;
	}
	public void setPriceInDouble(Double priceInDouble) {
		this.priceInDouble = priceInDouble;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Integer getParsehubId() {
		return parsehubId;
	}
	public void setParsehubId(Integer parsehubId) {
		this.parsehubId = parsehubId;
	}
	public String getToolId() {
		return toolId;
	}
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	public String getParsehubName() {
		return parsehubName;
	}
	public void setParsehubName(String parsehubName) {
		this.parsehubName = parsehubName;
	}
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	public Double getAvp() {
		return avp;
	}
	public void setAvp(Double avp) {
		this.avp = avp;
	}

}
