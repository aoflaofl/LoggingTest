import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(LoggingTest.class);

    UltimateAnswer s = new LoggingTest.UltimateAnswer();

    /*
     * Don't do this. It creates a StringBuilder to concatenate the Strings.
     */
    logger.info("String Concatenation: " + s);

    logger.info("Hello World {}", s);
    if (logger.isInfoEnabled()) {
      logger.info("Hello World {}", s.toInfoLoggingString());
    }
  }

  static class UltimateAnswer {

    static Map<String, String> map = new HashMap<>();

    static {
      map.put("41", "Almost");
      map.put("42", "Yep");
      map.put("43", "Too Far");
    }

    @Override
    public String toString() {
      return map.toString();
    }

    public String toInfoLoggingString() {
      return "The Ultimate Answer to Life, the Universe and Everything.";
    }
  }
}