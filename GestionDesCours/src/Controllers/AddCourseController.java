package Controllers;

import DAO.CourseDAO;
import DAO.CourseTypeDAO;
import DAO.MatiereDAO;
import Models.Course;
import Models.CourseType;
import Models.Matiere;
import Controllers.Session;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;

public class AddCourseController implements Initializable 
{

    @FXML private TextField titleField;
    @FXML private ComboBox<CourseType> typeComboBox;
    @FXML private ComboBox<Matiere> matiereComboBox;
    @FXML private TextField filePathField;
    @FXML private DatePicker deadlinePicker;
    @FXML private Label deadlineLabel;
    @FXML private Label lblStatus;
   
    private final CourseDAO courseDAO = new CourseDAO();
    private final CourseTypeDAO typeDAO = new CourseTypeDAO();
    private int currentTeacherNcin;
    
    private String selectedFilePath = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
        Map<String, String> session = Session.loadSession();
        if (session != null && session.containsKey("NCIN")) 
        {
            currentTeacherNcin = Integer.parseInt(session.get("NCIN"));
        } 
        else 
        {
            showAlert(Alert.AlertType.ERROR, "Erreur de Session", "Veuillez vous reconnecter.");
            return;
        }

        loadCourseTypes();
        loadMatieres();
        
        deadlinePicker.setVisible(false);
        deadlineLabel.setVisible(false);

        typeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldType, newType) -> {
            boolean isRequired = newType != null && newType.isRequiresSubmission();
            deadlinePicker.setVisible(isRequired);
            deadlineLabel.setVisible(isRequired);
        });
    }
    
    private void loadCourseTypes() 
    {
        List<CourseType> types = typeDAO.getAll();
        typeComboBox.setItems(FXCollections.observableArrayList(types));
    }
    
    private void loadMatieres() 
    {
        List<Matiere> matieres = null;
		try 
		{
			matieres = MatiereDAO.getAllMatieres();
		} 
		catch (Exception e) 
		{
		
			e.printStackTrace();
		}
        matiereComboBox.setItems(FXCollections.observableArrayList(matieres));
    }

    @FXML
    private void handleSelectFile(ActionEvent event) 
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner le fichier du cours");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.doc", "*.docx", "*.txt"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null)
        {
            selectedFilePath = selectedFile.getAbsolutePath();
            filePathField.setText(selectedFile.getName());
        } 
        else 
        {
            filePathField.setText("Aucun fichier sélectionné");
            selectedFilePath = null;
        }
    }

    @FXML
    private void handleSaveCourse(ActionEvent event) 
    {
        String title = titleField.getText().trim();
        Matiere selectedMatiere = matiereComboBox.getSelectionModel().getSelectedItem();
        CourseType selectedType = typeComboBox.getSelectionModel().getSelectedItem();
        
        if (title.isEmpty() || selectedMatiere == null || selectedType == null) 
        {
            showAlert(Alert.AlertType.WARNING, "Champs Incomplets", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        if (selectedFilePath == null || selectedFilePath.isEmpty()) 
        {
            showAlert(Alert.AlertType.WARNING, "Fichier Manquant", "Veuillez sélectionner le fichier de cours.");
            return;
        }

        LocalDateTime deadline = null;
        if (selectedType.isRequiresSubmission()) 
        {
            LocalDate date = deadlinePicker.getValue();
            if (date == null) 
            {
                showAlert(Alert.AlertType.WARNING, "Deadline Manquante", "Ce type de cours nécessite une date limite de remise.");
                return;
            }
            deadline = date.atTime(LocalTime.MAX);
        }

        Course newCourse = new Course(title, selectedMatiere, selectedType, currentTeacherNcin, selectedFilePath, deadline);
        if (courseDAO.add(newCourse)) 
        {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le cours '" + title + "' a été publié avec succès!");
            handleClearForm(null);
        } 
        else 
        {
            showAlert(Alert.AlertType.ERROR, "Erreur DB", "Impossible d'enregistrer le cours. Vérifiez la connexion.");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) 
    {
        titleField.clear();
        matiereComboBox.getSelectionModel().clearSelection();
        typeComboBox.getSelectionModel().clearSelection();
        filePathField.clear();
        deadlinePicker.setValue(null);
        selectedFilePath = null;
        
        deadlinePicker.setVisible(false);
        deadlineLabel.setVisible(false);
    }

    private void showAlert(Alert.AlertType type, String title, String message) 
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
