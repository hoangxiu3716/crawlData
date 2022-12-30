package de.gimik.apps.parsehub.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import de.gimik.apps.parsehub.backend.model.ParsehubSetting;

@Entity
@Table(name = "group_pharma_detail")
@EntityListeners(AuditingEntityListener.class)
public class GroupPharmaDetail extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@Column
	private String name;
	@Column
	private String type;
	@Column(nullable = false, columnDefinition = "TINYINT(1)  DEFAULT 0")
	private boolean deleted = false;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "groupPharmaDetail")
	private List<BasePharma> basePharma ;
	@ManyToMany(fetch = FetchType.EAGER) 
	@JoinTable(name = "group_shop",
	            joinColumns = @JoinColumn(name = "groupId"),
	            inverseJoinColumns = @JoinColumn(name = "shopId"))
    private List<ParsehubSetting> shops = new ArrayList<ParsehubSetting>();
	public GroupPharmaDetail() {
		super();
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

	public List<BasePharma> getBasePharma() {
		return basePharma;
	}

	public void setBasePharma(List<BasePharma> basePharma) {
		this.basePharma = basePharma;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public List<ParsehubSetting> getShops() {
		return shops;
	}

	public void setShops(List<ParsehubSetting> shops) {
		this.shops = shops;
	}
	
}
