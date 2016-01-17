import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

/**
 * BullyBot
 * A slightly smarter kind of bot.
 * It determines its strongest planet and then attacks the weakest enemy planet.
 * The score is computed based on the number of ships.
 * <p>
 * This bot also serves as a small example that functions are not evil.
 *
 * @author Jur van den Berg
 * @version 2.1
 */
public class BullyBot implements Bot {

    /**
     * Function that gets called every turn.
     * This is where you add your implementation.
     *
     * @param pw The game state
     */
    public void doTurn(PlanetWars pw) {
        // (1) Find my strongest planet.
        Planet source = findStrongestPlanet(pw);

        // (2) Find the weakest enemy or neutral planet.
        Planet dest = findWeakestPlanet(pw);

        // (3) Attack!
        if (source != null && dest != null) {
            pw.issueOrder(source, dest);
        }
    }

    private Planet findStrongestPlanet(PlanetWars pw) {
        Planet strongestPlanet = null;
        double strongestScore = Double.MIN_VALUE;

        // Loop over all my planets
        for (Planet planet : pw.getMyPlanets()) {
            // skip planets with only one ship
            if (planet.getNumShips() <= 1)
                continue;

            // This score is one way of defining how 'good' my planet is.
            double score = (double) planet.getNumShips();

            // If you want to debug how the score is computed, uncomment the following line:
            // pw.log("Planet:", planet.getID(), "Score:", score);

            if (score > strongestScore) {
                //we want to maximize the score, so store the planet with the best score
                strongestScore = score;
                strongestPlanet = planet;
            }
        }

        return strongestPlanet;
    }

    private Planet findWeakestPlanet(PlanetWars pw) {
        Planet weakestPlanet = null;
        double weakestScore = Double.MAX_VALUE;

        // Loop over all planets I do not yet own
        for (Planet planet : pw.getNotMyPlanets()) {

            //This score is one way of defining how 'good' the planet is.
            double score = (double) (planet.getNumShips());

            // If you want to debug how the score is computed, uncomment the following line:
            // pw.log("Planet:", planet.getID(), "Score:", score);

            if (score < weakestScore) {
                //We want to select the planet with the lowest score
                weakestScore = score;
                weakestPlanet = planet;
            }
        }

        return weakestPlanet;
    }

    public static void main(String[] args) {
        GameClient.run(new BullyBot());
    }
}
