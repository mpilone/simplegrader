package org.mpilone.simplegrader.controller;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import org.mpilone.simplegrader.domain.ReviewComment;
import org.mpilone.simplegrader.util.ReportBuilder;
import org.mpilone.simplegrader.util.ReviewCommentSerializer;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * The controller for the main window of the application.
 *
 * @author mpilone
 */
public class MainWindowController implements Initializable {
  public static final String TITLE = "Simple Grader";

  @FXML
  private MenuBar mainMenuBar;
  @FXML
  private TableView<ReviewComment> catalogTable;
  @FXML
  private TableColumn<ReviewComment, String> catalogCategoryCol;
  @FXML
  private TableColumn<ReviewComment, String> catalogCommentCol;
  @FXML
  private TableColumn<ReviewComment, Number> catalogPointsCol;
  @FXML
  private Button catalogNewBtn;
  @FXML
  private Button catalogEditBtn;
  @FXML
  private Button catalogDeleteBtn;
  @FXML
  private Button assignBtn;
  @FXML
  private TableView<ReviewComment> assignedTable;
  @FXML
  private TableColumn<ReviewComment, String> assignedCategoryCol;
  @FXML
  private TableColumn<ReviewComment, String> assignedCommentCol;
  @FXML
  private TableColumn<ReviewComment, Number> assignedPointsCol;
  @FXML
  private Button assignedNewBtn;
  @FXML
  private Button assignedEditBtn;
  @FXML
  private Button assignedDeleteBtn;
  @FXML
  private Button resetBtn;
  @FXML
  private Button gradeBtn;
  @FXML
  private TextField pointsTxt;
  @FXML
  private TextArea reportTxt;

  private File catalogFile;

  private final ObservableList<ReviewComment> catalogComments = FXCollections.
      observableArrayList();

  private final ObservableList<ReviewComment> assignedComments = FXCollections.
      observableArrayList();

