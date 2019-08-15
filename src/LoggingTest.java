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
public final class LoggingTest {

  /** Instantiate nothing. */
  private LoggingTest() {
  }

  /**
   * Get the logger object. Some sites recommend making the logger static, some
   * don't. For performance reasons I think it should be static so there's only
   * one instance. One case where it can't be static is if the logger is injected
   * into the object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingTest.class);

  /**
   * Try to cause every kind of logging problem and show best practices.
   * 
   * @param args
   *          Command line arguments.
   */
  public static void main(final String[] args) {
    String key = "Hello";
    String value = "World!";

    /*
     * Don't do this. Java creates a StringBuilder object to handle the String
     * concatenation. And if the log level is not DEBUG then the resulting String
     * will be Garbage Collected without being used.
     * 
     * Hint: Set logging level to INFO and use a debugger on this line.
     */
    LOGGER.debug("Example 1 : String concatenation : Key=" + key + ", Value=" + value);

    /*
     * Don't do this unless you need the formatting functions. String.format() is
     * more expensive than String concatenation.
     */
    LOGGER.debug(String.format("Example 2 : String format : Key=%s, Value=%s", key, value));

    /*
     * Instead use the {} place-holder format.
     * 
     * The benefit is that the parameters will be interpolated into the message only
     * if and when the message is output to the log.
     * 
     * Another benefit is keeping formatting and data separate for easier editing.
     */
    LOGGER.debug("Example 3 : Parameters : Key={}, Value={}", key, value);

    /*
     * Best practices for using {} place-holders when logging messages and
     * Exceptions.
     */

    // Valid. Two place-holders and two parameters.
    LOGGER.info("Example 4 : Correct number of place-holders : {}, {}.", "Hello", "World");

    // Bad. This logging call has 2 place-holders, but only one parameter.
    LOGGER.info("Example 5 : Missing parameter : {}, {}.", "Hello");

    // Valid. If you need to show {} in a log, here is how.
    LOGGER.info("Example 6 : How to log \\{} : \\{}, {}.", "World");

    /*
     * Best practices for logging Exceptions.
     * 
     * Important: All the logging methods take a Throwable object as an optional
     * final argument so it is not necessary to use place-holders when logging
     * exceptions.
     */
    RuntimeException ex1 = new RuntimeException("This is an Exception!");

    // Bad. Throwable instance does not need placeholder if it is the last
    // argument. In this case, the second place-holder is ignored.
    LOGGER.error("Example 7 : Exception doesn't use parameter : {}, {}", key, ex1);

    // Bad. There is no need to log getMessage() because the Exception's
    // message is already logged with the Exception.
    LOGGER.error("Example 8 : No need to log Exception's Message : {}, {}", ex1.getMessage(), ex1);

    // Valid. The Exception, including its message, will be printed in the log.
    // There is no need to separately log the message.
    RuntimeException ex2 = new RuntimeException("Correct way to log an exception.");
    LOGGER.error("Example 9 : Correct way to log Exception : {}", key, ex2);

    /*
     * Create a big ugly map of random Strings. This is something that would never
     * be logged unless necessary for debugging and takes work to generate the
     * String representation.
     * 
     * An important goal with logging is to make sure logging Objects like this does
     * not impact application performance.
     */
    Map<String, String> bigUglyMap = new HashMap<>(1000);
    for (int i = 0; i < 1000; i++) {
      bigUglyMap.put(randomAlphaString(10), randomAlphaString(10));
    }

    /*
     * Don't do this. It creates a StringBuilder behind the scenes to concatenate
     * the Strings. In Java, all arguments to a method have to be resolved to a
     * primitive or Object reference before the method is called, and for logging,
     * all primitives will be boxed. Therefore the following line wastes the time it
     * takes to build the final String for concatenation.
     */
    LOGGER.trace("Example 10 : Trace Logging with String Concatenation : " + bigUglyMap);

    /*
     * Don't do this. Will rarely get logged (because it's a trace message), but
     * toString() will still be called.
     */
    LOGGER.trace("Example 11 : Trace Logging with toString() : {}", bigUglyMap.toString());

    /*
     * Do this. Won't call toString() unless logging is at trace level.
     */
    LOGGER.trace("Example 12 : Trace Logging with Parameter, no toString() : {}", bigUglyMap);

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
    LOGGER.info("Example 14 : Boxing example : {} - {} = {}", a, b, a - b);

    String[] ary = { "one", "two", "three" };
    LOGGER.info("Example 15 : Logging an array - Too many values : {} {}", (Object[]) ary);
    LOGGER.info("Example 16 : Logging an array - Too many place-holders : {} {} {} {}", (Object[]) ary);

    // You can use newlines and other formatting in log messages. It will just make
    // the logs look worse and harder to parse.
    LOGGER.info("Logging with \n formatting!\nPlease don't do this!\n\n\tOh\n\t\tthe\n\t\t\t\thumanity!");
  }

  /**
   * Trying to make a logging helper function.
   * 
   * This is something to try, but keep in mind that the Object... vararg means
   * the arguments to this method have to be Object references, so primitives will
   * be boxed and all function calls will be resolved to an Object reference.
   * 
   * @param argument
   *          Object list of things to be logged.
   */
  public static void logIfDebugEnabled(final Object... argument) {
    if (LOGGER.isDebugEnabled()) {
      StringBuilder logString = new StringBuilder();
      for (Object object : argument) {
        logString.append(object);
      }
      LOGGER.debug(logString.toString());
    }
  }

  /**
   * Generate random Alpha String of a given length.
   * 
   * @param count
   *          length of String to generate
   * @return Random String of Alpha chars of count length.
   */
  public static String randomAlphaString(int count) {
    StringBuilder builder = new StringBuilder(count);
    while (count-- != 0) {
      builder.append((char) ('A' + (int) (Math.random() * 26)));
    }

    return builder.toString();
  }

}
