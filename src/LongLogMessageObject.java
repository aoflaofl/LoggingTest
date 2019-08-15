import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * An object to do little more than create ugly log messages.
 * 
 * @author gejohann
 *
 */
public final class LongLogMessageObject {

  private Map<String, String> bigUglyMap = new HashMap<>(1000);
  static Random ran = new Random();

  LongLogMessageObject() {
    for (int i = 0; i < 1000; i++) {
      this.bigUglyMap.put(randomAlphaString(10), randomAlphaString(10));
    }
  }

  @Override
  public String toString() {
    return this.bigUglyMap.toString();
  }

  public String longerLoggingMessage() {
    return "This is a longer logging message even though it doesn't add anything to toString(): " + toString();
  }

  /**
   * Generate random Alpha String of a given length.
   * 
   * @param count
   *          length of String to generate
   * @return Random String of Alpha chars of count length.
   */
  public static String randomAlphaString(final int count) {
    StringBuilder builder = new StringBuilder(count);
    for (int i = 0; i < count; i++) {
      builder.append((char) ('A' + ran.nextInt(26)));
    }

    return builder.toString();
  }
}
