// Package declaration (adjust based on project structure)

// Import necessary libraries for browser integration, UI, and HTTP requests
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private JButton button;
    String res = "";

    private JPanel rPanel;

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
        rPanel.setPreferredSize(new Dimension(500, 450));
        sPanel.setBackground(new Color(0x00063f));
        mainFrame.setResizable(true);

        // Defining another panel for the lols
        JPanel truePanel = new JPanel();
        truePanel.add(new JLabel("Powered Bu Google...for now..."));

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
                    search(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rPanel.revalidate();
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
                try {
                    search(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rPanel.revalidate();
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
        mainFrame.add(sPanel, BorderLayout.NORTH);
        mainFrame.add(rPanel, BorderLayout.SOUTH);
        // mainFrame.add(truePanel, BorderLayout.SOUTH);

        // Placeholder Text ***NEEDS FIX***
        // pT = new PlaceholderText(" ");
        // pT.setPreferredSize(new Dimension(100, 50));
        // pT.setPlaceholder("Enter Search here...");
        // pT.setBackground(Color.gray);
        // mainFrame.add(pT);

        mainFrame.setResizable(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public void search(String query) throws IOException {

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
        var sites = resDoc.getElementsByClass("qLRx3b tjvcx GvPZzd cHaqb");
        int i = 1;
        int j = 0;
        for (Element n : names) {

            System.out.print(i + ". " + n.text());
            res += i + ". " + n.text();
            i++;
            System.out.println("@ " + sites.get(j).text());
            res += "@ " + sites.get(j).text() + "\n";
            j++;

            // rPanel.add(new JLabel("" + res));
            // Add results to result pane
            searchResults.setText(res);

            // rPanel.repaint();
            // rPanel.add(new JLabel("Yeah..."));
            // Refresh panel so that results are visible
            rPanel.revalidate();
        }
        rPanel.revalidate();

    }

    public static void main(String[] args) throws IOException {
        QuicSearch q = new QuicSearch();
    }
}