package com.crm.contacts.controllers;

import com.crm.contacts.dtos.ContactsRequestResponse;
import com.crm.contacts.services.IContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/contacts")
public class ContactsController {

    @Autowired
    private IContactsService contactsService;

    @GetMapping("/all")
    public List<ContactsRequestResponse> fetchAll(
            @RequestParam("firstName") @Nullable String firstName,
            @RequestParam("lastName") @Nullable String lastName,
            @RequestParam("email") @Nullable String email) throws Exception {
        return contactsService.fetchAll(firstName, lastName, email);
    }

    @PutMapping
    public ContactsRequestResponse save(@RequestBody ContactsRequestResponse contactsRequestResponse) throws Exception {
        return contactsService.saveContact(contactsRequestResponse);
    }

    @PostMapping
    public ContactsRequestResponse update(@RequestBody ContactsRequestResponse contactsRequestResponse) throws Exception {
        return contactsService.updateContact(contactsRequestResponse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws Exception {
        contactsService.deleteContact(id);
    }
}
