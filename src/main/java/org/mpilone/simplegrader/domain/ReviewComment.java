
package org.mpilone.simplegrader.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * A review comment that can be assigned when grading an assignment. The comment
 * includes a category, text comment, and point value.
 *
 * @author mpilone
 */
public class ReviewComment {

  private final SimpleStringProperty category = new SimpleStringProperty(
      "General");
  private final SimpleStringProperty comment = new SimpleStringProperty("");
  private final SimpleIntegerProperty points = new SimpleIntegerProperty(0);

  public ReviewComment() {
  }

  public ReviewComment(String category, String comment, int points) {
    this.comment.set(comment);
    this.category.set(category);
    this.points.set(points);
  }

  public String getCategory() {
    return category.get();
  }

  public String getComment() {
    return comment.get();
  }

  public int getPoints() {
    return points.get();
  }

  public SimpleStringProperty categoryProperty() {
    return category;
  }

  public SimpleStringProperty commentProperty() {
    return comment;
  }

  public SimpleIntegerProperty pointsProperty() {
    return points;
  }

  public void setComment(String comment) {
    this.comment.set(comment);
  }

  public void setPoints(int points) {
    this.points.set(points);
  }

  public void setCategory(String category) {
    this.category.set(category);
  }

}
