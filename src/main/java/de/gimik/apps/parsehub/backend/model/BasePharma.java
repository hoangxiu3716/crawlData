package de.gimik.apps.parsehub.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "base_pharma")
@EntityListeners(AuditingEntityListener.class)
public class BasePharma extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "group_pharma_id")
	private GroupPharmaDetail groupPharmaDetail;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "product_id")
	private PharmaProduct pharmaProduct;
	@Column(nullable = false, columnDefinition = "TINYINT(1)  DEFAULT 0")
	private boolean deleted = false;
	public BasePharma() {
		super();
	}
	public BasePharma(BasePharma item) {
		super();
	}
	
	public PharmaProduct getPharmaProduct() {
		return pharmaProduct;
	}
	public void setPharmaProduct(PharmaProduct pharmaProduct) {
		this.pharmaProduct = pharmaProduct;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public GroupPharmaDetail getGroupPharmaDetail() {
		return groupPharmaDetail;
	}
	public void setGroupPharmaDetail(GroupPharmaDetail groupPharmaDetail) {
		this.groupPharmaDetail = groupPharmaDetail;
	}
	
	
	
}
