package de.gimik.apps.parsehub.backend.model;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;

@Entity
@Table(name = "pharma_screen_shot")
@EntityListeners(AuditingEntityListener.class)
public class PharmaScreenShot extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@Column
	private String url;
	@Column
	private String name;
	@Column
	private String type;
	@Column
	private String projectToken;
	@Column
	private String apiKey;
	@Column
	private String keyword;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "pharmaId")
	private PharmaSetting pharmaSetting;
	public PharmaScreenShot() {
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
	public String getProjectToken() {
		return projectToken;
	}
	public void setProjectToken(String projectToken) {
		this.projectToken = projectToken;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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
	
	
	
}
