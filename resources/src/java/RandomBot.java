import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

import java.util.List;
import java.util.Random;

/**
 * RandomBot
 * An example bot that picks one of his planets at random and sends half of the ships
 * from that planet to a random target planet.
 * <p>
 * Not a very clever bot, but showcases the functions that can be used.
 * Overcommented for educational purposes.
 *
 * @author Jur van den Berg
 * @version 2.1
 */
public class RandomBot implements Bot {

    public static void main(String[] args) {
        GameClient.run(new RandomBot());
    }

    /**
     * Function that gets called every turn.
     * This is where you add your implementation
     *
     * @param pw The game state
     */
    public void doTurn(PlanetWars pw) {
        // (0) Create a random number generator
        Random random = new Random();

        // (1) Pick one of my planets at random.
        Planet source = null;

        // (1a) Take the list of my planets
        List<Planet> myPlanets = pw.getMyPlanets();

        // An example debug statement.
        // These statements are useful when you don't understand the behaviour of your bot.
        pw.log("I have", myPlanets.size(), "planets.");

        // (1b) If the list is not empty:
        if (myPlanets.size() > 0) {
            // (1c) Pick a random integer in [0, number_of_my_planets)
            Integer randomSource = random.nextInt(myPlanets.size());

            // (1d) Pick a random planet as source
            source = myPlanets.get(randomSource);
        }

        // (2) Pick a target planet at random
        Planet dest = null;

        // (2a) Take the list of not my planets
        List<Planet> allPlanets = pw.getNotMyPlanets();

        // (2b) If the list is not empty:
        if (allPlanets.size() > 0) {
            // (2c) Pick a random integer in [0, number_of_all_planets]
            Integer randomTarget = random.nextInt(allPlanets.size());

            // (2d) Pick a random planet as target
            dest = allPlanets.get(randomTarget);
        }

        // (3) Send half the ships from source to destination
        if (source != null && dest != null) {
            pw.issueOrder(source, dest);
        }
    }
}
