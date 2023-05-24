package com.crm.contacts.daos.impl;

import com.crm.contacts.daos.IContactsDao;
import com.crm.contacts.models.Contacts;
import com.crm.contacts.util.HibernateUtil;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.naming.directory.InvalidAttributesException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class ContactsDao extends AbstractMasterDaoImpl implements IContactsDao {

    private static final Logger LOG = Logger.getLogger(ContactsDao.class.getName());

    /**
     * This function will search all contacts with
     * matching first name, last name and email.
     * All the searchable params are queried with AND conditions.
     *
     * @param firstName First name of the user
     * @param lastName  Last name of the user
     * @param email     Email address of the user
     * @return List of user contacts
     */
    @Override
    public List<Contacts> findContacts(String firstName, String lastName, String email) throws Exception {
        if (firstName == null && lastName == null && email == null)
            throw new InvalidAttributesException("firstName, lastName and email cannot be null");

        Map<String, Object> attributeMap = new HashMap<String, Object>();
        attributeMap.put("firstName", firstName);
        attributeMap.put("lastName", lastName);
        attributeMap.put("email", email);

        return fetchAllWithCriteria(Contacts.class, attributeMap);
    }

    /**
     * This function will save entry in contacts table.
     *
     * @param firstName First name of the user
     * @param lastName  Last name of the user
     * @param email     Email address of the user
     * @param phone     Phone number of the user
     * @return Contact object saved in the database
     */
    @Override
    public Contacts save(String firstName, String lastName, String email, String phone) throws Exception {
        if (firstName == null)
            throw new InvalidAttributesException("firstName cannot be null");
        else if (lastName == null)
            throw new InvalidAttributesException("firstName cannot be null");
        else if (email == null)
            throw new InvalidAttributesException("email cannot be null");
        else if (phone == null)
            throw new InvalidAttributesException("phone cannot be null");

        Contacts contact = new Contacts(firstName, lastName, email, phone);
        return (Contacts) add(contact);
    }

    /**
     * This function will save an entry if no id is present
     * and update when id is passed in the API
     *
     * @param id        Long id of the existing record
     * @param firstName First name of the user
     * @param lastName  Last name of the user
     * @param email     Email address of the user
     * @param phone     Phone number of the user
     * @return Updated or saved contact
     */
    @Override
    public Contacts update(Long id, String firstName, String lastName, String email, String phone) throws Exception {
        if (id == null)
            throw new Exception("Id cannot be null");

        Contacts contact = new Contacts(id, firstName, lastName, email, phone);
        return (Contacts) update(contact);
    }

    private String getUpdateQueryById(String firstName, String lastName, String email, String phone) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  UPDATE contacts SET  ");
        if (firstName != null)
            queryBuilder.append("  firstName = '").append(firstName).append("', ");
        if (lastName != null)
            queryBuilder.append("  lastName = '").append(lastName).append("', ");
        if (email != null)
            queryBuilder.append("  email = '").append(email).append("', ");
        if (phone != null)
            queryBuilder.append("  phone = '").append(phone).append("' ");
        queryBuilder.append("  WHERE id = :id ");

        return queryBuilder.toString();
    }

    /**
     * This function will delete the record from the database
     *
     * @param id Long id primary key against contacts table
     */
    @Override
    public void delete(Long id) throws Exception {
        if (id == null)
            throw new InvalidAttributesException("Id cannot be null");

        Session session = HibernateUtil.getHibernateSession();
        try {
            HibernateUtil.beginTransaction(session);
            int x = session.createSQLQuery("DELETE FROM contacts WHERE id = :id")
                    .setParameter("id", id).executeUpdate();
            HibernateUtil.commitTransaction(session);
            if (x == 0)
                throw new Exception("No records deleted, please enter valid id");
        } catch (Exception ex) {
            LOG.severe("Unable to update the record in the database");
            HibernateUtil.rollBackTransaction(session);
            throw ex;
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    private String getContactsByIdentifiersQuery(String firstName, String lastName, String email) {
        boolean multiParam = false;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT  ");
        queryBuilder.append("   id,  ");
        queryBuilder.append(" 	firstName, ");
        queryBuilder.append(" 	lastName, ");
        queryBuilder.append(" 	email, ");
        queryBuilder.append(" 	phone ");
        queryBuilder.append(" FROM  ");
        queryBuilder.append(" 	contacts  ");
        queryBuilder.append(" WHERE  ");
        if (firstName != null) {
            multiParam = true;
            queryBuilder.append(" 	firstName = :firstName  ");
        }
        if (lastName != null) {
            if (multiParam)
                queryBuilder.append(" AND ");
            queryBuilder.append(" 	lastName = :lastName ");
            multiParam = true;
        }
        if (email != null) {
            if (multiParam)
                queryBuilder.append(" AND ");
            queryBuilder.append(" 	email = :email ");
        }

        return queryBuilder.toString();
    }
}
