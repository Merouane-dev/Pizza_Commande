package directpizzam1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class PizzaOrderApp {

    private static Connection cn = null;
    private static Statement st = null;

    public static void main(String[] args) {
        // Connexion à la base de données
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/pizza";
            String login = "root";
            String passwd = "";
            cn = DriverManager.getConnection(url, login, passwd);
            st = cn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return;
        }

        // Créer la fenêtre principale
        JFrame frame = new JFrame("Commande de Pizza");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Liste pour afficher les pizzas
        DefaultListModel<String> pizzaListModel = new DefaultListModel<>();
        JList<String> pizzaList = new JList<>(pizzaListModel);
        JScrollPane pizzaScrollPane = new JScrollPane(pizzaList);
        panel.add(pizzaScrollPane, BorderLayout.WEST);

        // Charger les pizzas depuis la base de données
        try {
            String sql = "SELECT * FROM pizza";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String pizzaName = rs.getString("DESIGNPIZZ");
                double price = rs.getDouble("TARIFPIZZ");
                pizzaListModel.addElement(pizzaName + " - Tarif : " + price + "€");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Zone pour afficher les ingrédients de la pizza sélectionnée
        JPanel ingredientPanel = new JPanel();
        ingredientPanel.setLayout(new BorderLayout());
        JLabel ingredientLabel = new JLabel("Ingrédients de la pizza :");
        JTextArea ingredientArea = new JTextArea();
        ingredientArea.setEditable(false);
        JScrollPane ingredientScrollPane = new JScrollPane(ingredientArea);
        ingredientPanel.add(ingredientLabel, BorderLayout.NORTH);
        ingredientPanel.add(ingredientScrollPane, BorderLayout.CENTER);

        panel.add(ingredientPanel, BorderLayout.CENTER);

        // Listener pour afficher les ingrédients lorsqu'une pizza est sélectionnée
        pizzaList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPizza = pizzaList.getSelectedValue();
                if (selectedPizza != null) {
                    String pizzaName = selectedPizza.split(" - ")[0];
                    try {
                        String ingredientSql = "SELECT i.CODEINGR FROM ingredient i " +
                                "JOIN composer c ON i.CODEINGR = c.CODEINGR " +
                                "JOIN pizza p ON c.NROPIZZ = p.NROPIZZ " +
                                "WHERE p.DESIGNPIZZ = '" + pizzaName + "'";
                        ResultSet rs = st.executeQuery(ingredientSql);
                        StringBuilder ingredients = new StringBuilder();
                        while (rs.next()) {
                            ingredients.append("- ").append(rs.getString("CODEINGR")).append("\n");
                        }
                        ingredientArea.setText(ingredients.toString());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ingredientArea.setText("Erreur lors de la récupération des ingrédients.");
                    }
                } else {
                    ingredientArea.setText("");
                }
            }
        });

        // Section droite pour gérer les clients
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout());

        // Panel pour les boutons "Afficher" et "Ajouter un client"
        JPanel clientButtonPanel = new JPanel();
        clientButtonPanel.setLayout(new FlowLayout());

        JButton showClientsButton = new JButton("Afficher les clients fidèles");
        JButton addClientButton = new JButton("Ajouter un nouveau client");

        clientButtonPanel.add(showClientsButton);
        clientButtonPanel.add(addClientButton);

        clientPanel.add(clientButtonPanel, BorderLayout.NORTH);

        // Liste pour afficher les clients
        DefaultListModel<String> clientListModel = new DefaultListModel<>();
        JList<String> clientList = new JList<>(clientListModel);
        JScrollPane clientScrollPane = new JScrollPane(clientList);
        clientPanel.add(clientScrollPane, BorderLayout.CENTER);

        // Charger les clients depuis la base de données
        showClientsButton.addActionListener(e -> {
            clientListModel.clear();
            try {
                String sql = "SELECT * FROM client";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    String clientInfo = rs.getString("NOMCLIE") + " " +
                                        rs.getString("PRENOMCLIE") + " - " +
                                        rs.getString("VILLECLIE") + ", " +
                                        rs.getString("TITRECLIE");
                    clientListModel.addElement(clientInfo);
                }
                JOptionPane.showMessageDialog(frame, "Liste des clients fidèles mise à jour.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des clients.");
            }
        });

        // Ajouter un nouveau client
        addClientButton.addActionListener(e -> {
            JTextField nomField = new JTextField();
            JTextField prenomField = new JTextField();
            JTextField adresseField = new JTextField();
            JTextField villeField = new JTextField();
            JTextField codePostalField = new JTextField();
            JTextField titreField = new JTextField();
            JTextField telField = new JTextField();

            Object[] message = {
                "Nom:", nomField,
                "Prénom:", prenomField,
                "Adresse:", adresseField,
                "Ville:", villeField,
                "Code Postal:", codePostalField,
                "Titre (M., Mme, etc.):", titreField,
                "Numéro de Téléphone:", telField
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Ajouter un client", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String adresse = adresseField.getText();
                String ville = villeField.getText();
                String codePostal = codePostalField.getText();
                String titre = titreField.getText();
                String tel = telField.getText();

                if (!nom.isEmpty() && !prenom.isEmpty() && !adresse.isEmpty() && !ville.isEmpty() && !codePostal.isEmpty() && !titre.isEmpty()) {
                    try {
                        String insertSql = "INSERT INTO client (NOMCLIE, PRENOMCLIE, ADRESSECLIE, VILLECLIE, CODEPOSTALCLIE, TITRECLIE, NROTELCLIE) " +
                                           "VALUES ('" + nom + "', '" + prenom + "', '" + adresse + "', '" + ville + "', '" + codePostal + "', '" + titre + "', '" + tel + "')";
                        st.executeUpdate(insertSql);
                        JOptionPane.showMessageDialog(frame, "Nouveau client ajouté avec succès !");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du nouveau client.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Tous les champs (sauf téléphone) doivent être remplis.");
                }
            }
        });

        panel.add(clientPanel, BorderLayout.EAST);

        // Ajouter le panel à la fenêtre
        frame.add(panel);
        frame.setVisible(true);
    }
}
