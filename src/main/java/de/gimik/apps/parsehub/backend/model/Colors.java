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
@Table(name = "colors")
@EntityListeners(AuditingEntityListener.class)
public class Colors extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "product_id")
	private Categories product_id;
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String img;
	
	public Colors() {
		super();
	}

	public Categories getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Categories product_id) {
		this.product_id = product_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
}
