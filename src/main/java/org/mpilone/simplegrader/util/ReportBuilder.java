
package org.mpilone.simplegrader.util;

import java.util.*;
import java.util.stream.Collectors;

import org.mpilone.simplegrader.domain.ReviewComment;

/**
 * Builds the grading report based on the items.
 *
 * @author mpilone
 */
public class ReportBuilder {

  private Collection<ReviewComment> items;
  private Integer possiblePoints = null;

  /**
   * Constructs the builder with no possible point value. Only the point
   * additions or subtractions will be displayed.
   *
   * @param items the items to include in the report
   */
  public ReportBuilder(Collection<ReviewComment> items) {
    this(items, null);
  }

  /**
   * Constructs the builder with possible points. The deductions will be removed
   * from the possible points and a final grade calculation included in the
   * report.
   *
   * @param items the items to include in the report
   * @param possiblePoints the total possible points for grading
   */
  public ReportBuilder(Collection<ReviewComment> items, Integer possiblePoints) {
    this.items = items;
    this.possiblePoints = possiblePoints;
  }

  /**
   * Returns the report that was built from the items.
   *
   * @return the report
   */
  @Override
  public String toString() {
    final Map<String, Integer> pointMap = new HashMap<>();
    final Map<String, StringBuilder> commentMap = new HashMap<>();

    // Group the comments by category and total the points.
    items.forEach(item -> {
      String category = item.getCategory();

      if (!pointMap.containsKey(category)) {
        pointMap.put(category, 0);
        commentMap.put(category, new StringBuilder());
      }

      pointMap.put(category, pointMap.get(category) + item.getPoints());
      commentMap.get(category).append("- ").append(item.getComment()).append(
          " (").append(item.getPoints()).append(")\n");
    });

    // Sort the categories.
    List<String> categories = new ArrayList<>(commentMap.keySet());
    categories.sort((item1, item2) -> {
      return item1.compareTo(item2);
    });

    // Build the final report.
    final int totalPoints = pointMap.values().stream().collect(Collectors.
        summingInt(v -> v));
    final StringBuilder report = new StringBuilder();

    categories.forEach(category -> {
      int points = pointMap.get(category);

      int titleLength = category.length() + 2 + String.valueOf(points).length()
          + 1;

      report.append(category).append(" (").append(points).append(")\n");
      String underline = new String(new char[titleLength]).replace('\0', '-');
      report.append(underline).append("\n");

      report.append(commentMap.get(category)).append("\n\n");
    });

    report.append("Grade: ");
    if (possiblePoints != null) {
      int earnedPoints = possiblePoints + totalPoints;
      String grade = String.format("%.2f", (earnedPoints
          / (double) possiblePoints) * 100);

      report.append(earnedPoints).append("/").append(possiblePoints).
          append(" (").append(grade).append("%)");
    }
    else {
      report.append(totalPoints);
    }
    report.append("\n");

    return report.toString();
  }

}
