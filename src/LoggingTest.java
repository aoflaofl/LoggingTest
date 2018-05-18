import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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

  /**
   * Try to cause every kind of logging problem.
   * 
   * @param args
   *          Unused
   */
  public static void main(final String[] args) {

    /*
     * First, some general best practices for using {} place-holders when logging
     * messages and Exceptions.
     */

    // Valid. Two place-holders and two parameters.
    LOGGER.info("msg: {}, {}.", "Hello", "World");

    // Invalid. This logging call has 2 place-holders, but only one parameter.
    LOGGER.info("msg: {}, {}.", "Hello");

    // Valid. How to have {} display in a logging message.
    LOGGER.info("msg: \\{}, {}.", "World");

    /*
     * Logging Exceptions.
     */
    RuntimeException ex = new RuntimeException("Something is wrong!");

    // Invalid. Throwable instance does not need placeholder if it is the last
    // argument. In this case, the second place-holder is ignored.
    LOGGER.error("msg: {}, {}", "Hello", ex);

    // Valid. The Exception, including its message, will be printed in the log.
    // There is no need to separately log the message.
    LOGGER.error("msg: {}", "Hello", new RuntimeException("Correct way to log an exception."));

    /*
     * Create a big ugly map of random Strings. This is something that would never
     * be logged unless necessary for debugging and takes work to generate the
     * String representation. An important goal is to make sure logging this Map
     * does not impact application performance.
     */
    RandomString gen = new RandomString(10, ThreadLocalRandom.current());
    Map<String, String> bigUglyMap = new HashMap<>(1000);
    for (int i = 0; i < 1000; i++) {
      bigUglyMap.put(gen.nextString(), gen.nextString());
    }

    /** An object to use as a logging example. */
    // UltimateAnswer answer = new LoggingTest.UltimateAnswer();

    /*
     * Don't do this. It creates a StringBuilder behind the scenes to concatenate
     * the Strings. In Java, all arguments to a method have to be resolved to a
     * primitive or Object reference before the method is called.
     */
    LOGGER.trace("Trace Logging String Concatenation: " + bigUglyMap);

    /*
     * Don't do this. Won't get logged (because it's a trace message), but
     * toString() will still be called.
     */
    LOGGER.trace("Trace Logging toString(): {}", bigUglyMap.toString());

    /*
     * Do this. Won't call toString() unless logging is at trace level.
     */
    LOGGER.trace("Trace Logging Parameter: {}", bigUglyMap);

    /*
     * Do this. This will call toString() when the message is logged.
     */
    LOGGER.info("Info Logging Parameter: {}", bigUglyMap);

    RuntimeException e = new RuntimeException("Here in exception.");
    LOGGER.info("Info Logging Parameter Exception: ", e);
    // LOGGER.info(e);
    /*
     * If you can't override toString() and need to output a log message, here is
     * one way to do it without calling the String creation method unnecessarily.
     * There are similar methods for all log levels.
     */
    if (LOGGER.isDebugEnabled()) {
      // LOGGER.trace("Trace Logging Parameter and custom String: {}",
      // m.toTraceLoggingString());
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

  /**
   * Trying to make a logging helper function.
   * 
   * @param argument
   *          Object list of things to be logged.
   */
  public static void logIfDebugEnabled(Object... argument) {
    if (LOGGER.isDebugEnabled()) {
      StringBuilder logString = new StringBuilder();
      for (Object object : argument) {
        logString.append(object);
      }
      LOGGER.debug(logString.toString());
    }
  }

}
