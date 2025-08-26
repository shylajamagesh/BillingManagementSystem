package com.example.demo.controller;

import com.example.demo.model.Contact;
import com.example.demo.service.ContactService;
import com.example.demo.service.CsvService; // Import CSV service

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private CsvService csvService; // Inject CSV service

    // Home page
    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    // View all contacts
    @GetMapping("/contacts")
    public String viewContacts(Model model) {
        List<Contact> contacts = contactService.getAllContacts();
        model.addAttribute("contacts", contacts);
        return "view-contacts";
    }

    // Show add contact form
    @GetMapping("/contacts/add")
    public String showAddContactForm() {
        return "add-contact";
    }

    // Add new contact
    @PostMapping("/contacts/add")
    public String addContact(@ModelAttribute Contact contact) {
        contactService.addContact(contact);
        return "redirect:/contacts";
    }

    // Show edit form
    @GetMapping("/contacts/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "edit-contact";
    }

    // Update contact
    @PostMapping("/contacts/update")
    public String updateContact(@ModelAttribute Contact contact) {
        contactService.updateContact(contact);
        return "redirect:/contacts";
    }

    // Delete contact
    @GetMapping("/contacts/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/contacts";
    }

    // Show search form
    @GetMapping("/contacts/search")
    public String showSearchForm() {
        return "search-by";
    }

    // Handle search
    @GetMapping("/contacts/search/results")
    public String searchContacts(@RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            Model model) {
        List<Contact> results = contactService.searchContacts(name, category);
        model.addAttribute("contacts", results);
        return "view-contacts";
    }

    @GetMapping("/contacts/export")
    public void exportContacts(HttpServletResponse response) throws IOException {
        // Set the content type to CSV
        response.setContentType("text/csv");

        // Set the content-disposition header to trigger a file download
        response.setHeader("Content-Disposition", "attachment; filename=contacts.csv");

        // Call the service to generate and send the CSV file to the response output
        // stream
        csvService.exportToCsv(response);
    }

    // Import contacts from CSV
    // Import contacts from CSV (POST method)
    @PostMapping("/contacts/import")
    public String importContacts(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        // Check if the file is empty
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a valid CSV file.");
            return "redirect:/contacts"; // Redirect to the contacts page if file is empty
        }

        // Pass the file to CsvService for processing
        csvService.importFromCsv(file);

        model.addAttribute("message", "Contacts imported successfully!");
        return "redirect:/contacts"; // Redirect to contacts page after import
    }

}
