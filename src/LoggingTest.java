import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrate correct and incorrect ways to do SLF4J logging.
 * 
 * @author gejohann
 *
 */
public class LoggingTest {
  /**
   * Get the logger object. Some sites recommend making the logger static, some
   * don't. For performance reasons I think it should be static so there's only
   * one instance. One case where it can't be static is if the logger is injected
   * into the object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingTest.class);

  public static void main(final String[] args) {
    /** An object to use as a logging example. */
    UltimateAnswer answer = new LoggingTest.UltimateAnswer();

    // Invalid: this logging method has 2 placeholders, but only one parameter.
    LOGGER.info("msg: {}, {}.", "Hello");

    // Valid
    LOGGER.info("msg: {}, {}.", "Hello", "World");

    // invalid: Throwable instance does not need placeholder
    LOGGER.error("msg: {}, {}", "Hello", new RuntimeException("Wrong way to log an exception."));

    // valid. The Exception, including the message, will be printed in the log.
    LOGGER.error("msg: {}", "Hello", new RuntimeException("Correct way to log an exception."));

    /*
     * Don't do this. It creates a StringBuilder to concatenate the Strings.
     */
    LOGGER.info("Info Logging String Concatenation: " + answer);

    /*
     * Don't do this. Won't get logged (because it's a trace message), but
     * toString() will still be called.
     */
    LOGGER.trace("Trace Logging toString(): {}", answer.toString());

    /*
     * Do this. Won't call toString() unless logging is at trace level.
     */
    LOGGER.trace("Trace Logging Parameter: {}", answer);

    /*
     * Do this. This will call toString() when the message is logged.
     */
    LOGGER.info("Info Logging Parameter: {}", answer);

    RuntimeException e = new RuntimeException("Here in exception.");
    LOGGER.info("Info Logging Parameter Exception: ", e);
    /*
     * If you can't override toString() and need to output a log message, here is
     * one way to do it without calling the String creation method unnecessarily.
     * There are similar methods for all log levels.
     */
    if (LOGGER.isDebugEnabled()) {
      LOGGER.trace("Trace Logging Parameter and custom String: {}", answer.toTraceLoggingString());
    }

    int a = 12;
    int b = 222;
    /*
     * This boxes the calculation.
     */
    LOGGER.info("Here is a calc : {}", a - b);

    LOGGER.info("Here is an array : {} {} {}", (Object[]) args);
    
    LOGGER.info("Logging with \n formatting!");
  }

  public static void logIfDebugEnabled(Object... argument) {
    if (LOGGER.isDebugEnabled()) {
      StringBuilder logString = new StringBuilder();
      for (Object object : argument) {
        logString.append(object);
      }
      LOGGER.debug(logString.toString());
    }
  }

  private static class UltimateAnswer {

    private static final Map<String, String> map = new HashMap<>();

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
