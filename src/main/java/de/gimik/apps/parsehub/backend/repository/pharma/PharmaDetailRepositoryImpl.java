
package de.gimik.apps.parsehub.backend.repository.pharma;
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
import de.gimik.apps.parsehub.backend.repository.CommonRepository;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;
import de.gimik.apps.parsehub.backend.util.Constants;
/**
 *
 * @author dang
 */
public class PharmaDetailRepositoryImpl extends CommonRepository implements PharmaDetailCustomRepository {
    @Override
    public Page<PharmacyCrawlBasicInfo> findAll(Pageable pageable, Map<String, String> filter, Integer pharmaId,Date from,Date to, Double fromPrice, Double toPrice, Integer fromDiscount, Integer toDiscount) {
        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PharmaDetail> criteriaQuery = queryBuilder.createQuery(PharmaDetail.class);
        Root<PharmaDetail> root = criteriaQuery.from(PharmaDetail.class);
        criteriaQuery.where(getPredicates(filter, queryBuilder, root, pharmaId,from,to,fromPrice,toPrice, fromDiscount, toDiscount))
        .distinct(true);
        Sort sort = pageable.getSort();
        if (sort != null) {
            criteriaQuery.orderBy(toOrders(sort, root, queryBuilder));
        }
        TypedQuery<PharmaDetail> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
//        List<PharmaDetail> list = query.getResultList();
//        CriteriaQuery<Long> countCriteriaQuery = queryBuilder.createQuery(Long.class);
//        Root<PharmaDetail> countRoot = countCriteriaQuery.from(PharmaDetail.class);
//        countCriteriaQuery.where(getPredicates(filter, queryBuilder, countRoot,from,to));
//        countCriteriaQuery.select(queryBuilder.countDistinct(countRoot));
//        TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
//        Long total = countQuery.getSingleResult();
//        
        List<PharmaDetail> list = query.getResultList();
        List<PharmacyCrawlBasicInfo> reportInfos = Lists.newArrayList(Iterables.transform(list,
                new Function<PharmaDetail, PharmacyCrawlBasicInfo>() {
                    @Override
                    public PharmacyCrawlBasicInfo apply(PharmaDetail item) {
                        return new PharmacyCrawlBasicInfo(item);
                    }
                }));
        
        CriteriaQuery<Long> countCriteriaQuery = queryBuilder.createQuery(Long.class);
        Root<PharmaDetail> countRoot = countCriteriaQuery.from(PharmaDetail.class);
        countCriteriaQuery.where(getPredicates(filter, queryBuilder, countRoot, pharmaId,from, to, fromPrice, toPrice, fromDiscount, toDiscount));
        countCriteriaQuery.select(queryBuilder.countDistinct(countRoot));
        TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
        Long total = countQuery.getSingleResult();

        Page<PharmacyCrawlBasicInfo> page = new PageImpl<>(reportInfos, pageable, total);
        return page;
    }
    private Predicate[] getPredicates(Map<String, String> filter, CriteriaBuilder queryBuilder, Root<PharmaDetail> root, Integer pharmaId,Date from, Date to,Double fromPrice, Double toPrice, Integer fromDiscount, Integer toDiscount) {
        List<Predicate> predicates = new ArrayList<>();
        if(from!=null)
            predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Date>get("creationTime"), from));
        if(to != null)
            predicates.add(queryBuilder.lessThanOrEqualTo(root.<Date>get("creationTime"), to));
        if(fromPrice!=null)
            predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Double>get("priceInDouble"), fromPrice));
        if(toPrice != null)
            predicates.add(queryBuilder.lessThanOrEqualTo(root.<Double>get("priceInDouble"), toPrice));
        if(fromDiscount!=null)
            predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Integer>get("discount"), fromDiscount));
        if(toDiscount != null)
            predicates.add(queryBuilder.lessThanOrEqualTo(root.<Integer>get("discount"), toDiscount));
        if(pharmaId!=null)
            predicates.add(queryBuilder.equal(root.<PharmaSetting>get("pharmaSetting"), pharmaId));
        if (!CollectionUtils.isEmpty(filter)) {
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(key))
                    continue;
                predicates.add(queryBuilder.like(root.<String>get(key),"%" + value + "%"));            
            }
        
       
    }
    
        return predicates.toArray(new Predicate[0]);

}
    @Override
    public List<PharmacyCrawlBasicInfo> detailPharma(FilterConditionInfo item) {
        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PharmaDetail> criteriaQuery = queryBuilder.createQuery(PharmaDetail.class);
        Root<PharmaDetail> root = criteriaQuery.from(PharmaDetail.class);
        List<Predicate> predicates = new ArrayList<>();
        if(item.getFromTime() != null)
        	predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Date>get("creationTime"), item.getFromTime()));
        if(item.getToTime() != null )
        	predicates.add(queryBuilder.lessThan(root.<Date>get("creationTime"), item.getToTime()));
        if(!CollectionUtils.isEmpty(item.getPzns()))
        	predicates.add(queryBuilder.and(root.<String>get("pzn").in(item.getPzns())));
        if(item.getFromPrice() != null)
        	predicates.add(queryBuilder.greaterThanOrEqualTo(root.<Double>get("priceInDouble"), item.getFromPrice()));
        if(item.getToPrice() != null)
        	predicates.add(queryBuilder.lessThanOrEqualTo(root.<Double>get("priceInDouble"), item.getToPrice()));
