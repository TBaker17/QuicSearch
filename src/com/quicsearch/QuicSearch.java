// Package declaration (adjust based on project structure)
package com.quicsearch;

// Import necessary libraries for browser integration, UI, and HTTP requests
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
// Main class for the browser extension
public class QuicSearch {
    private JFrame mainFrame;
    private JTextField searchInput;
    private JTextArea searchResults;

    public QuicSearch() {
        setupUI();
    }

    private void setupUI() {
        mainFrame = new JFrame("QuicSearch Mini Tab");
        mainFrame.setSize(400, 300);
        mainFrame.setLayout(new BorderLayout());

        searchInput = new JTextField("Enter your search...");
        searchInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchInput.getText());
            }
        });

        searchResults = new JTextArea();
        searchResults.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(searchResults);

        mainFrame.add(searchInput, BorderLayout.NORTH);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private void performSearch(String query) {
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("QuicSearchDB");
            MongoCollection<Document> collection = database.getCollection("SearchQueries");

            Document doc = new Document("searchQuery", query)
                    .append("timestamp", System.currentTimeMillis());
            collection.insertOne(doc);

            StringBuilder results = new StringBuilder("Search Results:\n");
            for (Document savedQuery : collection.find()) {
                results.append("- ").append(savedQuery.getString("searchQuery")).append("\n");
            }
            searchResults.setText(results.toString());
        } catch (Exception e) {
            e.printStackTrace();
            searchResults.setText("An error occurred while accessing the database.");
        }
    }

    public static void main(String[] args) {
        new QuicSearch();
    }
}
