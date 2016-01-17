import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

import java.util.List;
import java.util.Random;

/**
 * AdaptiveBot
 * A bot which adapts its behaviour according to the environment characteristics.
 * It changes its strategy, based on the current environment (e.g. number of neutral planets in the map,
 * number of ships, etc.). Knowing which strategy to use has to be collected beforehand.
 * This requires running a number of games of your bots, and evaluate which bot performs best for a certain environment.
 * You should then add this to the data structure (in AdaptivityMap.java).
 * The DoTurn method can then query this data structure to know what strategy should be used for this turn.
 * This example provides two environment variables: the number of neutral planets on the map, and the average growth
 * ratio of these neutral planets.
 * <p>
 * We provide a possible implementation using the hash adaptivityMap, which maps lists of integers (representing
 * the environment) with names of bots. See AdaptivityMap.java
 * <p>
 * Interesting questions (you can probably come up with other questions yourself as well):
 * 1. Can you modify or extend the environment variables we use?
 * Maybe other things are interesting other than the number of neutral planets,
 * and the average planet size of these neutral planets.
 * 2. The table in AdaptivityMap.java is filled by us (randomly) with only two simple bots.
 * But how should the table really look like?
 * This means you should collect data on how all your previous bots (BullyBot, RandomBot, HillClimbingBot,
 * LookaheadBot and/or others) perform in different environments.
 * 3. Can you implement your other bot implementations in AdaptiveBot.java?
 * Currently the only strategies are BullyBot ('DoBullyBotTurn') and RandomBot ('DoRandomBotTurn').
 * Implement the bot strategies you used to fill AdaptivityMap.java here as well.
 *
 * @author Jur van den Berg
 * @version 2.0
 */
public class AdaptiveBot implements Bot {

    public static void main(String[] args) {
        GameClient.run(new AdaptiveBot());
    }

    /**
     * Function that gets called every turn.
     * This is where you add your implementation.
     *
     * @param pw The game state
     */
    public void doTurn(PlanetWars pw) {

        // Retrieve environment characteristics
        // Are there characteristics you want to use instead, or are there more you'd like to use? Try it out!
        int neutralPlanets = pw.getNeutralPlanets().size();
        float totalPlanetSize = 0;
        for (Planet p : pw.getNeutralPlanets()) {
            totalPlanetSize += p.getGrowthRate() / pw.getNeutralPlanets().size();
        }

        int averagePlanetSize = Math.round(totalPlanetSize);

        // Use AdaptivityMap to get the bot which matches the current environment characteristics
        String thisTurnBot = AdaptivityMap.get(neutralPlanets, averagePlanetSize);

        if (thisTurnBot == null) {
            pw.log("WARNING: You have not entered bot data for this case. Using default bot");
            doRandomBotTurn(pw);
        } else {
            if (thisTurnBot.equals("BullyBot")) {
                pw.log("BullyBot is going to play this turn");
                doBullyBotTurn(pw);
            } else if (thisTurnBot.equals("RandomBot")) {
                pw.log("RandomBot is going to play this turn");
                doRandomBotTurn(pw);
            } else {
                pw.log("WARNING: Adaptivity map wants", thisTurnBot,
                    "to play this turn, but this strategy is not implemented in this bot! Using default bot");
                doRandomBotTurn(pw);
            }
        }
    }

    /**
     * Implementation of the BullyBot strategy
     * <p>
     * Condensed copy from BullyBot.java
     *
     * @param pw The game state
     */
    private void doBullyBotTurn(PlanetWars pw) {
        Planet source = null;
        double strongestScore = Double.MIN_VALUE;
        for (Planet planet : pw.getMyPlanets()) {
            if (planet.getNumShips() <= 1) continue;
            double score = (double) planet.getNumShips();
            if (score > strongestScore) {
                strongestScore = score;
                source = planet;
            }
        }

        Planet dest = null;
        double weakestScore = Double.MAX_VALUE;
        for (Planet planet : pw.getNotMyPlanets()) {
            double score = (double) (planet.getNumShips());
            if (score < weakestScore) {
                weakestScore = score;
                dest = planet;
            }
        }

        if (source != null && dest != null)
            pw.issueOrder(source, dest);
    }

    /**
     * Implementation of the RandomBot strategy
     * <p>
     * Condensed copy from RandomBot.java
     *
     * @param pw The game state
     */
    private void doRandomBotTurn(PlanetWars pw) {
        Random random = new Random();
        Planet source = null;
        List<Planet> myPlanets = pw.getMyPlanets();
        if (myPlanets.size() > 0)
            source = myPlanets.get(random.nextInt(myPlanets.size()));

        Planet dest = null;
        List<Planet> allPlanets = pw.getNotMyPlanets();
        if (allPlanets.size() > 0)
            dest = allPlanets.get(random.nextInt(allPlanets.size()));

        if (source != null && dest != null)
            pw.issueOrder(source, dest);
    }
}
