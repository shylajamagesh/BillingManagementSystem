package com.example.demo.repository;

import com.example.demo.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByNameContainingIgnoreCase(String name);

    List<Contact> findByCategoryContainingIgnoreCase(String category);

    List<Contact> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String name, String category);
}
