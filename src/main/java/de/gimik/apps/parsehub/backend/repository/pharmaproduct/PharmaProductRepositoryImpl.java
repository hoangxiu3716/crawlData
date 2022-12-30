
package de.gimik.apps.parsehub.backend.repository.pharmaproduct;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.repository.CommonRepository;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaProductBasicInfo;
/**
 *
 * @author dang
 */
public class PharmaProductRepositoryImpl extends CommonRepository implements PharmaProductRepositoryCustom {
    @Override
    public Page<PharmaProductBasicInfo> findAll(Pageable pageable, Map<String, String> filter) {
        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PharmaProduct> criteriaQuery = queryBuilder.createQuery(PharmaProduct.class);
        Root<PharmaProduct> root = criteriaQuery.from(PharmaProduct.class);
        criteriaQuery.where(getPredicates(filter, queryBuilder, root))
        .distinct(true);
        Sort sort = pageable.getSort();
        if (sort != null) {
            criteriaQuery.orderBy(toOrders(sort, root, queryBuilder));
        }
        TypedQuery<PharmaProduct> query = entityManager.createQuery(criteriaQuery);
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
        List<PharmaProduct> list = query.getResultList();
        List<PharmaProductBasicInfo> reportInfos = Lists.newArrayList(Iterables.transform(list,
                new Function<PharmaProduct, PharmaProductBasicInfo>() {
                    @Override
                    public PharmaProductBasicInfo apply(PharmaProduct item) {
                        return new PharmaProductBasicInfo(item);
                    }
                }));
        
        CriteriaQuery<Long> countCriteriaQuery = queryBuilder.createQuery(Long.class);
        Root<PharmaProduct> countRoot = countCriteriaQuery.from(PharmaProduct.class);
        countCriteriaQuery.where(getPredicates(filter, queryBuilder, countRoot));
        countCriteriaQuery.select(queryBuilder.countDistinct(countRoot));
        TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
        Long total = countQuery.getSingleResult();

        Page<PharmaProductBasicInfo> page = new PageImpl<>(reportInfos, pageable, total);
        return page;
    }
    private Predicate[] getPredicates(Map<String, String> filter, CriteriaBuilder queryBuilder, Root<PharmaProduct> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!CollectionUtils.isEmpty(filter)) {
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(key))
                    continue;
                if(key.equals("active")) {
                	predicates.add(queryBuilder.isTrue(root.<Boolean>get("active")));
                	continue;
                }
                predicates.add(queryBuilder.like(root.<String>get(key), "%" + value + "%"));            
            }
            
       
    }
    
        return predicates.toArray(new Predicate[0]);

}
}
