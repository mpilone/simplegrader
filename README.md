# Simple Grader

Simple Grader is a basic utility to assist with grading assignments. The grader maintains review comment catalogs that can be used to quickly assign common comments to assignments while grading. The comments are grouped by category and carry a point value. Once grading is complete, a summary of all comments and a final grade are generated.

## Motivation

I wrote Simple Grader after grading introductory programming assignments for a few years. I found that I was repeating the same comments over and over again:

- "Comment your code."
- "Use proper variable names."
- "Move the X functionality out into a separate method."
- "Be sure to include supporting documentation and test cases."

After a while I started maintaining my comments in a text file so they could be easily copied and pasted as I graded. This helped a lot but it wasn't the fastest process. Simple Grader makes reusing assignment review comments (and adding new, custom ones) a lot easier and a single button resets the assigned comments for the next student.

## Usage

After launching the application, if this is the first time grading an assignment add comments to the comment catalog table as you grade the first few assignments. If you already have a review comment catalog created, load it using the ```File``` menu.

As you review an assignment, select comments from the catalog and assign them, copying the assignment to the assigned comment table. You can always edit or add custom comments in the assigned comment table without affecting the comment catalog. This allows you to create custom or enhance catalog comments for each student while maintaining the base catalog for grading speed and consistency.

Once all the review comments have been assigned, the final grade summary can be generated including the comments by category and a final score.

At the completion of grading, you can save the review comment catalog for future use.

## Screenshots

![Catalog Loaded](/docs/CatalogLoaded.png?raw=true "Catalog Loaded")

![Fully Graded Assignment](/docs/FullyGradedAssignment.png?raw=true "Fully Graded Assignment")

## Requirements

- Java 8 Runtime Environment
