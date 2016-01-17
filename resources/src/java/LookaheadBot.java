import planetWarsAPI.Bot;
import planetWarsAPI.GameClient;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * LookaheadBot
 * Another smarter kind of bot, which implements a minimax algorithm with look-ahead of two turns.
 * It simulates the opponent using the BullyBot strategy and simulates the possible outcomes for any
 * choice of source and destination planets in the attack. The simulated outcome states are ranked by
 * the evaluation function, which returns the most promising one.
 * <p>
 * Try to improve this bot. For example, you can try to answer some of these questions:
 * - Can you come up with smarter heuristics/scores for the evaluation function?
 * - What happens if you run this bot against your bot from week1?
 * - How can you change this bot to beat your week1 bot?
 * - Can you extend the bot to look ahead more than two turns? How many turns do you want to look ahead?
 * - Is there a smart way to make this more efficient?
 *
 * @author Jur van den Berg
 * @version 2.1
 */
public class LookaheadBot implements Bot {

    /**
     * Static LookaheadBot, used only to access SimulatedPlanetWars (DON'T CHANGE)
     */
    static LookaheadBot dummyBot = new LookaheadBot();

    /**
     * Function that gets called every turn.
     * This is where you add your implementation
     *
     * @param pw The game state
     */
    public void doTurn(PlanetWars pw) {

        double score = Double.MIN_VALUE;
        Planet source = null;
        Planet dest = null;


        // We try to simulate each possible action and its outcome after two turns
        // considering each of my planets as a possible source
        // and each enemy planet as a possible destination
        for (Planet myPlanet : pw.getMyPlanets()) {

            //avoid planets with only one ship
            if (myPlanet.getNumShips() <= 1)
                continue;

            for (Planet notMyPlanet : pw.getNotMyPlanets()) {

                // Create simulation environment - need to create one for each simulation
                SimulatedPlanetWars simpw = createSimulation(pw);

                // (1) simulate my turn with the current couple of source and destination
                simpw.simulateAttack(myPlanet, notMyPlanet);
                // (2) simulate the growth of ships that happens in each turn
                simpw.simulateGrowth();

                // (3) simulate the opponent's turn, assuming that the opponent is the BullyBot
                //     here you can add other opponents
                simpw.simulateBullyBotAttack();
                // (4) simulate the growth of ships that happens in each turn
                simpw.simulateGrowth();


                // (5) evaluate how the current simulated state is
                //     here you can change how a state is evaluated as good
                double scoreMax = evaluateState(simpw);

                // (6) find the planet with the maximum evaluated score
                //     this is the most promising future state
                if (scoreMax > score) {
                    score = scoreMax;
                    source = myPlanet;
                    dest = notMyPlanet;
                }
            }
        }

        // Attack using the source and destinations that lead to the most promising state in the simulation
        if (source != null && dest != null) {
            pw.issueOrder(source, dest);
        }
    }

    /**
     * This function evaluates how promising a simulated state is.
     * <p>
     * You can change it to anything that makes sense, using combinations
     * of number of planets, ships or growth rate.
     *
     * @param pw Simulated instance of the game
     * @return score of the final state of the simulation
     */
    public static double evaluateState(SimulatedPlanetWars pw) {

        // CHANGE HERE

        double enemyShips = 1.0;
        double myShips = 1.0;

        for (Planet planet : pw.getEnemyPlanets()) {
            enemyShips += planet.getNumShips();
        }

        for (Planet planet : pw.getMyPlanets()) {
            myShips += planet.getNumShips();
        }

        return myShips / enemyShips;
    }

    public static void main(String[] args) {
        GameClient.run(new LookaheadBot());
    }

    /**
     * Create the simulation environment. Returns a SimulatedPlanetWars instance.
     * Call every time you want a new simulation environment.
     *
     * @param pw The original PlanetWars object
     * @return SimulatedPlanetWars instance on which to simulate your attacks.
     * Create a new one every time you want to try alternative simulations.
     */
    public static SimulatedPlanetWars createSimulation(PlanetWars pw) {
        return dummyBot.new SimulatedPlanetWars(pw);
    }

