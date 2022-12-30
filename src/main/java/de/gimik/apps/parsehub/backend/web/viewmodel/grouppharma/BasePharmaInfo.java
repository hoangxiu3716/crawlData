package de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.gimik.apps.parsehub.backend.model.BasePharma;
import de.gimik.apps.parsehub.backend.util.DateTimeUtility;
import java.util.Date;

public class BasePharmaInfo {
	private Integer id;
	private Integer baseId;
	private String baseName;
	private boolean active;
	private boolean baseActive;
	private Integer groupPharmaId;
    private Date creationTime;
    private String pzn;
	public BasePharmaInfo() {
		super();
	}
	public BasePharmaInfo(BasePharma item) {
		super();
		this.id = item.getId();
		if(item.getPharmaProduct() != null) {
			this.baseId = item.getPharmaProduct().getId();
			this.baseName = item.getPharmaProduct().getName();
			this.baseActive = item.getPharmaProduct().isActive();
			this.creationTime = item.getPharmaProduct().getCreatedDate();
			this.pzn = item.getPharmaProduct().getPzn();
		}
		this.groupPharmaId = item.getGroupPharmaDetail() == null ? -1 : item.getGroupPharmaDetail().getId();
		this.active = item.isActive();
		
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Integer getGroupPharmaId() {
		return groupPharmaId;
	}
	public void setGroupPharmaId(Integer groupPharmaId) {
		this.groupPharmaId = groupPharmaId;
	}
	public Integer getBaseId() {
		return baseId;
	}
	public void setBaseId(Integer baseId) {
		this.baseId = baseId;
	}
	public String getBaseName() {
		return baseName;
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	public boolean isBaseActive() {
		return baseActive;
	}
	public void setBaseActive(boolean baseActive) {
		this.baseActive = baseActive;
	}
	
	public String getPzn() {
		return pzn;
	}
	public void setPzn(String pzn) {
		this.pzn = pzn;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public static List<BasePharmaInfo> convertToInfo(List<BasePharma> datas){
		return  Lists.newArrayList(
        Iterables.transform(datas, new Function<BasePharma, BasePharmaInfo>() {
            @Override
            public BasePharmaInfo apply(BasePharma item) {
                return new BasePharmaInfo(item);
            }
		}));
	}
	public static List<Integer> getBasePharmaIds(List<BasePharmaInfo> datas){
		List<Integer> result = new ArrayList<Integer>();
		if(!CollectionUtils.isEmpty(datas)) {
			for(BasePharmaInfo item : datas ) {
				if(item.getBaseId() != null)
					result.add(item.getBaseId());
			}
		}
		return result;
	}
	
}
