package de.gimik.apps.parsehub.backend.model;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;

@Entity
@Table(name = "pharma_product")
@EntityListeners(AuditingEntityListener.class)
public class PharmaProduct extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@Column
	private String name;
	@Column(unique=true)
	private String pzn;

	public PharmaProduct() {
		super();
	}

	public PharmaProduct(String name, String pzn) {
		super();
		this.name = name;
		this.pzn = pzn;
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


}
