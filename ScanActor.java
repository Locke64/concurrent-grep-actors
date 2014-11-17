import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.LinkedList;
import Configure.java;
import Found.java;
import java.util.regex.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ScanActor extends UntypedActor{
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private Configure assignment;
  private final Pattern regExpression;

  /*
   * Constructor for the ScanActor
   *
   * @param expression      the regular expression used in evaluating the file
   */

  public ScanActor(Pattern expression) {
    regExpression = expression;
  }
 
  /*
   * Method that waits to receive a message from another Actor.
   *
   * @param message    the message Object sent from another Actor
   */

  public void onReceive(Object message) throws Exception {
    if (message instanceof Configure) {
      log.info("Received Configure  message: {}", message);
      assignment = message;
      Found results = null;

      // if a filename is given, compare the regular expression against the contents
      // otherwise, compare the regular expression against the line from standard input

      if (!assignment.getFilename().equals(null)) {
        results = searchFile();
      } else {
        results = searchInput();
      }

      assignment.getReference().tell(results);
    } else
      unhandled(message);
  }

  /*
   * Static method that performs the scanning of a file. Any line
   * that matches the regular expression is added to a list of
   * matching lines.
   *
   * @return regExResults     a Found object containing the results of scanning the file
   */

  private static Found searchFile() {
    BufferedReader reader = null;
    LinkedList<String> matchingLines = new LinkedList<String>();
    int lineNumber = 0;
    String line = null;
    Matcher lineMatcher = null;
    Found regExResults;

    try {
      reader = new BufferedReader(new FileReader(assignment.getFilename()));
      line = reader.readLine();
      while(line != null) {
        lineNumber++;
	lineMatcher = regExpression.matcher(line);
        if (lineMatcher.find()) {
	  matchingLines.add(lineNumber + " " + line);
	}
        line = reader.readLine();
      }
    }catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }catch (IOExcepion e) {
      System.err.println(e.getMessage());
    }

    regExResults = new Found(assignment.getFilename(), matchingLines);
    return regExResults;
  }

  /*
   * Method used to scan a line sent in from Standard Input. If the line matches,
   * it is added to a list of matching lines.
   *
   * @return  regExResults     the Found object containing the results of scanning Standard Input
   */

  private static Found searchInput() {
    //TODO write this method
    BufferedReader reader = null;
    LinkedList<String> matchingLines = new LinkedList<String>();
    Matcher lineMatcher = null;
    String line = null;
    Found regExResults;

    try {
      reader = new BufferedReader(new InputStreamReader(System.in));
      System.out.printf("Please enter a string:");
      line = reader.readLine();
      lineMatcher = regExpression.matcher(line);
      if (lineMatcher.find()) {
        matchingLines.add(line);
      }
    }catch (IOException e) {
      System.err.println(e.getMessage());
    }

    regExResults = new Found("-", matchingLines);
    return regExResults; 
  }
}
