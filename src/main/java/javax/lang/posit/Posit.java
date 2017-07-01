package javax.lang.posit;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Posit
 * <p>
 * A tool for generating random words, phrases, and sentences
 * 
 * @author <a href="mailto://dan@danbecker.info>Dan Becker</a>
 */
public class Posit {
	public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Posit.class);

	public static String WORD_TYPE_DELIM = ",";

	// options
	
	public static Random random = new Random();

	public static void main(String[] args) throws Exception {
		LOGGER.debug("Hello");
		System.out.println("Posit");
		// Parse command line options
		parseGatherOptions(args);
		LOGGER.debug("Bye");
	}

	/** Command line options for this application. */
	public static void parseGatherOptions(String[] args) throws ParseException, IOException {
		// Parse the command line arguments
		Options options = new Options();
		// Use dash with shortcut (-h) or -- with name (--help).
		options.addOption("h", "help", false, "print the command line options");
		options.addOption("n", "numPatterns", true, "generates this many patterns");

		CommandLineParser cliParser = new DefaultParser();
		CommandLine line = cliParser.parse(options, args);

		// // Gather command line arguments for execution
		if (line.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar posit.jar <options> javax.lang.posit.Posit",
					options);
			System.exit(0);
		}
//		if (line.hasOption("numPatterns")) {
//			numPatterns = Integer.parseInt(line.getOptionValue("numPatterns"));
//			System.out.println("   numPatterns=" + numPatterns);
//		}
	}
}