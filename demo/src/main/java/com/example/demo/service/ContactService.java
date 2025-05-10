package com.example.demo.service;

import com.example.demo.model.Contact;
import java.util.List;

public interface ContactService {
    void addContact(Contact contact);

    List<Contact> getAllContacts();

    Contact getContactById(Long id);

    void updateContact(Contact contact);

    void deleteContact(Long id);

    List<Contact> searchContacts(String name, String category);
}
