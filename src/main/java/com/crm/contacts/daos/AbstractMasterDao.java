package com.crm.contacts.daos;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Map;

public interface AbstractMasterDao {

    public <T> List<T> fetchAllWithCriteria(Class<T> entityClass, Map<String, Object> attributeMap);

    public <T> Object update(T data);

    public <T> Object add(T data);
}
