package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    private ContactRepository contactRepository;

    // Export contacts to CSV
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Contact> contacts = contactRepository.findAll();

        // Set the response headers to prompt a download
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=contacts.csv");

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(response.getOutputStream()))) {
            // Write header to CSV
            writer.writeNext(new String[] { "ID", "Name", "Phone", "Email", "Category" });

            // Write contact data to CSV
            for (Contact contact : contacts) {
                writer.writeNext(new String[] {
                        contact.getId().toString(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory()
                });
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception properly
        }
    }

    // Import contacts from CSV

    public void importFromCsv(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) { // Using
                                                                                               // InputStreamReader with
                                                                                               // getInputStream()
            String[] nextLine;
            reader.skip(1); // Skip header row
            while ((nextLine = reader.readNext()) != null) {
                // Create new contact and set the fields from the CSV
                Contact contact = new Contact();
                contact.setName(nextLine[0]); // Assuming column 0 is Name
                contact.setPhone(nextLine[1]); // Assuming column 1 is Phone
                contact.setEmail(nextLine[2]); // Assuming column 2 is Email
                contact.setCategory(nextLine[3]); // Assuming column 3 is Category
                // Save contact to repository
                contactRepository.save(contact);
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace(); // Handle or log the exception properly
        }
    }
}
