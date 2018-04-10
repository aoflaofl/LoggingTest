import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
  private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);

  public static void main(final String[] args) {

    UltimateAnswer answer = new LoggingTest.UltimateAnswer();

    // invalid: this logging method has 2 placeholders, but given parameter is only
    // 1.
    logger.info("msg: {}, {}.", "Hello");

    // valid
    logger.info("msg: {}, {}.", "Hello", "World");

    // invalid: Throwable instance does not need placeholder
    logger.error("msg: {}, {}", "Hello", new RuntimeException());

    // valid
    logger.error("msg: {}", "Hello", new RuntimeException());

    /*
     * Don't do this. It creates a StringBuilder to concatenate the Strings.
     */
    logger.info("Info Logging String Concatenation: " + answer);

    /*
     * Don't do this. Won't get logged (because it's a trace message), but
     * toString() will still be called.
     */
    logger.trace("Trace Logging toString(): {}", answer.toString());

    /*
     * Do this. Won't call toString() unless logging is at trace level.
     */
    logger.trace("Trace Logging Parameter: {}", answer);

    /*
     * Do this. This will call toString() when the message is logged.
     */
    logger.info("Info Logging Parameter: {}", answer);
    logger.info("Info Logging Parameter: {} {}", "Something", new RuntimeException("Here in exception."));
    /*
     * If you can't override toString() and need to output a log message, here is
     * one way to do it without calling the String creation method unnecessarily.
     * There are similar methods for all log levels.
     */
    if (logger.isTraceEnabled()) {
      logger.trace("Trace Logging Parameter and custom String: {}", answer.toTraceLoggingString());
    }
  }

  private static class UltimateAnswer {

    private final static Map<String, String> map = new HashMap<>();

    static {
      map.put("41", "Almost");
      map.put("42", "Yep");
      map.put("43", "Too Far");
    }

    @Override
    public String toString() {
      return map.toString();
    }

    private String toTraceLoggingString() {
      return "Trying to figure out the Ultimate Answer to Life, the Universe and Everything.";
    }
  }
}
