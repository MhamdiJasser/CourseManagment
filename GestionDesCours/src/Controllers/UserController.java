package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import Application.DatabaseConnection;
import Controllers.Session;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class UserController implements Initializable 
{

    @FXML private TextField ncinField;
    @FXML private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {

        Map<String, String> session = Session.loadSession();
        if (session != null) 
        {
            String role = session.get("role");
            String username = session.get("username");
            System.out.println("Session trouvée : role=" + role + ", username=" + username);

            Platform.runLater(() -> {
                Stage stage = (Stage) ncinField.getScene().getWindow();
                redirectUser(role, stage, username);
            });
        }
    }

    @FXML
    private void onLogin(ActionEvent event) 
    {
        String ncinText = ncinField.getText().trim();
        String password = passwordField.getText().trim();

        if (ncinText.isEmpty() || password.isEmpty()) 
        {
            showAlert(AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        int ncin;
        try 
        {
            ncin = Integer.parseInt(ncinText);
        } 
        catch (NumberFormatException e) 
        {
            showAlert(AlertType.ERROR, "Format incorrect", "Le NCIN doit contenir uniquement des chiffres.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) 
        {
            String sql = "SELECT NCIN, role, full_name FROM users WHERE NCIN = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ncin);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) 
            {
                String role = rs.getString("role");
                String username = rs.getString("full_name");

                Session.saveSession(ncin, role, username);

                showAlert(AlertType.INFORMATION, "Succès", "Connexion réussie en tant que " + role);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                redirectUser(role, stage, username);

            } 
            else 
            {
                showAlert(AlertType.ERROR, "Erreur", "NCIN ou mot de passe incorrect.");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la connexion: " + e.getMessage());
        }
    }

    private void redirectUser(String role, Stage stage, String username) 
    {
        if (stage == null) 
        {
            System.err.println("redirectUser(): Stage is NULL. Redirect aborted.");
            return;
        }

        String fxmlFile = switch (role.toLowerCase()) {
            case "etudiant" -> "/View/StudentDashboard.fxml";
            case "enseignant" -> "/View/EnseignantDashboard.fxml";
            default -> null;
        };

        if (fxmlFile == null) 
        {
            System.out.println("Rôle inconnu — reste sur la page de connexion.");
            return;
        }

        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (role.equalsIgnoreCase("enseignant")) 
            {
                EnseignantDashboardController controller = loader.getController();
                controller.setUserData(username);
            }

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Plateforme E-Learning - " +
                    (role.equalsIgnoreCase("enseignant") ? "Enseignant" : "Étudiant"));
            stage.show();

        } catch (IOException e) 
        {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur de Navigation", "Impossible de charger l'interface utilisateur.");
        }
    }

    private void showAlert(AlertType type, String title, String message) 
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
