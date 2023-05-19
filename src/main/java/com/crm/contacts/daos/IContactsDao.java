package com.crm.contacts.daos;

import com.crm.contacts.models.Contacts;

import java.util.List;

public interface IContactsDao {
    public List<Contacts> findContacts(String firstName, String lastName, String email) throws Exception;

    public Contacts save(String firstName, String lastName, String email, String phone) throws Exception;

    public Contacts update(Long id, String firstName, String lastName, String email, String phone) throws Exception;

    public void delete(Long id) throws Exception;
}
