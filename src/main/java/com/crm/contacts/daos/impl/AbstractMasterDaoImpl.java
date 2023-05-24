package com.crm.contacts.daos.impl;

import com.crm.contacts.daos.AbstractMasterDao;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AbstractMasterDaoImpl implements AbstractMasterDao {


    Logger LOG = Logger.getLogger(AbstractMasterDaoImpl.class.getName());

    @PersistenceContext(unitName = "contactDataSource")
    @Qualifier(value = "entityManagerFactory")
    protected EntityManager manager;

    @Override
    public <T> List<T> fetchAllWithCriteria(Class<T> entityClass, Map<String, Object> attributeMap) {
        return fetchAllWithCriteria(entityClass, attributeMap, null, null, null, null, 0);
    }

    public <T> List<T> fetchAllWithCriteria(Class<T> entityClass, Map<String, Object> attributeMap,
                                            Map<String, String> likeMap, String orderBy, List<String> notInList, String notInAttribute, int maxResults) {

        final CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        final CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();

        Root root = criteriaQuery.from(entityClass);

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (null != attributeMap) {

            for (Map.Entry<String, Object> e : attributeMap.entrySet()) {

                String key = e.getKey();
                Object value = e.getValue();

                if (null != key && null != value) {
                    predicates.add(criteriaBuilder.equal(root.get(key), value));
                }
            }
        }

        if (null != likeMap) {

            for (Map.Entry<String, String> e : likeMap.entrySet()) {

                String key = e.getKey();
                String value = e.getValue();

                if (null != key && null != value) {
                    predicates.add(criteriaBuilder.like(root.get(key), value));
                }
            }

        }

        if (null != notInAttribute && null != notInList) {
            predicates.add(criteriaBuilder.not(root.get(notInAttribute).in(notInList)));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}));

        if (null != orderBy) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderBy)));
        }

        Query query = manager.createQuery(criteriaQuery);

        if (maxResults != 0) {
            query.setMaxResults(maxResults);
        }

        return query.getResultList();
    }

    @Override
    public <T> Object update(T data) {

        try {
            data = manager.merge(data);
            manager.flush();
            return data;
        } catch (Exception e) {
            LOG.severe("Error updating " + data.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public <T> Object add(T data) {
        try {
            manager.persist(data);
            manager.flush();
            return data;
        } catch (Exception e) {
            LOG.severe("Error adding " + data.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }
}
