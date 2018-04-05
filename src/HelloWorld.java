import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(HelloWorld.class);

		String s = "Log this.";

		if (logger.isInfoEnabled()) {
			logger.info("Hello World {}", s.toString());
		}
	}
}