//        if(!StringUtils.isEmpty(item.getTypeOfPharma()) && item.getTypeOfPharma().equals("CROSS-SELLING")) {
//        	if(item.getPharmaId() != null)
//                predicates.add(queryBuilder.equal(root.<PharmaSetting>get("pharmaSetting"), item.getPharmaId()));
//        }else if(item.getPharmaKeyword() != null)
//            predicates.add(queryBuilder.equal(root.<String>get("keyword"), item.getPharmaKeyword()));
        Join<PharmaDetail, ParsehubSetting> settingJoin = root.join("parsehubSetting", JoinType.INNER);
    	if(item.getParentId() != null) {
    		Predicate predicates1 = queryBuilder.equal(root.<PharmaSetting>get("pharmaSetting"), item.getPharmaId());
    		Predicate predicates2 = queryBuilder.and(queryBuilder.equal(root.<PharmaSetting>get("pharmaSetting"), item.getParentId()),queryBuilder.equal(settingJoin.<Integer>get("id"),  Constants.Shop.SHOP_SANICARE_FROM_PHARMA));
    		Predicate andPredicates = queryBuilder.or(predicates1,predicates2);
    		predicates.add(andPredicates);
    	}else if(item.getPharmaId() != null)
    		 predicates.add(queryBuilder.equal(root.<PharmaSetting>get("pharmaSetting"), item.getPharmaId()));
    		
        if(item.getShopId() != null ) {
        	if(item.getChildrenId() != null) {
        		predicates.add(queryBuilder.equal(settingJoin.<Integer>get("id"), item.getChildrenId()));
        	}else
        		predicates.add(queryBuilder.equal(settingJoin.<Integer>get("id"), item.getShopId()));
        }
        if(!CollectionUtils.isEmpty(item.getShopUrls())) {
        	predicates.add(queryBuilder.and(settingJoin.<String>get("url").in(item.getShopUrls())));
        }
       
        criteriaQuery.where(predicates.toArray(new Predicate[0]))
        .distinct(true);

        TypedQuery<PharmaDetail> query = entityManager.createQuery(criteriaQuery);

        List<PharmaDetail> list = query.getResultList();
        List<PharmacyCrawlBasicInfo> reportInfos = Lists.newArrayList(Iterables.transform(list,
                new Function<PharmaDetail, PharmacyCrawlBasicInfo>() {
                    @Override
                    public PharmacyCrawlBasicInfo apply(PharmaDetail item) {
                        return new PharmacyCrawlBasicInfo(item);
                    }
                }));
        
        return reportInfos;
    }
}
