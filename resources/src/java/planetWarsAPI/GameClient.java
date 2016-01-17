package planetWarsAPI;

/*
 * You do not need to worry about anything in this file. This is just
 * helper code that does the boring stuff for you, so you can focus on the
 * interesting stuff. That being said, you're welcome to change anything in
 * this file if you know what you're doing.
 */

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @package planetWarsAPI
 * Java version of the API
 */

/**
 * The wrapper around doing every turn.
 * This prevents you from writing this entire block of code,
 * in the way we had to.
 *
 * @author Jur van den Berg
 * @version 1.0
 */
public class GameClient {
    /**
     * Start the main bot loop. This is the only thing your bot needs to call
     * in it's main.
     * <p>
     * Example:
     * <pre>{@code
     * public static void main(String[] args) {
     *     GameClient.run(new YourBot());
     * }
     * }</pre>
     * @param bot Instance of the Bot that will be running
     */
    public static void run(Bot bot) {
        String line = "";
        String message = "";
        int c;
        try {
            while ((c = System.in.read()) >= 0) {
                switch (c) {
                    case '\n':
                        if (line.equals("go")) {
                            PlanetWars pw = new PlanetWars(message);
                            bot.doTurn(pw);
                            pw.finishTurn();
                            message = "";
                        } else {
                            message += line + "\n";
                        }
                        line = "";
                        break;
                    default:
                        line += (char) c;
                        break;
                }
            }
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String stackTrace = writer.toString();
            System.err.println(stackTrace);
            System.exit(1); //just stop now. we've got a problem
        }
    }
}
