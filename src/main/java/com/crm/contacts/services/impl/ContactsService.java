package com.crm.contacts.services.impl;

import com.crm.contacts.daos.IContactsDao;
import com.crm.contacts.dtos.ContactsRequestResponse;
import com.crm.contacts.models.Contacts;
import com.crm.contacts.services.IContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ContactsService implements IContactsService {

    private static final Logger LOG = Logger.getLogger(ContactsService.class.getName());

    @Autowired
    private IContactsDao contactsDao;

    /**
     * This function will fetch all the contact details
     * present in the database.
     * @return List of user contacts
     */
    @Override
    public List<ContactsRequestResponse> fetchAll(String firstName, String lastName, String email) throws Exception {
        List<Contacts> results = contactsDao.findContacts(firstName, lastName, email);

        if (results == null || results.size() == 0) {
            throw new Exception("Cannot find records by search");
        }

        List<ContactsRequestResponse> response = new ArrayList<>();
        for (Contacts result: results) {
            ContactsRequestResponse contact = new ContactsRequestResponse();
            contact.setId(result.getId());
            contact.setFirstName(result.getFirstName());
            contact.setLastName(result.getLastName());
            contact.setEmail(result.getEmail());
            contact.setPhone(result.getPhone());

            response.add(contact);
        }

        return response;
    }

    /**
     * This function saves the contact details in the database
     * @param request Incoming request
     * @return ContactRequestResponse object saved in teh database
     */
    @Override
    public ContactsRequestResponse saveContact(ContactsRequestResponse request) throws Exception {
        Contacts contact = contactsDao.save(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone());
        if (contact == null) {
            LOG.severe("Unable to save contacts");
            return null;
        }

        return new ContactsRequestResponse(contact);
    }

    /**
     * This function will update the contact details of the user
     * @param request Incoming request object
     * @return ContactRequestResponse object with the updated value
     */
    @Override
    public ContactsRequestResponse updateContact(ContactsRequestResponse request) throws Exception {
        Contacts contact = contactsDao.update(request.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(), request.getPhone());

        if (contact == null) {
            LOG.severe("Unable to update the contact details of the user");
            return null;
        }
        return new ContactsRequestResponse(contact);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ContactsRequestResponse deleteContact(Long id) throws Exception {
        contactsDao.delete(id);
        return null;
    }
}
