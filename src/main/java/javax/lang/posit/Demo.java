package javax.lang.posit;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a Posit demo from the command line.
 *
 * @author <a href="mailto://dan@danbecker.info">Dan Becker</a>
 */
public abstract class Demo extends Number implements Comparable<Demo> {
	/** Serialization version */
	private static final long serialVersionUID = 1L;

	/** LOGGER */
	public static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

	// Constructors
	// Runtime
	public static void main(String[] args) throws Exception {
		LOGGER.info("Posit demo");
		// Parse command line options
		parseOptions(args);
		LOGGER.debug("Posit bye");
	}

	/** Command line options for this application. */
	public static void parseOptions(String[] args) throws ParseException, IOException {
		// Parse the command line arguments
		final Options options = new Options();
		// Use dash with shortcut (-h) or -- with name (--help).
		options.addOption("h", "help", false, "print the command line options");
		options.addOption("n", "numPatterns", true, "generates this many patterns");

		final CommandLineParser cliParser = new DefaultParser();
		final CommandLine line = cliParser.parse(options, args);

		// // Gather command line arguments for execution
		if (line.hasOption("help")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar posit.jar <options> javax.lang.posit.Posit", options);
			System.exit(0);
		}
		// if (line.hasOption("numPatterns")) {
		// numPatterns = Integer.parseInt(line.getOptionValue("numPatterns"));
		// System.out.println(" numPatterns=" + numPatterns);
		// }
	}
}