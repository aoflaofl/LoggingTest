import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomString {

  /**
   * Generate a random string.
   */
  public String nextString() {
    for (int idx = 0; idx < buf.length; ++idx) {
      buf[idx] = symbols[random.nextInt(symbols.length)];
    }
    return new String(buf);
  }

  public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);

  public static final String DIGITS = "0123456789";

  public static final String ALPHANUM = UPPER + LOWER + DIGITS;

  private final Random random;

  private final char[] symbols;

  private final char[] buf;

  /**
   * Make a string of random characters to a given length.
   * 
   * @param length
   *          number of chars
   * @param random
   *          random generator
   * @param symbols
   *          symbols to choose from
   */
  public RandomString(final int length, final Random random, final String symbols) {
    if (length < 1) {
      throw new IllegalArgumentException();
    }
    if (symbols.length() < 2) {
      throw new IllegalArgumentException();
    }
    this.random = Objects.requireNonNull(random);
    this.symbols = symbols.toCharArray();
    this.buf = new char[length];
  }

  /**
   * Create an alphanumeric string generator.
   */
  public RandomString(final int length, final Random random) {
    this(length, random, ALPHANUM);
  }

  /**
   * Create an alphanumeric strings from a secure generator.
   */
  public RandomString(final int length) {
    this(length, new SecureRandom());
  }

  /**
   * Create session identifiers.
   */
  public RandomString() {
    this(21);
  }
}