    /**
     * Class which provide the simulation environment
     * <p>
     * For information about what each method does, see its equivalent PlanetWars
     *
     * @see PlanetWars
     */
    public class SimulatedPlanetWars {

        List<Planet> planets = new ArrayList<Planet>();

        public SimulatedPlanetWars(PlanetWars pw) {
            for (Planet planet : pw.getAllPlanets()) {
                planets.add(planet);
            }
        }

        public void simulateGrowth() {
            for (Planet p : planets) {

                if (p.getOwner() == 0)
                    continue;

                Planet newPlanet = new Planet(p.getID(), p.getOwner(), p.getNumShips() + p.getGrowthRate(),
                    p.getGrowthRate(), p.getX(), p.getY());

                planets.set(p.getID(), newPlanet);
            }
        }

        public void simulateAttack(int player, Planet source, Planet dest) {
            if (source != null && dest != null) {
                if (source.getOwner() != player) {
                    return;
                }

                // Simulate attack
                int remnantFleet = dest.getNumShips() - source.getNumShips() / 2;
                int owner = dest.getOwner();

                if (remnantFleet < 0)
                    owner = player;

                Planet newSource = new Planet(source.getID(), source.getOwner(), source.getNumShips() / 2,
                    source.getGrowthRate(), source.getX(), source.getY());
                Planet newDest = new Planet(dest.getID(), owner, Math.abs(remnantFleet),
                    dest.getGrowthRate(), dest.getX(), dest.getY());

                planets.set(source.getID(), newSource);
                planets.set(dest.getID(), newDest);
            }
        }

        public void simulateAttack(Planet source, Planet dest) {
            simulateAttack(1, source, dest);
        }


        public void simulateBullyBotAttack() {
            Planet source = null;
            Planet dest = null;

            double sourceScore = Double.MIN_VALUE;
            double destScore = Double.MAX_VALUE;

            for (Planet planet : planets) {

                if (planet.getOwner() == PlanetWars.ENEMY) {
                    if (planet.getNumShips() <= 1) continue;
                    double scoreMax = (double) planet.getNumShips();
                    if (scoreMax > sourceScore) {
                        sourceScore = scoreMax;
                        source = planet;
                    }
                }

                // (2) Find the weakest enemy or neutral planet.
                if (planet.getOwner() != PlanetWars.ENEMY) {
                    double scoreMin = (double) (planet.getNumShips());
                    if (scoreMin < destScore) {
                        destScore = scoreMin;
                        dest = planet;
                    }
                }

            }

            // (3) Simulate attack
            if (source != null && dest != null) {
                simulateAttack(2, source, dest);
            }

        }

        public int numPlanets() {
            return planets.size();
        }

        public Planet getPlanet(int planetID) {
            return planets.get(planetID);
        }

        public List<Planet> getAllPlanets() {
            return planets;
        }


        public List<Planet> getMyPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() == PlanetWars.PLAYER)
                    r.add(p);

            return r;
        }

        public List<Planet> getNeutralPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() == PlanetWars.NEUTRAL)
                    r.add(p);

            return r;
        }

        public List<Planet> getEnemyPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() >= PlanetWars.ENEMY)
                    r.add(p);

            return r;
        }

        public List<Planet> getNotMyPlanets() {
            List<Planet> r = new ArrayList<Planet>();

            for (Planet p : planets)
                if (p.getOwner() != PlanetWars.PLAYER)
                    r.add(p);

            return r;
        }

        public boolean isPlayerAlive(int player) {
            for (Planet p : planets)
                if (p.getOwner() == player)
                    return true;

            return false;
        }

        public int getWinner() {
            Set<Integer> remainingPlayers = new TreeSet<Integer>();
            for (Planet p : planets)
                remainingPlayers.add(p.getOwner());

            switch (remainingPlayers.size()) {
                case 0:
                    return 0;
                case 1:
                    return (Integer) remainingPlayers.toArray()[0];
                default:
                    return -1;
            }
        }

        public int getNumShips(int player) {
            int numShips = 0;

            for (Planet p : planets)
                if (p.getOwner() == player)
                    numShips += p.getNumShips();

            return numShips;
        }

        public void issueOrder(Planet source, Planet dest) {
            simulateAttack(source, dest);
        }
    }
}
