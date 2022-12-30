package de.gimik.apps.parsehub.backend.job;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import de.gimik.apps.parsehub.backend.model.ParsehubDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.service.ParsehubService;
import de.gimik.apps.parsehub.backend.service.PharmaProductService;
import de.gimik.apps.parsehub.backend.util.TextUtility;
import de.gimik.apps.parsehub.backend.web.viewmodel.pushhub.PharmacyBasicData;
public class ParsehubData {

	@Autowired
	ParsehubService parsehubService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void getParsehubData() {
		
		List<ParsehubSetting> parsehubSettings = parsehubService.findAllSetting();
		System.out.println("run it getParsehubData");
		if(!CollectionUtils.isEmpty(parsehubSettings)) {
			for(ParsehubSetting item : parsehubSettings) {
				String projectToken = item.getProjectToken();
				if(StringUtils.isEmpty(projectToken))
					continue;
				String apiKey = item.getApiKey() == null ? "tDeObZvKLg0J" :  item.getApiKey();
				String url = "https://parsehub.com/api/v2/projects/"+projectToken+"/last_ready_run/data?api_key="+apiKey;
				String result = callParsehubService(url);
//				@SuppressWarnings("serial")
//				Map<String, List<PharmacyBasicData>> retMap = new Gson().fromJson(
//						result, new TypeToken<HashMap<String, List<PharmacyBasicData>>>() {}.getType()
//					);
				System.out.println(url);
				buidDataPharmacy(result, item, projectToken);
//				System.out.println(result);
			}
		}
//		System.out.println("deleted " + deleteDate.getTime());
	}
	static String callParsehubService(String url) {
	    try {
	    	
	    	URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) { 
			 Reader reader = null;
		      if ("gzip".equals(con.getContentEncoding())) {
		         reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()), StandardCharsets.UTF_8);
		      }
		      else {
		         reader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
		      }
		      StringBuffer response = new StringBuffer();
		      while (true) {
		         int ch = reader.read();
		         if (ch==-1) {
		            break;
		         }
		         response.append((char)ch);
		      }
		      String finalvalue= response.toString();
//		      System.out.println(finalvalue);
		      return finalvalue;
			}else return "";
			
	     } 
	    catch (Exception e) {
	    	e.printStackTrace();
	        return null;
	    }   
	}
	public   void buidDataPharmacy(String result, ParsehubSetting parsehubSetting, String projectToken) {
		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		calendar.set(Calendar.HOUR, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date importDate = calendar.getTime();
		try {
			JSONObject convertedObject = new JSONObject(result);
			@SuppressWarnings("unchecked")
			Iterator<String> keys = convertedObject.keys();
			while(keys.hasNext()) {
			    String key = keys.next();
			    if (convertedObject.get(key) instanceof JSONArray) {
			    	JSONArray values = convertedObject.getJSONArray(key);
			    	if(values != null && values.length() > 0) {
			    		List<ParsehubDetail> parsehubDetails = new ArrayList<ParsehubDetail>();
				    	for (int i=0;i< values.length();i++){   
				    		String value = values.get(i).toString();
				    		PharmacyBasicData item = new Gson().fromJson(value, PharmacyBasicData.class);  
				    		ParsehubDetail parsehubDetail = new ParsehubDetail();
							parsehubDetail = ParsehubDetail.buildData(parsehubDetail, item,key,projectToken);
							parsehubDetail.setParsehubSetting(parsehubSetting);
							parsehubDetail.setKeyword(key);
							parsehubDetail.setImportDate(importDate);
							parsehubDetail.setSequenceByKeyword(i+1);
							System.out.println(parsehubDetail.getFullPzn());
							System.out.println(parsehubDetail.getName());
							// check product 
							if(!StringUtils.isEmpty(parsehubDetail.getPzn())) {
								PharmaProduct pharmaProductUnique = pharmaProductService.findByPznAndActiveTrue(parsehubDetail.getPzn().trim());
								if(pharmaProductUnique != null) parsehubDetail.setProductId(pharmaProductUnique.getId());
								else {
									pharmaProductUnique = new PharmaProduct(parsehubDetail.getName(), parsehubDetail.getPzn().trim());
									pharmaProductService.create(pharmaProductUnique);
									parsehubDetail.setProductId(pharmaProductUnique.getId());
								}
							}
							parsehubDetails.add(parsehubDetail);
				        }  
				    	if(!CollectionUtils.isEmpty(parsehubDetails)) System.out.println("size "+parsehubDetails.size());
				    		parsehubService.createListDetail(parsehubDetails);
			    	}
			    }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		

	}
	public static void main(String[] args) {
//		String result = callParsehubService("https://parsehub.com/api/v2/projects/t1jAATgLG5J1/last_ready_run/data?api_key=tDeObZvKLg0J");
//		JSONObject convertedObject;
//		try {
//			convertedObject = new JSONObject(result);
//			Iterator<String> keys = convertedObject.keys();
//			while(keys.hasNext()) {
//			    String key = keys.next();
//			    ArrayList<Object> listdata = new ArrayList<Object>();  
//			    if (convertedObject.get(key) instanceof JSONArray) {
//			    	JSONArray test = convertedObject.getJSONArray(key);
//			    	for (int i=0;i<test.length();i++){   
//		                  
//		                //Adding each element of JSON array into ArrayList  
//			    			listdata.add(test.get(i));  
//			    			System.out.println(test.get(i).toString());
//			    			PharmacyBasicData ce = new Gson().fromJson(test.get(i).toString(), PharmacyBasicData.class);
//			    			System.out.println(ce);
//			        }  
////			    	List<PharmacyBasicData> contactList = new Gson().fromJson(test, new TypeToken<List<PharmacyBasicData>>(){}.getType());
////			    	String nsd = new Gson().toJson(test);
////			    	List<PharmacyBasicData> data =  new Gson().fromJson(
////			    			nsd, new TypeToken< List<PharmacyBasicData>>() {}.getType()
////			    			);
////			    	System.out.println(nsd);
//			    }
//			}
////			System.out.println(convertedObject);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
//		@SuppressWarnings("serial")
//		Map<String, List<PharmacyBasicData>> retMap = new Gson().fromJson(
//				result, new TypeToken<HashMap<String, List<PharmacyBasicData>>>() {}.getType()
//			);
	
//		String text = "5.111,19 €";
//		text = text.replace(".", "");
//		text = text.replace(",", ".");
//		String t= text.replaceAll("[^0-9.]", "");
//		Double d = Double.valueOf(t);
//		System.out.println(d);
//		String fullPzn = "Freestyle Precision Blutzucker Teststreifen ohne Codieren\nTeststreifen, 50 Stück\nAbbott GmbH Abbott Diabetes Care\nArt.-Nr. (PZN): 6905334\n(6)";
//		String pzn = "";
//		if(!StringUtils.isEmpty(fullPzn)) {
//			String checkPzn = "(PZN)";
//			if(fullPzn.toUpperCase().indexOf(checkPzn) > -1) {
//				pzn = fullPzn.substring(fullPzn.toUpperCase().indexOf(checkPzn)+6, fullPzn.length());
//				if(pzn.indexOf("\n") > -1)
//					pzn = pzn.substring(0, pzn.indexOf("\n"));
//			}
//			if(StringUtils.isEmpty(pzn) && TextUtility.isNumeric(fullPzn)) {
//					pzn = fullPzn;
//			}
//			
//			
//		}
//		pzn = "123456";
//		if(pzn.length() < 8)
//		 pzn = ("00000000" + pzn).substring(pzn.length());
//		 System.out.println(pzn);
//		System.out.println("https://parsehub.com/api/v2/projects/t_M_bnKRDX7M/last_ready_run/data?api_key=tDeObZvKLg0J");
//		String result = callParsehubService("https://parsehub.com/api/v2/projects/t_M_bnKRDX7M/last_ready_run/data?api_key=tDeObZvKLg0J");
//		buidDataPharmacy(result, null, "t_M_bnKRDX7M");
	}
}
