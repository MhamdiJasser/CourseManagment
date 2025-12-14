package Controllers;

import DAO.CourseTypeDAO;
import Models.CourseType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ManageCourseTypesController implements Initializable 
{

    @FXML private TextField nameField;
    @FXML private CheckBox requiresSubmissionCheckbox;
    @FXML private Button btnSave;
    @FXML private TableView<CourseType> courseTypeTable;
    @FXML private TableColumn<CourseType, Integer> colId;
    @FXML private TableColumn<CourseType, String> colName;
    @FXML private TableColumn<CourseType, Boolean> colRequiresSubmission;
    @FXML private TableColumn<CourseType, Void> colActions;

    private final CourseTypeDAO courseTypeDAO = new CourseTypeDAO();
    private ObservableList<CourseType> courseTypes;
    private CourseType selectedType = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRequiresSubmission.setCellValueFactory(new PropertyValueFactory<>("requiresSubmission"));
       
        colRequiresSubmission.setCellFactory(column -> new TableCell<CourseType, Boolean>() 
        {
            @Override
            protected void updateItem(Boolean item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) 
                {
                    setText(null);
                } 
                else 
                {
                    setText(item ? "Oui" : "Non");
                }
            }
        });

        addActionsColumn();
        
        courseTypeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) 
            {
                selectedType = newSelection;
                nameField.setText(selectedType.getName());
                requiresSubmissionCheckbox.setSelected(selectedType.isRequiresSubmission());
                btnSave.setText("Modifier");
            }
        });
        
        loadCourseTypes();
    }
    
    private void loadCourseTypes() 
    {
        courseTypes = FXCollections.observableArrayList(courseTypeDAO.getAll());
        courseTypeTable.setItems(courseTypes);
    }
    
    private void addActionsColumn() 
    {
        colActions.setCellFactory(param -> new TableCell<CourseType, Void>()
        {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox pane = new HBox(5, editButton, deleteButton);
            
            {
                editButton.getStyleClass().addAll("primary-button", "small-button");
                deleteButton.getStyleClass().addAll("sidebar-button-logout", "small-button");
                pane.setAlignment(Pos.CENTER);
                
                editButton.setOnAction((ActionEvent event) -> {
                    CourseType type = getTableView().getItems().get(getIndex());
                    courseTypeTable.getSelectionModel().select(type);
                });
                
                deleteButton.setOnAction((ActionEvent event) -> {
                    CourseType type = getTableView().getItems().get(getIndex());
                    confirmAndDelete(type);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) 
            {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void handleSave(ActionEvent event) 
    {
        String name = nameField.getText().trim();
        boolean requiresSubmission = requiresSubmissionCheckbox.isSelected();
        
        if (name.isEmpty()) 
        {
            showAlert(AlertType.WARNING, "Champ manquant", "Veuillez entrer le nom du type de cours.");
            return;
        }

        boolean success;
        if (selectedType == null) 
        {
            CourseType newType = new CourseType(name, requiresSubmission);
            success = courseTypeDAO.add(newType);
            if (success) 
            {
                showAlert(AlertType.INFORMATION, "Succès", "Type de cours ajouté.");
            }
        } 
        else 
        {
            selectedType.setName(name);
            selectedType.setRequiresSubmission(requiresSubmission);
            success = courseTypeDAO.update(selectedType);
            if (success) 
            {
                showAlert(AlertType.INFORMATION, "Succès", "Type de cours modifié.");
            }
        }

        if (success) 
        {
            handleClearSelection(null);
            loadCourseTypes();
        } 
        else 
        {
            showAlert(AlertType.ERROR, "Erreur DB", "Une erreur est survenue lors de l'enregistrement.");
        }
    }

    @FXML
    private void handleClearSelection(ActionEvent event) 
    {
        selectedType = null;
        nameField.clear();
        requiresSubmissionCheckbox.setSelected(false);
        btnSave.setText("Enregistrer");
        courseTypeTable.getSelectionModel().clearSelection();
    }
 
    private void confirmAndDelete(CourseType type) 
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Suppression");
        alert.setHeaderText("Supprimer le type de cours : " + type.getName());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce type de cours ? Cette action est irréversible et échouera s'il est utilisé par des cours existants.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) 
            {
                if (courseTypeDAO.delete(type.getId())) 
                {
                    showAlert(AlertType.INFORMATION, "Succès", "Type de cours supprimé.");
                    loadCourseTypes();
                } 
                else 
                {
                    showAlert(AlertType.ERROR, "Erreur de Suppression", "Impossible de supprimer ce type de cours. Assurez-vous qu'aucun cours ne l'utilise.");
                }
            }
        });
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