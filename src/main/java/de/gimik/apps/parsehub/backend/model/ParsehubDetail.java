package de.gimik.apps.parsehub.backend.model;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import de.gimik.apps.parsehub.backend.util.TextUtility;
import de.gimik.apps.parsehub.backend.web.viewmodel.pushhub.PharmacyBasicData;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "parsehub_detail")
@EntityListeners(AuditingEntityListener.class)
public class ParsehubDetail extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@Column
	private String url;
	@Column
	private Date importDate;
	@Column
	private Integer sequenceByKeyword;
	@Column(length=1000)
	private String name;
	@Column
	private String pzn;
	@Column(columnDefinition="text")
	private String fullPzn;
	@Column
	private Double price;
	@Column
	private String fullPrice;
	@Column
	private String quantity;
	@Column
	private String lot;
	@Column
	private String clawerDate;
	@Column
	private String keyword;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "parsehubId")
	private ParsehubSetting parsehubSetting;
	@Column
	private Integer productId;
	public ParsehubDetail() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
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


	public String getClawerDate() {
		return clawerDate;
	}

	public void setClawerDate(String clawerDate) {
		this.clawerDate = clawerDate;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public ParsehubSetting getParsehubSetting() {
		return parsehubSetting;
	}

	public void setParsehubSetting(ParsehubSetting parsehubSetting) {
		this.parsehubSetting = parsehubSetting;
	}


	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public static ParsehubDetail buildData(ParsehubDetail data, PharmacyBasicData item, String key, String projectToken) {
		try {
			  //  Block of code to try
			
			data.setClawerDate(item.getDate());
			String name = item.getName();
			data.setName(name);
			data.setFullPrice(item.getPreis());
			String pzn = "";
			String fullPzn = StringUtils.isEmpty(item.getPzn()) ? (StringUtils.isEmpty(item.getPreis_url()) ? "" : item.getPreis_url()) : item.getPzn();
			data.setFullPzn(fullPzn);
			String quantity = "";
			if(!StringUtils.isEmpty(fullPzn)) {
				if(fullPzn.toUpperCase().indexOf("PZN") > -1) {
					if(fullPzn.toUpperCase().indexOf("(PZN)") > -1)
						pzn = fullPzn.substring(fullPzn.toUpperCase().indexOf("PZN")+6, fullPzn.length());
					else
						pzn = fullPzn.substring(fullPzn.toUpperCase().indexOf("PZN")+4, fullPzn.length());
					if(pzn.indexOf("\n") > -1)
						pzn = pzn.substring(0, pzn.indexOf("\n"));
				}
				if(StringUtils.isEmpty(pzn) && TextUtility.isNumeric(fullPzn)) {
						pzn = fullPzn;
				}
				if((projectToken.equals("tGDVm5wy5Y6Y") || projectToken.equals("t-9wTnkn9BU6") || projectToken.equals("tGDVm5wy5Y6Y") ) 
						&& fullPzn.indexOf("/") > -1 ) {
					pzn = fullPzn.substring(fullPzn.lastIndexOf("/")+1);
				}
				if(projectToken.equals("t6D7NTOYy1QR")  ) {
					if(fullPzn.indexOf("id=") > -1) {
						pzn = fullPzn.substring(fullPzn.indexOf("id=")+3);
						if(pzn.indexOf("&")> -1) pzn = pzn.substring(0, pzn.indexOf("&"));
					}else {
						if(fullPzn.indexOf("-") > -1) {
							pzn = fullPzn.substring(fullPzn.lastIndexOf("-")+1);
	//						if(!StringUtils.isEmpty(pzn) && pzn.length() < 8) { pzn = ("00000000" + pzn).substring(pzn.length());}
						}
					}
				}
				if((projectToken.equals("t_M_bnKRDX7M") || projectToken.equals("tkNdTuV8Bdhr")) && fullPzn.indexOf("-") > -1  ) {
					pzn = fullPzn.substring(fullPzn.lastIndexOf("-")+1);
				}
			}
			if(StringUtils.isEmpty(pzn)) {
				if(key.indexOf("_") > -1) {
					pzn = key.split("_")[1];
				}
			}	
			if(!StringUtils.isEmpty(pzn)) {
				if(pzn.indexOf("/") > -1)
					pzn = pzn.split("/")[0];
				pzn = pzn.replaceAll("[^0-9.]", "").trim();
			}
			if(!StringUtils.isEmpty(pzn) && pzn.length() < 8)  pzn = ("00000000" + pzn).substring(pzn.length());
			
			data.setPzn(pzn);
			if(!StringUtils.isEmpty(item.getMenge()))
				quantity = item.getMenge();
			else {
				if(!StringUtils.isEmpty(name)) {
					if(name.indexOf("(") > -1 && name.indexOf(")") > -1)
						quantity =  name.substring(name.indexOf("(")+1,name.indexOf(")"));
					else {
						if(!StringUtils.isEmpty(fullPzn)) {
							if(fullPzn.indexOf("\n")> -1 && !projectToken.equals("tFvUZVRHXNO2") && !projectToken.equals("tgCjdAFxoSkr")) {
								quantity =  fullPzn.substring(fullPzn.indexOf("\n"),fullPzn.length());
								if(!StringUtils.isEmpty(quantity) && quantity.indexOf(",") > -1)
									quantity = quantity.substring(quantity.indexOf(",")+1,quantity.length());
							}
						}
					}
				}
			}
			if((projectToken.equals("tkNdTuV8Bdhr") || projectToken.equals("t6D7NTOYy1QR")) && !StringUtils.isEmpty(name) && name.indexOf("," ) > -1) {
				quantity = name.split(",")[1];
			}
			if(!StringUtils.isEmpty(quantity))
				quantity = quantity.trim();	
			data.setQuantity(quantity);
			String fullPrice = item.getPreis();
			data.setFullPrice(fullPrice);
			Double price = null;
			if(!StringUtils.isEmpty(fullPrice)) {
				fullPrice = fullPrice.replace(".", "").replace(",", ".").replaceAll("[^0-9.]", "");
				if(!StringUtils.isEmpty(fullPrice)) price = Double.valueOf(fullPrice);
				data.setPrice(price);
			}
			data.setLot(item.getMenge());
		}
		catch(Exception e) {
		  //  Block of code to handle errors
		}
		return data;
	}
	
}
