
package de.gimik.apps.parsehub.backend.repository.parsehub;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.model.Role;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.repository.CommonRepository;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;
/**
 *
 * @author trung
 */
public class ParsehubDetailRepositoryImpl extends CommonRepository implements ParsehubDetailCustomRepository {
//    @Override
//    public Page<PharmacyCrawlBasicInfo> findAll(Pageable pageable, Map<String, String> filter, Integer pharmaId,Date from,Date to, Double fromPrice, Double toPrice, Integer fromDiscount, Integer toDiscount) {
//        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<PharmaDetail> criteriaQuery = queryBuilder.createQuery(PharmaDetail.class);
//        Root<PharmaDetail> root = criteriaQuery.from(PharmaDetail.class);
//        criteriaQuery.where(getPredicates(filter, queryBuilder, root, pharmaId,from,to,fromPrice,toPrice, fromDiscount, toDiscount))
//        .distinct(true);
//        Sort sort = pageable.getSort();
//        if (sort != null) {
//            criteriaQuery.orderBy(toOrders(sort, root, queryBuilder));
//        }
//        TypedQuery<PharmaDetail> query = entityManager.createQuery(criteriaQuery);
//        query.setFirstResult(pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
////        List<PharmaDetail> list = query.getResultList();
////        CriteriaQuery<Long> countCriteriaQuery = queryBuilder.createQuery(Long.class);
////        Root<PharmaDetail> countRoot = countCriteriaQuery.from(PharmaDetail.class);
////        countCriteriaQuery.where(getPredicates(filter, queryBuilder, countRoot,from,to));
////        countCriteriaQuery.select(queryBuilder.countDistinct(countRoot));
////        TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
////        Long total = countQuery.getSingleResult();
////        
//        List<PharmaDetail> list = query.getResultList();
//        List<PharmacyCrawlBasicInfo> reportInfos = Lists.newArrayList(Iterables.transform(list,
//                new Function<PharmaDetail, PharmacyCrawlBasicInfo>() {
//                    @Override
//                    public PharmacyCrawlBasicInfo apply(PharmaDetail item) {
//                        return new PharmacyCrawlBasicInfo(item);
//                    }
//                }));
//        
//        CriteriaQuery<Long> countCriteriaQuery = queryBuilder.createQuery(Long.class);
//        Root<PharmaDetail> countRoot = countCriteriaQuery.from(PharmaDetail.class);
//        countCriteriaQuery.where(getPredicates(filter, queryBuilder, countRoot, pharmaId,from, to, fromPrice, toPrice, fromDiscount, toDiscount));
//        countCriteriaQuery.select(queryBuilder.countDistinct(countRoot));
//        TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
//        Long total = countQuery.getSingleResult();
//
//        Page<PharmacyCrawlBasicInfo> page = new PageImpl<>(reportInfos, pageable, total);
//        return page;
//    }
//    private Predicate[] getPredicates(Map<String, String> filter, CriteriaBuilder queryBuilder, Root<PharmaDetail> root, Integer pharmaId,Date from, Date to,Double fromPrice, Double toPrice, Integer fromDiscount, Integer toDiscount) {
//        List<Predicate> predicates = new ArrayList<>();
//        if(from!=null)
//            predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Date>get("creationTime"), from));
//        if(to != null)
//            predicates.add(queryBuilder.lessThanOrEqualTo(root.<Date>get("creationTime"), to));
//        if(fromPrice!=null)
//            predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Double>get("priceInDouble"), fromPrice));
//        if(toPrice != null)
//            predicates.add(queryBuilder.lessThanOrEqualTo(root.<Double>get("priceInDouble"), toPrice));
//        if(fromDiscount!=null)
//            predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Integer>get("discount"), fromDiscount));
//        if(toDiscount != null)
//            predicates.add(queryBuilder.lessThanOrEqualTo(root.<Integer>get("discount"), toDiscount));
//        if(pharmaId!=null)
//            predicates.add(queryBuilder.equal(root.<PharmaSetting>get("pharmaSetting"), pharmaId));
//        if (!CollectionUtils.isEmpty(filter)) {
//            for (Map.Entry<String, String> entry : filter.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(key))
//                    continue;
//                predicates.add(queryBuilder.like(root.<String>get(key),"%" + value + "%"));            
//            }
//        
//       
//    }
//    
//        return predicates.toArray(new Predicate[0]);
//
//}
    @Override
    public List<PharmacyCrawlBasicInfo> detailParsehub(FilterConditionInfo item) {
        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ParsehubDetail> criteriaQuery = queryBuilder.createQuery(ParsehubDetail.class);
        Root<ParsehubDetail> root = criteriaQuery.from(ParsehubDetail.class);
        List<Predicate> predicates = new ArrayList<>();
        if(item.getFromTime() != null)
        	predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Date>get("importDate"), item.getFromTime()));
        if(item.getToTime() != null )
        	predicates.add(queryBuilder.lessThan(root.<Date>get("importDate"), item.getToTime()));
        if(!CollectionUtils.isEmpty(item.getPzns()))
        	predicates.add(queryBuilder.and(root.<String>get("pzn").in(item.getPzns())));
        if(item.getFromPrice() != null)
        	predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Double>get("price"), item.getFromPrice()));
        if(item.getToPrice() != null)
        	predicates.add(queryBuilder.lessThanOrEqualTo(root.<Double>get("price"), item.getToPrice()));
        if(item.getParsehubKeyword()!= null) {
        	if(!StringUtils.isEmpty(item.getTypeOfPharma()) && item.getTypeOfPharma().equals(Constants.Object.CROSS_SELLING))
        		predicates.add(queryBuilder.like(root.<String>get("keyword"),"%" + item.getParsehubKeyword() + "%")); 
        	else {
//	        	if(!item.getParsehubKeyword().equals("diabetes_teststreifen")) {
//	        		if(item.getParsehubKeyword().equals(Constants.Keyword.KAT1)) {
//	        			
//	        		}
//	        		predicates.add(queryBuilder.equal(root.<String>get("keyword"), item.getParsehubKeyword()));
//	        	}
//	        	else {
//	        		
//	        	}
        		switch(item.getParsehubKeyword()) {
        		  case Constants.Keyword.diabetes_teststreifen:
        			  predicates.add(queryBuilder.or(queryBuilder.equal(root.<String>get("keyword"), item.getParsehubKeyword()),queryBuilder.equal(root.<String>get("keyword"), Constants.Keyword.dia_teststreifen)));
        		    break;
        		  case Constants.Keyword.KAT1:
        			  predicates.add(queryBuilder.or(queryBuilder.equal(root.<String>get("keyword"), item.getParsehubKeyword()),queryBuilder.equal(root.<String>get("keyword"), Constants.Keyword.KAT_1)));
        		    break;
        		  case Constants.Keyword.KAT2:
        			  predicates.add(queryBuilder.or(queryBuilder.equal(root.<String>get("keyword"), item.getParsehubKeyword()),queryBuilder.equal(root.<String>get("keyword"), Constants.Keyword.KAT_2)));
        		    break;  
        		  default:
        			  predicates.add(queryBuilder.equal(root.<String>get("keyword"), item.getParsehubKeyword()));
        		}
        	}
        }
        Join<ParsehubDetail, ParsehubSetting> settingJoin = root.join("parsehubSetting", JoinType.INNER);
//        predicates.add(queryBuilder.equal(settingJoin.<String>get("type"), "voll"));
        if(!StringUtils.isEmpty(item.getShopUrl())) {
        	 predicates.add(queryBuilder.equal(settingJoin.<String>get("url"), item.getShopUrl()));
        }
        if(!CollectionUtils.isEmpty(item.getShopUrls())) {
        	predicates.add(queryBuilder.and(settingJoin.<String>get("url").in(item.getShopUrls())));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]))
        .distinct(true);

        TypedQuery<ParsehubDetail> query = entityManager.createQuery(criteriaQuery);

        List<ParsehubDetail> list = query.getResultList();
        List<PharmacyCrawlBasicInfo> reportInfos = Lists.newArrayList(Iterables.transform(list,
                new Function<ParsehubDetail, PharmacyCrawlBasicInfo>() {
                    @Override
                    public PharmacyCrawlBasicInfo apply(ParsehubDetail item) {
                        return new PharmacyCrawlBasicInfo(item);
                    }
                }));
        
        return reportInfos;
    }
}
