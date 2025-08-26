package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    @Override
    public void updateContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> searchContacts(String name, String category) {
        if (name != null && category != null) {
            return contactRepository.findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(name, category);
        } else if (name != null) {
            return contactRepository.findByNameContainingIgnoreCase(name);
        } else if (category != null) {
            return contactRepository.findByCategoryContainingIgnoreCase(category);
        } else {
            return getAllContacts();
        }
    }
}
