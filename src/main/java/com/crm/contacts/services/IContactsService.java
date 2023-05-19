package com.crm.contacts.services;

import com.crm.contacts.dtos.ContactsRequestResponse;

import java.util.List;

public interface IContactsService {
    public List<ContactsRequestResponse> fetchAll(String firstName, String lastName, String email) throws Exception;

    public ContactsRequestResponse saveContact(ContactsRequestResponse contact) throws Exception;

    public ContactsRequestResponse updateContact(ContactsRequestResponse contact) throws Exception;

    public ContactsRequestResponse deleteContact(Long id) throws Exception;
}
