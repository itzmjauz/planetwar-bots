package planetWarsAPI;

/*
 * You do not need to worry about anything in this file. This is just
 * helper code that does the boring stuff for you, so you can focus on the
 * interesting stuff. That being said, you're welcome to change anything in
 * this file if you know what you're doing.
 */

import java.util.*;

/**
 * A class representing a single state of PlanetWars.
 * Also grants access to command the PlanetWars Engine.
 *
 * @author Jur van den Berg
 * @version 2.0
 */
public class PlanetWars {
    /**
     * Identifier for no ownership
     */
    public static final int NEUTRAL = 0;
    /**
     * Identifier for player ownership
     */
    public static final int PLAYER  = 1;
    /**
     * Identifier for enemy ownership
     */
    public static final int ENEMY   = 2;

    /**
     * List of planets in this instance
     */
    private ArrayList<Planet> planets;

    /**
     * Cached versions, for performance.
     */
    private ArrayList<Planet> cachedMyPlanets;
    private ArrayList<Planet> cachedNotMyPlanets;
    private ArrayList<Planet> cachedNeutralPlanets;
    private ArrayList<Planet> cachedEnemyPlanets;

    /**
     * Construct an empty PlanetWars instance
     */
    public PlanetWars() {
        planets = new ArrayList<Planet>();
    }

    /**
     * Construct a PlanetWars instance
     *
     * @param gameState Description of game state, produced by the engine
     */
    public PlanetWars(String gameState) {
        planets = new ArrayList<Planet>();
        parseGameState(gameState);
    }

    /**
     * Return the number of planets
     *
     * @return the number of planets
     */
    public int numPlanets() {
        return planets.size();
    }

    /**
     * Retrieve a given planet by ID
     *
     * @param planetID id of Planet to retrieve.
     *                  id numbering starts at 0.
     * @return planet referenced by id, or null if it did not exist
     */
    public Planet getPlanet(int planetID) {
        return planets.get(planetID);
    }

    /**
     * Retrieve a list of all planets
     *
     * @return list of planets
     */
    public List<Planet> getAllPlanets() {
        return planets;
    }

    /**
     * Return a list of all planets owned by the current player.
     *
     * @return list of planets
     */
    public List<Planet> getMyPlanets() {
        if (cachedMyPlanets != null)
            return cachedMyPlanets;

        cachedMyPlanets = new ArrayList<Planet>();

        for (Planet p : planets)
            if (p.getOwner() == PLAYER)
                cachedMyPlanets.add(p);

        return cachedMyPlanets;
    }

    /**
     * Return a list of all planets not owned by any player
     *
     * @return list of planets
     */
    public List<Planet> getNeutralPlanets() {
        if (cachedNeutralPlanets != null)
            return cachedNeutralPlanets;

        cachedNeutralPlanets = new ArrayList<Planet>();

        for (Planet p : planets)
            if (p.getOwner() == NEUTRAL)
                cachedNeutralPlanets.add(p);

        return cachedNeutralPlanets;
    }

    /**
     * Return a list of all planets owned by any enemy
     *
     * @return list of planets
     */
    public List<Planet> getEnemyPlanets() {
        if (cachedEnemyPlanets != null)
            return cachedEnemyPlanets;

        cachedEnemyPlanets = new ArrayList<Planet>();

        for (Planet p : planets)
            if (p.getOwner() >= ENEMY)
                cachedEnemyPlanets.add(p);

        return cachedEnemyPlanets;
    }

    /**
     * Return a list of all planets not owned by the player
     *
     * @return list of planets
     */
    public List<Planet> getNotMyPlanets() {
        if (cachedNotMyPlanets != null)
            return cachedNotMyPlanets;

        cachedNotMyPlanets = new ArrayList<Planet>();

        for (Planet p : planets)
            if (p.getOwner() != PLAYER)
                cachedNotMyPlanets.add(p);

        return cachedNotMyPlanets;
    }

    /**
     * Sends an order to the game engine.
     * This will trigger the engine to send half of the ships on the source Planet
     * to the destination Planet.
     * <p>
     * Keep these things in mind:
     *   * The planets are indexed, starting at zero
     *   * You must own the source Planet.
     *     If you order from an enemy Planet, the engine will kick you out immediately.
     *
     * @param source      id of the source planet
     * @param destination id of the destination planet
     */
    public void issueOrder(int source, int destination) {
        System.out.println("" + source + " " + destination);
        System.out.flush();
    }

    /**
     * Sends an order to the game engine.
     * Convenience wrapper to allow you to issue orders between Planet instances
     *
     * @param source      source Planet
     * @param destination destination Planet
     * @see PlanetWars#issueOrder(int, int)
     */
    public void issueOrder(Planet source, Planet destination) {
        issueOrder(source.getID(), destination.getID());
    }

    /**
     * Notify the game engine that we have finished our turn.
     * <p>
     * Make sure you have issued your order before you call this method.
     * All further orders will be discarded by the engine.
     */
    public void finishTurn() {
        System.out.println("go");
        System.out.flush();
    }

    /**
     * Return the total number of ships the given player has.
     *
     * @param player id of the player
     * @return number of ships owned by player
     */
    public int getNumShips(int player) {
        int numShips = 0;

        for (Planet p : planets)
            if (p.getOwner() == player)
                numShips += p.getNumShips();

        return numShips;
    }

    /**
     * Modifies the current instance to represent the game state
     *
     * @param gameState description of game state
     * @return true on success
     * false on failure
     */
    private boolean parseGameState(String gameState) {
        Locale.setDefault(Locale.US);
        planets.clear();
        int planetID = 0;

        String[] lines = gameState.split("\n");

        for (String line : lines) {
            // Remove comment from line if it exists
            int commentBegin = line.indexOf('#');
            if (commentBegin >= 0)
                line = line.substring(0, commentBegin);

            // Skip if line is empty
            // Required as "".split(" ").length == 1
            if (line.trim().length() == 0)
                continue;

            // Skip if line has no tokens
            String[] tokens = line.split(" ");
            if (tokens.length == 0)
                continue;

            if (tokens[0].equals("P")) {
                // Planet token layout: ["P", x, y, owner, numShips, growthRate]
                if (tokens.length != 6)
                    return false;

                double x = Double.parseDouble(tokens[1]);
                double y = Double.parseDouble(tokens[2]);
                int owner = Integer.parseInt(tokens[3]);
                int numShips = Integer.parseInt(tokens[4]);
                int growthRate = Integer.parseInt(tokens[5]);

                planets.add(new Planet(planetID++, owner, numShips, growthRate, x, y));
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Log list of parameters, concatenated by spaces, to output of engine.
     * <p>
     * Normal println will not help as the output is consumed by the engine.
     *
     * @param args Objects to log
     */
    public void log(Object... args) {
        StringBuilder s = new StringBuilder();

        for (Object arg : args)
            s.append(arg).append(" ");

        System.err.println(s.toString());
        System.err.flush();
    }

    /** Generate a state string from the current instance
     *
     * @return parsable game state
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Planet p : planets)
            s.append(p.serialize());

        return s.toString();
    }
}
