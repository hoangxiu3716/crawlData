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
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product extends AuditableEntity implements Serializable {

	private static final long serialVersionUID = 3483915904023463446L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id", name = "category_Id")
	private Categories category_id;
	@Column
	private String size;
	@Column
	private String name;
	@Column
	private Double price;
	@Column
	private Integer sale;
	@Column
	private String title;
	@Column(nullable = false, columnDefinition = "TINYINT(1)  DEFAULT 0")
    private boolean highlight = false;
	@Column(nullable = false, columnDefinition = "TINYINT(1)  DEFAULT 0")
    private boolean new_product = false;
	@Column(length=1000)
	private String detail;
	@Column
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date created_at;
	@Column
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date updated_at;
	public Product() {
		super();
	}
	public Categories getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Categories category_id) {
		this.category_id = category_id;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getSale() {
		return sale;
	}
	public void setSale(Integer sale) {
		this.sale = sale;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isHighlight() {
		return highlight;
	}
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
	public boolean isNew_product() {
		return new_product;
	}
	public void setNew_product(boolean new_product) {
		this.new_product = new_product;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
}
