// Package declaration (adjust based on project structure)
package com.quicsearch;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import org.json.*;

public class QuicSearchWithLinks {
    private JFrame mainFrame;
    private JTextField searchInput;
    private JEditorPane searchResults;

    public QuicSearchWithLinks() {
        setupUI();
    }

    private void setupUI() {
        mainFrame = new JFrame("QuicSearch Mini Tab");
        mainFrame.setSize(500, 400);
        mainFrame.setLayout(new BorderLayout());

        // Input field for search queries
        searchInput = new JTextField("Enter your search...");
        searchInput.addActionListener(e -> performSearch(searchInput.getText()));

        // Area to display clickable links
        searchResults = new JEditorPane();
        searchResults.setContentType("text/html");
        searchResults.setEditable(false);

        searchResults.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Failed to open link: " + ex.getMessage());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(searchResults);

        // Add components to the frame
        mainFrame.add(searchInput, BorderLayout.NORTH);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private void performSearch(String query) {
        try {
            // Use mock search results for testing
            String response = generateMockSearchResults(query);

            // Use real search results (uncomment this when ready to use the API)
            // String response = performRealSearch(query);

            // Display results as clickable links
            searchResults.setText(response);

        } catch (Exception e) {
            searchResults.setText("<html><body><p>Error: " + e.getMessage() + "</p></body></html>");
        }
    }

    private String performRealSearch(String query) throws IOException {
        String apiKey = "YOUR_API_KEY";
        String searchEngineId = "YOUR_SEARCH_ENGINE_ID";

        String url = "https://www.googleapis.com/customsearch/v1?q=" +
                     URLEncoder.encode(query, "UTF-8") + "&key=" + apiKey + "&cx=" + searchEngineId;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return parseSearchResults(response.toString());
        }
    }

    private String parseSearchResults(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray items = jsonObject.getJSONArray("items");
        StringBuilder html = new StringBuilder("<html><body><ul style='font-family: Arial; font-size: 14px;'>");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title");
            String link = item.getString("link");
            html.append("<li><a href='").append(link).append("'>").append(title).append("</a></li>");
        }

        html.append("</ul></body></html>");
        return html.toString();
    }

    // Added mock search results for testing
    private String generateMockSearchResults(String query) {
        return "<html><body>" +
               "<h2>Search Results for '" + query + "'</h2>" +
               "<ul>" +
               "<li><a href='https://example.com/result1'>Result 1</a></li>" +
               "<li><a href='https://example.com/result2'>Result 2</a></li>" +
               "<li><a href='https://example.com/result3'>Result 3</a></li>" +
               "</ul>" +
               "</body></html>";
    }

    public static void main(String[] args) {
        new QuicSearchWithLinks();
    }
}
