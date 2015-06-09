
package org.mpilone.simplegrader.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.mpilone.simplegrader.domain.ReviewComment;

/**
 * The serializer for saving and loading review comments to and from files.
 *
 * @author mpilone
 */
public class ReviewCommentSerializer {

  /**
   * Encodes the given comments into the output file.
   *
   * @param comments the comments to encode
   * @param outputFile the file to write to
   *
   * @throws FileNotFoundException if there is an error writing the file
   */
  public void encode(List<ReviewComment> comments, File outputFile)
      throws FileNotFoundException {

    // Always duplicate the list so we can control the type being serialized.
    List<ReviewComment> l = new ArrayList<>(comments);
    try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(outputFile))) {
      encoder.writeObject(l);
    }
  }

  /**
   * Decodes the comments found in the given input file.
   *
   * @param inputFile the file to read from
   *
   * @return the comments read from the file
   * @throws FileNotFoundException if there is an error reading the file
   */
  public List<ReviewComment> decode(File inputFile) throws
      FileNotFoundException {

    List<ReviewComment> l;
    try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(inputFile))) {
      l = (List<ReviewComment>) decoder.readObject();
    }

    return l;
  }

}
