package org.mpilone.simplegrader.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.mpilone.simplegrader.domain.ReviewComment;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * The controller for the comment editing dialog.
 *
 * @author mpilone
 */
public class ReviewCommentDialogController implements Initializable {

  @FXML
  private ComboBox<String> categoryCmb;
  @FXML
  private TextField pointsTxt;
  @FXML
  private TextArea commentTxt;

  private ReviewComment reviewComment;

  /**
   * Sets the categories to display in the category selector as possible
   * options.
   *
   * @param items the categories to display
   */
  public void setCategories(ObservableList<String> items) {
    categoryCmb.setItems(items);
  }

  /**
   * Sets the review comment to display/edit. The dialog fields will be
   * immediately populated.
   *
   * @param reviewComment the review comment to edit
   */
  public void setReviewComment(ReviewComment reviewComment) {
    this.reviewComment = reviewComment;

    categoryCmb.setValue(reviewComment.getCategory());
    commentTxt.setText(reviewComment.getComment());
    pointsTxt.setText(String.valueOf(reviewComment.getPoints()));
  }

  /**
   * Returns the review comment being edited. The comment will be populated with
   * the values from the UI.
   *
   * @return the comment being edited
   */
  public ReviewComment getReviewComment() {
    if (reviewComment == null) {
      reviewComment = new ReviewComment();
    }

    String value = categoryCmb.getValue();
    if (value != null && !value.isEmpty()) {
      reviewComment.setCategory(value);
    }

    value = commentTxt.getText();
    if (value != null && !value.isEmpty()) {
      reviewComment.setComment(value);
    }

    value = pointsTxt.getText();
    if (value != null && !value.isEmpty()) {
      try {
        reviewComment.setPoints(Integer.parseInt(value));
      }
      catch (NumberFormatException ex) {
        // ignore
      }
    }

    return reviewComment;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // no op
  }

}
