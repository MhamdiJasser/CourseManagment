package Controllers;

import DAO.CourseDAO;
import DAO.SubmissionDAO;
import Models.Course;
import Models.Submission;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MesCoursRemisesController 
{

    @FXML private ComboBox<Course> coursesComboBox;
    @FXML private TableView<Submission> submissionsTable;
    @FXML private TableColumn<Submission, String> colStudent, colFeedback;
    @FXML private TableColumn<Submission, String> colSubmittedAt;
    @FXML private TableColumn<Submission, Float> colGrade;
    @FXML private TableColumn<Submission, Boolean> colIsLate;
    @FXML private TableColumn<Submission, Void> colActions;

    private final CourseDAO courseDAO = new CourseDAO();
    private final SubmissionDAO submissionDAO = new SubmissionDAO();
    private int currentTeacherNcin;

    public void initialize() 
    {
        Map<String, String> session = Session.loadSession();
        if (session != null && session.containsKey("NCIN")) 
        {
            currentTeacherNcin = Integer.parseInt(session.get("NCIN"));
        }

        List<Course> courses = courseDAO.getCoursesByTeacher(currentTeacherNcin);
        coursesComboBox.setItems(FXCollections.observableArrayList(courses));

        coursesComboBox.setCellFactory(param -> new ListCell<Course>() 
        {
            @Override
            protected void updateItem(Course course, boolean empty) 
            {
                super.updateItem(course, empty);
                if (empty || course == null) 
                {
                    setText(null);
                }
                else {
                    setText(course.getTitle());
                }
            }
        });
        
        coursesComboBox.setButtonCell(new ListCell<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(course.getTitle());
                }
            }
        });

        // Configure table columns (NO colFile)
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colSubmittedAt.setCellValueFactory(cell -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = cell.getValue().getSubmittedAt().format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colFeedback.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        colIsLate.setCellValueFactory(cell -> 
            new SimpleBooleanProperty(cell.getValue().isLate())
        );

        addActionsColumn();
        
        submissionsTable.setRowFactory(tv -> {
            TableRow<Submission> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) 
                {
                    Submission submission = row.getItem();
                    downloadFile(submission);
                }
            });
            return row;
        });

        coursesComboBox.setOnAction(e -> loadSubmissions());
    }

    private void addActionsColumn() {
        Callback<TableColumn<Submission, Void>, TableCell<Submission, Void>> cellFactory = 
            new Callback<TableColumn<Submission, Void>, TableCell<Submission, Void>>() {
            @Override
            public TableCell<Submission, Void> call(final TableColumn<Submission, Void> param) {
                return new TableCell<Submission, Void>() {
                    private final Button gradeBtn = new Button("Noter");
                    private final Button downloadBtn = new Button("Télécharger");
                    private final HBox pane = new HBox(5, gradeBtn, downloadBtn);

                    {
                        gradeBtn.setOnAction(event -> {
                            Submission submission = getTableView().getItems().get(getIndex());
                            gradeSubmission(submission);
                        });
                        
                        downloadBtn.setOnAction(event -> {
                            Submission submission = getTableView().getItems().get(getIndex());
                            downloadFile(submission);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        };
        colActions.setCellFactory(cellFactory);
    }

    private void gradeSubmission(Submission submission) 
    {
        Dialog<Pair<Float, String>> dialog = new Dialog<>();
        dialog.setTitle("Noter le travail");
        dialog.setHeaderText("Noter le travail de: " + submission.getStudentName());

        ButtonType gradeButtonType = new ButtonType("Noter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(gradeButtonType, ButtonType.CANCEL);

        TextField gradeField = new TextField();
        gradeField.setPromptText("Note (0-20)");
        if (submission.getGrade() != null) 
        {
            gradeField.setText(String.valueOf(submission.getGrade()));
        }
        
        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("Feedback");
        feedbackArea.setPrefRowCount(3);
        if (submission.getFeedback() != null) 
        {
            feedbackArea.setText(submission.getFeedback());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        grid.add(new Label("Note:"), 0, 0);
        grid.add(gradeField, 1, 0);
        grid.add(new Label("Feedback:"), 0, 1);
        grid.add(feedbackArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == gradeButtonType) 
            {
                try
                {
                    float grade = Float.parseFloat(gradeField.getText());
                    String feedback = feedbackArea.getText();
                    return new Pair<>(grade, feedback);
                } 
                catch (NumberFormatException e) 
                {
                    showAlert("Erreur", "Note invalide", "Veuillez entrer un nombre valide.");
                    return null;
                }
            }
            return null;
        });

        Optional<Pair<Float, String>> result = dialog.showAndWait();
        result.ifPresent(gradeFeedback -> {
            submission.setGrade(gradeFeedback.getKey());
            submission.setFeedback(gradeFeedback.getValue());
            
            boolean success = submissionDAO.updateSubmissionGrade(submission);
            if (success) 
            {
                submissionsTable.refresh();
                showAlert("Succès", "Mise à jour réussie", 
                         "La note et le feedback ont été enregistrés.");
            } 
            else 
            {
                showAlert("Erreur", "Échec de la mise à jour", 
                         "Impossible de sauvegarder les modifications.");
            }
        });
    }

    private void downloadFile(Submission submission) 
    {
        String filePath = submission.getSubmissionFilePath();
        if (filePath == null || filePath.isEmpty()) 
        {
            showAlert("Erreur", "Fichier non disponible", 
                     "Aucun fichier n'est associé à cette remise.");
            return;
        }

        try 
        {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Enregistrer le fichier");

            String fileName = getFileNameFromPath(filePath);
            fileChooser.setInitialFileName(fileName);

            java.io.File selectedFile = fileChooser.showSaveDialog(submissionsTable.getScene().getWindow());
            
            if (selectedFile != null) 
            {
                java.nio.file.Path source = java.nio.file.Paths.get(filePath);
                java.nio.file.Path destination = selectedFile.toPath();
                java.nio.file.Files.copy(source, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                
                showAlert("Succès", "Téléchargement réussi", 
                         "Le fichier a été enregistré dans: " + selectedFile.getAbsolutePath());
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            showAlert("Erreur", "Échec du téléchargement", 
                     "Impossible de télécharger le fichier: " + e.getMessage());
        }
    }

    private String getFileNameFromPath(String filePath) 
    {
        if (filePath == null || filePath.isEmpty()) 
        {
            return "No file";
        }
        String[] parts = filePath.split("[\\\\/]");
        return parts[parts.length - 1];
    }

    private void showAlert(String title, String header, String content) 
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadSubmissions() 
    {
        Course selectedCourse = coursesComboBox.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) 
        {
            List<Submission> subs = submissionDAO.getSubmissionsByCourse(selectedCourse.getId());
            ObservableList<Submission> data = FXCollections.observableArrayList(subs);
            submissionsTable.setItems(data);
        } 
        else 
        {
            submissionsTable.setItems(FXCollections.observableArrayList());
        }
    }
    
    public class Pair<K, V> 
    {
        private final K key;
        private final V value;
        
        public Pair(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() { return key; }
        public V getValue() { return value; }
    }
}