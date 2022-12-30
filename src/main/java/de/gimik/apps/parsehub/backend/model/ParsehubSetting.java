package de.gimik.apps.parsehub.backend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "parsehub_setting")
@EntityListeners(AuditingEntityListener.class)
public class ParsehubSetting extends AuditableEntity implements Serializable {

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
	private String toolId;
	
	@Column(unique=true, nullable=true)
	private String code;
	@Column
	private Integer childrenId;
	public ParsehubSetting() {
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
	public String getToolId() {
		return toolId;
	}
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getChildrenId() {
		return childrenId;
	}
	public void setChildrenId(Integer childrenId) {
		this.childrenId = childrenId;
	}
	public static List<Integer> getShopIds(Map<Integer, Boolean> mapDatas) {
		List<Integer> result = null;
		if (mapDatas != null && !mapDatas.isEmpty()) {
			result = new ArrayList<Integer>();

			for (Map.Entry<Integer, Boolean> entry : mapDatas.entrySet()) {
				if (entry.getValue())
					result.add(entry.getKey());
			}
		}
		return result;
	}
	public static Map<Integer, Boolean> createParsehubSettingMaps(List<ParsehubSetting> datas) {
        Map<Integer, Boolean> result = new HashMap<>();
        if(!CollectionUtils.isEmpty(datas)) {
	        for (ParsehubSetting item : datas) {
	        	result.put(item.getId(), Boolean.TRUE);
	        }
        }
        return result;
    }
	public static Map<String, Boolean> createParsehubSettingSelected(List<ParsehubSetting> datas) {
        Map<String, Boolean> result = new HashMap<>();
        if(!CollectionUtils.isEmpty(datas)) {
	        for (ParsehubSetting item : datas) {
	        	result.put(item.getUrl(), Boolean.TRUE);
	        }
        }
        return result;
    }
	
}
