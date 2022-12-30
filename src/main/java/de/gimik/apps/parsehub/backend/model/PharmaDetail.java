package de.gimik.apps.parsehub.backend.model;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import de.gimik.apps.parsehub.backend.util.DateTimeSerializer;

@Entity
@Table(name = "pharma_detail")
@EntityListeners(AuditingEntityListener.class)
public class PharmaDetail extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@Column
	private String url;
	@Column
	private Integer sequenceByKeyword;
	@Column(length=1000)
	private String name;
	@Column
	private String pzn;
	@Column(columnDefinition="text")
	private String fullPzn;
	@Column
	private String price;
	@Column
	private Double priceInDouble;
	@Column
	private String fullPrice;
	@Column
	private String quantity;
	@Column
	private String lot;
	@Column
	private String crawlType;
	@Column
	private String keyword;
	@Column
	private Integer productId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "pharmaId")
	private PharmaSetting pharmaSetting;
	@Column
	private Integer discount;
	@Column
	private Double avp;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "parsehubId")
	private ParsehubSetting parsehubSetting;
	
	public PharmaDetail() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSequenceByKeyword() {
		return sequenceByKeyword;
	}

	public void setSequenceByKeyword(Integer sequenceByKeyword) {
		this.sequenceByKeyword = sequenceByKeyword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getFullPrice() {
		return fullPrice;
	}

	public void setFullPrice(String fullPrice) {
		this.fullPrice = fullPrice;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public PharmaSetting getPharmaSetting() {
		return pharmaSetting;
	}

	public void setPharmaSetting(PharmaSetting pharmaSetting) {
		this.pharmaSetting = pharmaSetting;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String avpSon1) {
		this.price = avpSon1;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getPzn() {
		return pzn;
	}

	public void setPzn(String pzn) {
		this.pzn = pzn;
	}

	public String getFullPzn() {
		return fullPzn;
	}

	public void setFullPzn(String fullPzn) {
		this.fullPzn = fullPzn;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Double getAvp() {
		return avp;
	}

	public void setAvp(Double avp) {
		this.avp = avp;
	}

	public ParsehubSetting getParsehubSetting() {
		return parsehubSetting;
	}

	public void setParsehubSetting(ParsehubSetting parsehubSetting) {
		this.parsehubSetting = parsehubSetting;
	}

//	public static PharmaDetail buildData(PharmaDetail data, PharmacyBasicData item, String key, String projectToken) {
//		data.setClawerDate(item.getDate());
//		String name = item.getName();
//		data.setName(name);
//		data.setFullPrice(item.getPreis());
//		String pzn = "";
//		String fullPzn = item.getPzn();
//		data.setFullPzn(fullPzn);
//		String quantity = "";
//		if(!StringUtils.isEmpty(fullPzn)) {
//			if(fullPzn.toUpperCase().indexOf("PZN") > -1) {
//				pzn = fullPzn.substring(fullPzn.toUpperCase().indexOf("PZN")+4, fullPzn.length());
//				if(pzn.indexOf("\n") > -1)
//					pzn = pzn.substring(0, pzn.indexOf("\n"));
//			}
//			if(StringUtils.isEmpty(pzn) && TextUtility.isNumeric(fullPzn)) {
//					pzn = fullPzn;
//			}
//		}
//		if(StringUtils.isEmpty(pzn)) {
//			if(key.indexOf("_") > -1) {
//				pzn = key.split("_")[1];
//			}
//		}	
//		if(!StringUtils.isEmpty(pzn)) {
//			if(pzn.indexOf("/") > -1)
//				pzn = pzn.split("/")[0];
//			pzn = pzn.replaceAll("[^0-9.]", "").trim();
//		}
//		data.setPzn(pzn);
//		if(!StringUtils.isEmpty(item.getMenge()))
//			quantity = item.getMenge();
//		else {
//			if(!StringUtils.isEmpty(name)) {
//				if(name.indexOf("(") > -1 && name.indexOf(")") > -1)
//					quantity =  name.substring(name.indexOf("(")+1,name.indexOf(")"));
//				else {
//					if(!StringUtils.isEmpty(fullPzn)) {
//						if(fullPzn.indexOf("\n")> -1 && !projectToken.equals("tFvUZVRHXNO2") && !projectToken.equals("tgCjdAFxoSkr")) {
//							quantity =  fullPzn.substring(fullPzn.indexOf("\n"),fullPzn.length());
//							if(!StringUtils.isEmpty(quantity) && quantity.indexOf(",") > -1)
//								quantity = quantity.substring(quantity.indexOf(",")+1,quantity.length());
//						}
//					}
//				}
//			}
//		}
//		if((projectToken.equals("tkNdTuV8Bdhr") || projectToken.equals("t6D7NTOYy1QR")) && !StringUtils.isEmpty(name) && name.indexOf("," ) > -1) {
//			quantity = name.split(",")[1];
//		}
//		if(!StringUtils.isEmpty(quantity))
//			quantity = quantity.trim();	
//		data.setQuantity(quantity);
//		String fullPrice = item.getPreis();
//		data.setFullPrice(fullPrice);
//		Double price = null;
//		if(!StringUtils.isEmpty(fullPrice)) {
//			fullPrice = fullPrice.replace(".", "").replace(",", ".").replaceAll("[^0-9.]", "");
//			price = Double.valueOf(fullPrice);
//			data.setPrice(price);
//		}
//		data.setLot(item.getMenge());
//		return data;
//	}
	
}
