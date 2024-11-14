// Package declaration (adjust based on project structure)

// Import necessary libraries for browser integration, UI, and HTTP requests
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.jsoup.*;
import org.jsoup.nodes.*;

// Main class for the browser extension
public class QuicSearch {
    // Main UI components
    private JFrame mainFrame;
    private JTextField searchInput;
    private JTextArea searchResults;
    // private PlaceholderText pT;
    JButton button;
    // Document results;

    // Constructor for setting up the UI
    public QuicSearch() {
        setupUI();
    }

    // Method to initialize the UI
    private void setupUI() {
        mainFrame = new JFrame("QuicSearch Mini Tab");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 500);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setResizable(true);

        // Defining panel for search features
        JPanel sPanel = new JPanel();
        sPanel.setPreferredSize(new Dimension(400, 40));
        sPanel.setBackground(new Color(0x000328));

        // Defining panel for search results
        JPanel rPanel = new JPanel();
        rPanel.setPreferredSize(new Dimension(400, 420));
        sPanel.setBackground(new Color(0x00063f));

        // Search button
        button = new JButton("Search");
        button.setPreferredSize(new Dimension(100, 30));
        button.setFont(new Font("Consolas", Font.PLAIN, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchInput.getText());
            }

            private void performSearch(String text) {
                throw new UnsupportedOperationException("Unimplemented method 'performSearch'");
            }
        });

        // Input field for search queries
        searchInput = new JTextField();
        searchInput.setPreferredSize(new Dimension(250, 30));
        searchInput.setFont(new Font("Consolas", Font.PLAIN, 16));
        searchInput.setBackground(Color.LIGHT_GRAY);
        searchInput.setText("What's on your mind?");

        searchInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchInput.getText());
            }

            private void performSearch(String text) {
                throw new UnsupportedOperationException("Unimplemented method 'performSearch'");
            }
        });

        // Area to display search results
        searchResults = new JTextArea();
        searchResults.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(searchResults);

        // Adding search components to panes
        sPanel.add(searchInput);
        sPanel.add(button);
        rPanel.add(searchResults);

        // Adding components to the frame
        mainFrame.add(sPanel);
        mainFrame.add(rPanel);

        // mainFrame.pack();

        // Placeholder ***Text NEEDS FIX***
        // pT = new PlaceholderText(" ");
        // pT.setPreferredSize(new Dimension(100, 50));
        // pT.setPlaceholder("Enter Search here...");
        // pT.setBackground(Color.gray);
        // mainFrame.add(pT);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public File webToFile(String url) {
        try {

            // New url to AI
            URL chtGPT = new URL(url);

            // Start an Input stream
            URLConnection conn = chtGPT.openConnection();
            InputStream iStream = conn.getInputStream();

            // Read bytes from website
            try (BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream))) {
                String ln = null;
                // Create result file
                File results = new File("Search-Results.txt");

                // Write to file
                FileWriter myWriter = new FileWriter(results);

                while ((ln = bReader.readLine()) != null) {
                    System.out.println(ln);
                    myWriter.write(ln);
                }
                myWriter.close();
                return results;
            }

        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException");
            e.printStackTrace();
            return null;
        } catch (IOException q) {
            System.err.println("IOException");
            q.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        QuicSearch q = new QuicSearch();
        // String html = "<html><head><title>First parse</title></head>"
        // + "<body><p>Parsed HTML into a doc.<p></body></html>";

        // Get html from webpage
        File newFile = q.webToFile("https://www.google.com/");

        // Turn file into document editable with Jsoup
        org.jsoup.nodes.Document doc = Jsoup.parse(newFile);

        System.out.println(doc.getElementsByTag("title").get(0).text());
    }
}