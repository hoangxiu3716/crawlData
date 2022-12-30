package de.gimik.apps.parsehub.backend.model;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;

@Entity
@Table(name = "pharma_setting")
@EntityListeners(AuditingEntityListener.class)
public class PharmaSetting extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@Column
	private String url;
	@Column
	private String name;
	@Column
	private String type;
	@Column
	private String keyword;
	@Column
	private String parsehubKeyword;
	@Column
	private Integer parentId;
	@Column
	private String code;
	@Column
	private String apothekeUrl;
	@Column
	private String juvalisUrl;
	@Column
	private String euraponUrl;
	@Column
	private String apodiscounterUrl;
	@Column
	private String medpexUrl;
	@Column
	private String medikamenteUrl;
	@Column
	private String docmorrisUrl;
	@Column
	private String aponeoUrl;
	@Column
	private String apotalUrl;
	@Column
	private String mycareUrl;
	@Column
	private String apiKey;
	@Column
	private String projectToken;
	public PharmaSetting() {
		super();
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getParsehubKeyword() {
		return parsehubKeyword;
	}
	public void setParsehubKeyword(String parsehubKeyword) {
		this.parsehubKeyword = parsehubKeyword;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getApothekeUrl() {
		return apothekeUrl;
	}
	public void setApothekeUrl(String apothekeUrl) {
		this.apothekeUrl = apothekeUrl;
	}
	public String getJuvalisUrl() {
		return juvalisUrl;
	}
	public void setJuvalisUrl(String juvalisUrl) {
		this.juvalisUrl = juvalisUrl;
	}
	public String getEuraponUrl() {
		return euraponUrl;
	}
	public void setEuraponUrl(String euraponUrl) {
		this.euraponUrl = euraponUrl;
	}
	public String getApodiscounterUrl() {
		return apodiscounterUrl;
	}
	public void setApodiscounterUrl(String apodiscounterUrl) {
		this.apodiscounterUrl = apodiscounterUrl;
	}
	public String getMedpexUrl() {
		return medpexUrl;
	}
	public void setMedpexUrl(String medpexUrl) {
		this.medpexUrl = medpexUrl;
	}
	public String getMedikamenteUrl() {
		return medikamenteUrl;
	}
	public void setMedikamenteUrl(String medikamenteUrl) {
		this.medikamenteUrl = medikamenteUrl;
	}
	public String getDocmorrisUrl() {
		return docmorrisUrl;
	}
	public void setDocmorrisUrl(String docmorrisUrl) {
		this.docmorrisUrl = docmorrisUrl;
	}
	public String getAponeoUrl() {
		return aponeoUrl;
	}
	public void setAponeoUrl(String aponeoUrl) {
		this.aponeoUrl = aponeoUrl;
	}
	public String getApotalUrl() {
		return apotalUrl;
	}
	public void setApotalUrl(String apotalUrl) {
		this.apotalUrl = apotalUrl;
	}
	public String getMycareUrl() {
		return mycareUrl;
	}
	public void setMycareUrl(String mycareUrl) {
		this.mycareUrl = mycareUrl;
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
