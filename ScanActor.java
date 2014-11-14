import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.LinkedList;
import Configure.java;
import Found.java;
import java.util.regex.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScanActor extends UntypedActor{
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private Configure assignment;
  private final Pattern regExpression;

  public ScanActor(Pattern expression) {
    regExpression = expression;
  }
 
  public void onReceive(Object message) throws Exception {
    if (message instanceof Configure) {
      log.info("Received Configure  message: {}", message);
      assignment = message;
      Found results = null;

      if (!assignment.getFilename().equals(null)) {
        results = searchFile();
      } else {
        results = searchInput();
      }

      getSender().tell(message, getSelf());
    } else
      unhandled(message);
  }

  private static Found searchFile() {
    BufferedREader reader = null;
    LinkedList<String> matchingLines = new LinkedList<String>();
    int lineNumber = 0;
    String line = null;
    Matcher lineMatcher = null;
    Found regExResults;

    try {
      reader = new BufferedReader(new FileReader(assignment.getFilename());
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
      //TODO exception handling
    }catch (IOExcepion e) {
      //TODO exeption handling
    }

    regExResults = new Found(assignment.getFilename(), matchingLines);
    return regExResults;
  }

  private static Found searchInput() {
    //TODO write this method
  }
}