  /**
   * Event handler for the File->Open menu item. Displays the file open dialog.
   *
   * @param event the event details
   */
  @FXML
  private void onFileOpenClicked(ActionEvent event) {
    Stage s = (Stage) gradeBtn.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Review Comment Catalog");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Review Comment Catalog", "*.rcc"),
        new ExtensionFilter("All Files", "*.*"));

    File selectedFile = fileChooser.showOpenDialog(s);

    if (selectedFile != null) {
      ReviewCommentSerializer serializer = new ReviewCommentSerializer();

      catalogComments.clear();

      try {
        catalogComments.addAll(serializer.decode(selectedFile));
        catalogFile = selectedFile;

        // Update the window title to include the file name.
        s.setTitle(String.
            format("%s - %s", TITLE, catalogFile.getAbsolutePath()));
      }
      catch (Exception ex) {
        showExceptionDialog(ex);
      }
    }
  }

  /**
   * Event handler for the File->Save as menu item. Closes the primary stage.
   *
   * @param event the event details
   */
  @FXML
  private void onFileSaveAsClicked(ActionEvent event) {
    Stage s = (Stage) gradeBtn.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Review Comment Catalog");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Review Comment Catalog", "*.rcc"),
        new ExtensionFilter("All Files", "*.*"));

    File selectedFile = fileChooser.showSaveDialog(s);

    if (selectedFile != null) {
      ReviewCommentSerializer serializer = new ReviewCommentSerializer();

      try {
        serializer.encode(catalogComments, selectedFile);
        catalogFile = selectedFile;

        // Update the window title to include the file name.
        s.setTitle(String.
            format("%s - %s", TITLE, catalogFile.getAbsolutePath()));
      }
      catch (Exception ex) {
        showExceptionDialog(ex);
      }
    }
  }

  /**
   * Event handler for the File->Save menu item. Closes the primary stage.
   *
   * @param event the event details
   */
  @FXML
  private void onFileSaveClicked(ActionEvent event) {
    
    if (catalogFile != null) {
      ReviewCommentSerializer serializer = new ReviewCommentSerializer();

      try {
        serializer.encode(catalogComments, catalogFile);
      }
      catch (Exception ex) {
        showExceptionDialog(ex);
      }
    }
    else {
      // Treat it as a Save As click.
      onFileSaveAsClicked(event);
    }
  }

  /**
   * Event handler for the File->Close menu item. Closes the primary stage.
   *
   * @param event the event details
   */
  @FXML
  private void onFileCloseClicked(ActionEvent event) {
    Stage s = (Stage) gradeBtn.getScene().getWindow();
    s.close();
  }

  /**
   * The event handler for the assign button.
   *
   * @param event the event details
   */
  @FXML
  private void onAssignClicked(ActionEvent event) {

    ObservableList<ReviewComment> items = catalogTable.getSelectionModel().
        getSelectedItems();

    items.forEach(item -> {
      ReviewComment assignedItem = new ReviewComment(item.getCategory(), item.
          getComment(), item.getPoints());
      assignedComments.add(assignedItem);
    });
  }

  /**
   * The event handler for the assigned delete button.
   *
   * @param event the event details
   */
  @FXML
  private void onAssignedDeleteClicked(ActionEvent event) {
    ObservableList<ReviewComment> items = assignedTable.getSelectionModel().
        getSelectedItems();
    assignedComments.removeAll(items);
  }

  /**
   * The event handler for the reset button.
   *
   * @param event the event details
   */
  @FXML
  private void onResetClicked(ActionEvent event) {
    assignedComments.clear();
    reportTxt.setText("");
  }

  /**
   * The event handler for the new catalog item button.
   *
   * @param event the event details
   */
  @FXML
  private void onCatalogNewClicked(ActionEvent event) {
    ReviewComment item = showEditDialog("New Review Comment",
        new ReviewComment());

    if (item != null) {
      catalogComments.add(item);
    }
  }

  /**
   * The event handler for the new assigned item button.
   *
   * @param event the event details
   */
  @FXML
  private void onAssignedNewClicked(ActionEvent event) {
    ReviewComment item = showEditDialog("New Review Comment",
        new ReviewComment());

    if (item != null) {
      assignedComments.add(item);
    }
  }

  /**
   * Shows the edit dialog with the given title. The dialog will be populated to
   * edit the given comment. If the OK button is clicked, the comment will be
   * updated with the edits and returned.
   *
   * @param title the title of the dialog
   * @param comment the comment to be edited
   *
   * @return the edited comment on OK or null on Cancel
   */
  private ReviewComment showEditDialog(String title, ReviewComment comment) {

    ReviewComment result = null;

    // Get the catories to display.
    Set<String> categories = catalogComments.stream().map(
        ReviewComment::getCategory).collect(Collectors.toSet());

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/org/mpilone/simplegrader/ui/ReviewCommentDialog.fxml"));

      Parent root = loader.load();
      ReviewCommentDialogController controller = loader.getController();

      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.setHeaderText(title);
      dialog.setTitle(title);
      dialog.setResizable(true);
      dialog.initOwner(gradeBtn.getScene().getWindow());
      dialog.getDialogPane().setContent(root);
      dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
      dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

      controller.setReviewComment(comment);
      controller.setCategories(FXCollections.observableList(
          new ArrayList<String>(categories)));

      if (dialog.showAndWait().get() == ButtonType.OK) {
        result = controller.getReviewComment();
      }
    }
    catch (IOException ex) {
      showExceptionDialog(ex);
    }

    return result;
  }

  /**
   * Shows an {@link Alert} dialog with expandable exception details.
   *
   * @param ex the exception to display
   */
  private void showExceptionDialog(Exception ex) {
    // Create expandable Exception.
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String exceptionText = sw.toString();

    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save item.");
    alert.getDialogPane().setExpandableContent(new Text(exceptionText));
    alert.initOwner(gradeBtn.getScene().getWindow());
    alert.show();
  }

  /**
   * The event handler for the catalog delete button.
   *
   * @param event the event details
   */
  @FXML
  private void onCatalogEditClicked(ActionEvent event) {
    // Get the item to edit.
    ReviewComment item = catalogTable.getSelectionModel().
        getSelectedItem();

    showEditDialog("Edit Review Comment", item);
  }

  /**
   * The event handler for the catalog delete button.
   *
   * @param event the event details
   */
  @FXML
  private void onCatalogDeleteClicked(ActionEvent event) {
    ObservableList<ReviewComment> items = catalogTable.getSelectionModel().
        getSelectedItems();
    catalogComments.removeAll(items);
  }

  /**
   * The event handler for the grade button.
   *
   * @param event the event details
   */
  @FXML
  private void onGradeClicked(ActionEvent event) {
    String tmp = pointsTxt.getText();
    Integer possiblePoints = tmp == null || tmp.isEmpty() ? null : Integer.
        parseInt(tmp);

    ReportBuilder rb = new ReportBuilder(assignedComments, possiblePoints);
    reportTxt.setText(rb.toString());
  }

  /**
   * Initializes the UI.
   *
   * @param url the URL of the FXML file
   * @param rb the resource bundle
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Assigned table column data.
    catalogCategoryCol.setCellValueFactory(cellData -> cellData.getValue().
        categoryProperty());
    catalogCommentCol.setCellValueFactory(cellData -> cellData.getValue().
        commentProperty());
    catalogCommentCol.setCellFactory(column -> {
      return new MultilineCell();
    });
    catalogPointsCol.setCellValueFactory(cellData -> cellData.getValue().
        pointsProperty());
    catalogTable.setItems(catalogComments);

    assignedCategoryCol.setCellValueFactory(cellData -> cellData.getValue().
        categoryProperty());
    assignedCommentCol.setCellValueFactory(cellData -> cellData.getValue().
        commentProperty());
    assignedCommentCol.setCellFactory(column -> {
      return new MultilineCell();
    });
    assignedPointsCol.setCellValueFactory(cellData -> cellData.getValue().
        pointsProperty());
    assignedTable.setItems(assignedComments);

    // Apply button icons using FontAwesome.
    GlyphsDude.setIcon(catalogNewBtn, FontAwesomeIcon.PLUS_SQUARE,
        ContentDisplay.GRAPHIC_ONLY);
    GlyphsDude.setIcon(catalogDeleteBtn, FontAwesomeIcon.MINUS_SQUARE,
        ContentDisplay.GRAPHIC_ONLY);
    GlyphsDude.setIcon(catalogEditBtn, FontAwesomeIcon.EDIT,
        ContentDisplay.GRAPHIC_ONLY);
    GlyphsDude.setIcon(assignBtn, FontAwesomeIcon.CHECK_SQUARE,
        ContentDisplay.GRAPHIC_ONLY);

    GlyphsDude.setIcon(assignedNewBtn, FontAwesomeIcon.PLUS_SQUARE,
        ContentDisplay.GRAPHIC_ONLY);
    GlyphsDude.setIcon(assignedEditBtn, FontAwesomeIcon.EDIT,
        ContentDisplay.GRAPHIC_ONLY);
    GlyphsDude.setIcon(assignedDeleteBtn, FontAwesomeIcon.MINUS_SQUARE,
        ContentDisplay.GRAPHIC_ONLY);
    GlyphsDude.setIcon(resetBtn, FontAwesomeIcon.RECYCLE,
        ContentDisplay.GRAPHIC_ONLY);

    GlyphsDude.setIcon(gradeBtn, FontAwesomeIcon.LIST);

    // Listen to table selection.
    catalogTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    catalogTable.getSelectionModel().selectedIndexProperty().addListener(
        this::onCatalogTableSelectionChange);

    assignedTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    assignedTable.getSelectionModel().selectedIndexProperty().addListener(
        this::onAssignedTableSelectionChange);

    // Set the menu bar to be the system menu.
    mainMenuBar.setUseSystemMenuBar(true);

    // Test code
//    catalogComments.add(new ReviewComment("Design", "Make fields private.",
//        -2));
//    catalogComments.add(new ReviewComment("Documentation",
//        "Use JavaDoc comments for fields and methods.",        -2));
//    catalogComments.add(new ReviewComment("Documentation",
//        "Use inline comments for code logic.", -4));

  }

  /**
   * The event handler for catalog table selection change. The button states
   * will be updated appropriately.
   *
   * @param observable the property that changed
   * @param oldValue the old value
   * @param newValue the new value
   */
  private void onCatalogTableSelectionChange(
      ObservableValue<? extends Number> observable, Number oldValue,
      Number newValue) {

    int count = catalogTable.getSelectionModel().getSelectedIndices().size();
    catalogEditBtn.setDisable(count != 1);
    catalogDeleteBtn.setDisable(count == 0);
    assignBtn.setDisable(count == 0);
  }

  /**
   * The event handler for catalog table selection change. The button states
   * will be updated appropriately.
   *
   * @param observable the property that changed
   * @param oldValue the old value
   * @param newValue the new value
   */
  private void onAssignedTableSelectionChange(
      ObservableValue<? extends Number> observable, Number oldValue,
      Number newValue) {

    int count = assignedTable.getSelectionModel().getSelectedIndices().size();
    assignedEditBtn.setDisable(count != 1);
    assignedDeleteBtn.setDisable(count == 0);
  }

  private static class MultilineCell extends TableCell<ReviewComment, String> {

    private final Text text = new Text();

    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);

      if (empty) {
        setText(null);
        setGraphic(null);
      }
      else {
        text.setText(item);
        text.setWrappingWidth(getTableColumn().getWidth());
        setGraphic(text);
      }
    }
  }
}
