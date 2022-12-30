
package de.gimik.apps.parsehub.backend.repository.grouppharma;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.util.ArrayList;
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

import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.repository.CommonRepository;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaViewInfo;
/**
 *
 * @author dang
 */
public class GroupPharmaRepositoryImpl extends CommonRepository implements GroupPharmaCustomRepository {
    @Override
    public Page<GroupPharmaViewInfo> findAll(Pageable pageable, Map<String, String> filter) {
        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GroupPharmaDetail> criteriaQuery = queryBuilder.createQuery(GroupPharmaDetail.class);
        Root<GroupPharmaDetail> root = criteriaQuery.from(GroupPharmaDetail.class);
        criteriaQuery.where(getPredicates(filter, queryBuilder, root))
        .distinct(true);
        Sort sort = pageable.getSort();
        if (sort != null) {
            criteriaQuery.orderBy(toOrders(sort, root, queryBuilder));
        }
        TypedQuery<GroupPharmaDetail> query = entityManager.createQuery(criteriaQuery);
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
        List<GroupPharmaDetail> list = query.getResultList();
        List<GroupPharmaViewInfo> reportInfos = Lists.newArrayList(Iterables.transform(list,
                new Function<GroupPharmaDetail, GroupPharmaViewInfo>() {
                    @Override
                    public GroupPharmaViewInfo apply(GroupPharmaDetail item) {
                        return new GroupPharmaViewInfo(item);
                    }
                }));
        
        CriteriaQuery<Long> countCriteriaQuery = queryBuilder.createQuery(Long.class);
        Root<GroupPharmaDetail> countRoot = countCriteriaQuery.from(GroupPharmaDetail.class);
        countCriteriaQuery.where(getPredicates(filter, queryBuilder, countRoot));
        countCriteriaQuery.select(queryBuilder.countDistinct(countRoot));
        TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
        Long total = countQuery.getSingleResult();

        Page<GroupPharmaViewInfo> page = new PageImpl<>(reportInfos, pageable, total);
        return page;
    }
    private Predicate[] getPredicates(Map<String, String> filter, CriteriaBuilder queryBuilder, Root<GroupPharmaDetail> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!CollectionUtils.isEmpty(filter)) {
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isEmpty(value) || StringUtils.isEmpty(key))
                    continue;
                predicates.add(queryBuilder.like(root.<String>get(key), "%" + value + "%"));            
            }
            predicates.add(queryBuilder.isFalse(root.<Boolean>get("deleted")));
        }
        return predicates.toArray(new Predicate[0]);
    }
    

    
}
