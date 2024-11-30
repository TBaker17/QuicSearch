// Package declaration (adjust based on project structure)

// Import necessary libraries for browser integration, UI, and HTTP requests
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.*;
import org.jsoup.nodes.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

// Main class for the browser extension
public class QuicSearch {
    // Main UI components
    private JFrame mainFrame;
    private JTextField searchInput;
    private JEditorPane searchResults;
    private JButton button;
    String res = "";
    private JLayeredPane tPane;
    private JLayeredPane bPane;
    private JLayeredPane cPane;
    private JPanel rPanel;
    private String user = "";

    // Constructor for setting up the UI
    public QuicSearch() {

        setupUI();
    }

    // Method to initialize the UI
    private void setupUI() {
        mainFrame = new JFrame("QuicSearch Mini Tab");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 500);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setResizable(true);

        // Setting Icon
        ImageIcon icon = new ImageIcon("QuicSearch.png");
        mainFrame.setIconImage(icon.getImage());

        // Defining panel for search features
        JPanel sPanel = new JPanel();
        sPanel.setPreferredSize(new Dimension(400, 40));
        sPanel.setBackground(new Color(0x000328));

        // Defining panel for search results
        rPanel = new JPanel();
        rPanel.setPreferredSize(new Dimension(500, 400));
        sPanel.setBackground(new Color(0x00063f));
        mainFrame.setResizable(true);

        // Search button
        button = new JButton("Search");
        button.setPreferredSize(new Dimension(100, 30));
        button.setFont(new Font("Consolas", Font.PLAIN, 16));
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchInput.getText());
            }

            private void performSearch(String text) {
                try {
                    // Stores the search query in the database
                    performDBAdd(text);

                    // Performs actual search
                    search(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // updateSearchResultsPanel();
                rPanel.revalidate();
                sPanel.revalidate();
                bPane.revalidate();
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        // Input field for search queries
        searchInput = new JTextField("What's on your mind?");
        searchInput.setPreferredSize(new Dimension(250, 30));
        searchInput.setFont(new Font("Consolas", Font.PLAIN, 16));
        searchInput.setBackground(Color.LIGHT_GRAY);
        // if (user.equalsIgnoreCase("")) {
        // searchInput.setText("What's on your mind?");
        // } else {
        // searchInput.setText("What's on your mind" + user + "?"); GRABS FROM DB A NAME
        // ?!?
        // }

        searchInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchInput.getText());
            }

            private void performSearch(String text) {
                try {
                    search(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rPanel.revalidate();
                sPanel.revalidate();
                bPane.revalidate();
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        // Area to display search results
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

        // Adding search components to panels
        sPanel.add(searchInput);
        sPanel.add(button);
        rPanel.add(searchResults);

        // Initializing panes

        tPane = new JLayeredPane();
        cPane = new JLayeredPane();
        bPane = new JLayeredPane();

        tPane.setBounds(300, 0, 350, 30);
        sPanel.setOpaque(true);
        tPane.setLayout(new BorderLayout());
        tPane.setLayer(sPanel, 1);
        tPane.add(sPanel);

        cPane.setBounds(300, 300, 500, 400);
        rPanel.setOpaque(true);
        cPane.setLayout(new BorderLayout());
        cPane.setLayer(rPanel, 0);
        cPane.add(rPanel);

        bPane.setLayout(new BorderLayout());
        var quiet = new JLabel("Powered By Google...for now...");
        bPane.setLayer(quiet, -1);

        // Adding components to the frame
        mainFrame.add(tPane, BorderLayout.NORTH);
        mainFrame.add(cPane, BorderLayout.CENTER);
        mainFrame.add(cPane, BorderLayout.SOUTH);

        // Placeholder Text ***NEEDS FIX***
        // pT = new PlaceholderText(" ");
        // pT.setPreferredSize(new Dimension(100, 50));
        // pT.setPlaceholder("Enter Search here...");
        // pT.setBackground(Color.gray);
        // mainFrame.add(pT);

        mainFrame.setResizable(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        popUp();
    }

    private void popUp() {
        final JFrame parent = new JFrame();
        String name;

        name = JOptionPane.showInputDialog(parent, "What is your name?", null);
        user = name;
        parent.pack();
        parent.setVisible(true);

    }

    // Function to search through a database
    private void performDBAdd(String query) {
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

    // Function to display saved search queries
    private void displaySavedQueries(MongoCollection<Document> collection) {
        StringBuilder results = new StringBuilder("Saved Search Queries:\n");
        for (Document savedQuery : collection.find()) {
            results.append("- ").append(savedQuery.getString("searchQuery")).append("\n");
        }
        results.append("<ul></body></html>");
        searchResults.setText(results.toString());
    }

    // Method to update the search results panel
    private void updateSearchResults() {
        rPanel.revalidate();
        bPane.revalidate();
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Function to search through google
    public void search(String query) throws IOException {

        res = "";

        // Format query in case of existing spaces
        Pattern space = Pattern.compile(" ");
        Matcher sMatch = space.matcher(query);

        if (sMatch.find()) {
            query = sMatch.replaceAll("+");
        }

        System.out.println(query);

        // Store resulting page into a file for manipualtion
        // File resFile = q.webToFile("https://www.google.com/search?q=" + query);

        var resDoc = Jsoup.connect("https://www.google.com/search?q=" + query).get();
        var names = resDoc.getElementsByClass("LC20lb MBeuO DKV0Md");
        // var msc = resDoc.getElementsByClass("qLRx3b tjvcx GvPZzd cHaqb");
        var links0 = resDoc.getElementsByClass("yuRUbf");

        int i = 1;
        int j = 0;
        res += "<html><body> + <h2>Top Results:</h2> + <ul>";
        for (Element n : links0) {

            res += "<li>";

            System.out.println(n.getElementsByTag("a").attr("href"));
            res += "<a href= '" + n.getElementsByTag("a").attr("href") + "'>";

            System.out.print(i + ". " + names.get(j).text());
            res += i + ". " + names.get(j).text();
            i++;

            // System.out.println(" @ " + msc.get(j).text());
            // res += " @ " + msc.get(j).text();

            // System.out.println(names.get(j).getElementsByTag("a").attr("href"));

            j++;

            // rPanel.add(new JLabel("" + res));
            // Add results to result pane
            searchResults.setText(res);

            // rPanel.repaint();
            // rPanel.add(new JLabel("Yeah..."));
            // Refresh panel so that results are visible
            rPanel.revalidate();
            bPane.revalidate();

            if (j == links0.size()) {
                res += "</a></li>" + "</ul>" + "</body></html>";
            }
        }

        rPanel.revalidate();
        bPane.revalidate();

    }

    public static void main(String[] args) throws IOException {
        QuicSearch q = new QuicSearch();
    }
}