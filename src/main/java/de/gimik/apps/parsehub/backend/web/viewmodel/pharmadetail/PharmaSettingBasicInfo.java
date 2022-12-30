package de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail;

import java.util.List;

import org.springframework.util.StringUtils;

import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.util.DateTimeUtility;

public class PharmaSettingBasicInfo {
	private Integer id;
	private String name;
	private String pharmaKeyword;
	private String parsehubKeyword;
	private String type;
	private String url;
	private boolean active;
	private Integer parentId;
	private String code;
	public PharmaSettingBasicInfo() {
		super();
	}
	public PharmaSettingBasicInfo(PharmaSetting item) {
		super();
		this.id = item.getId();
		this.name = item.getName();
		this.pharmaKeyword = item.getKeyword();
		this.parsehubKeyword = item.getParsehubKeyword();
		this.active = item.isActive();
		this.type = item.getType();
		this.url = item.getUrl();
		this.parentId = item.getParentId();
		this.code = item.getCode();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getPharmaKeyword() {
		return pharmaKeyword;
	}
	public void setPharmaKeyword(String pharmaKeyword) {
		this.pharmaKeyword = pharmaKeyword;
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
	public static String convertKeyword(String keyword, List<PharmaSettingBasicInfo> pharmaSettings) {
		
		String result = "";
		if(!StringUtils.isEmpty(keyword)) {
			for (PharmaSettingBasicInfo item : pharmaSettings) {
		        if (!item.getType().equals(Constants.Object.CROSS_SELLING)) {
		        	switch(keyword) {
	        		  case Constants.Keyword.dia_teststreifen:
	        			  if(item.getParsehubKeyword().equals(Constants.Keyword.diabetes_teststreifen))
	        				  result = item.getName();
	        		    break;
	        		  case Constants.Keyword.KAT_1:
	        			  if(item.getParsehubKeyword().equals(Constants.Keyword.KAT1))
		        				  result = item.getName();
	        		    break;
	        		  case Constants.Keyword.KAT_2:
	        			  if(item.getParsehubKeyword().equals(Constants.Keyword.KAT2))
		        				  result = item.getName();
	        		    break;  
	        		  default:
	        			  if(item.getPharmaKeyword().equals(keyword) || item.getParsehubKeyword().equals(keyword)  ) {
	   		        	   result = item.getName();
	   		           	}
	        		}
		        	if(!StringUtils.isEmpty(result))  break;
		        }else {
		        	 if(item.getPharmaKeyword().equals(keyword) || item.getParsehubKeyword().equals(keyword) 
	  		        			|| (keyword.indexOf("_") > -1 && (keyword.split("_")[1].equals(item.getPharmaKeyword()) || keyword.split("_")[1].equals(item.getParsehubKeyword())) )){
	  		        		result = item.getName(); break;
  		        	}
		        }
		    }
		}
		return StringUtils.isEmpty(result) ? keyword :result;
	}
}
