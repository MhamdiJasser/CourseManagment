package Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class EnseignantDashboardController 
{

    @FXML
    private Label lblUserInfo;

    @FXML
    private StackPane contentArea;

    private static final String LOGIN_FXML = "/View/login.fxml";

    private String username;

    public void setUserData(String username) 
    {
        this.username = username;
        if (lblUserInfo != null) 
        {
            lblUserInfo.setText("Bienvenue, " + username);
        }
    }

    private void loadContent(String fxmlPath) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();  // Parent works with ANY root layout

            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Erreur de chargement du fichier : " + fxmlPath);
        }
    }

    @FXML
    public void handleHome(ActionEvent event) 
    {
        loadContent("/View/EnseignantDashboard.fxml");
    }

    @FXML
    public void handleCourseTypes(ActionEvent event) 
    {
        loadContent("/View/ManageCourseTypes.fxml");
    }

    @FXML
    public void handleAddCourse(ActionEvent event) 
    {
        loadContent("/View/AddCourse.fxml");
    }

    @FXML
    public void handleViewCourses(ActionEvent event) 
    {
        loadContent("/View/EnseignantDashboard.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) 
    {
        try 
        {  
            Session.clearSession();

            Parent root = FXMLLoader.load(getClass().getResource(LOGIN_FXML));
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion Utilisateur");
            stage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

}
