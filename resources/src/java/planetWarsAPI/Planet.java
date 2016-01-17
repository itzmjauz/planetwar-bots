package planetWarsAPI;

/*
 * You do not need to worry about anything in this file. This is just
 * helper code that does the boring stuff for you, so you can focus on the
 * interesting stuff. That being said, you're welcome to change anything in
 * this file if you know what you're doing.
 */

import java.util.Locale;

/**
 * A data object representing a single Planet in a galaxy far, far away.
 *
 * @author Jur van den Berg
 * @version 2.0
 */
public class Planet {
    private int id;
    private int owner;
    private int numShips;
    private int growthRate;
    private double x, y;

    /**
     * Create an instance of a Planet
     *
     * @param id         unique identifier for the Planet, in relation to gamestate
     * @param owner      owner of the Planet
     * @param numShips   number of ships on the Planet
     * @param growthRate growth rate of the Planet
     * @param x          x location of the Planet
     * @param y          y location of the Planet
     */
    public Planet(int id, int owner, int numShips, int growthRate, double x, double y) {
        this.id = id;
        this.owner = owner;
        this.numShips = numShips;
        this.growthRate = growthRate;
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new instance of a given Planet
     *
     * @param other The Planet to copy data from
     */
    private Planet(Planet other) {
        this.id = other.id;
        this.owner = other.owner;
        this.numShips = other.numShips;
        this.growthRate = other.growthRate;
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * Return the ID of the Planet
     *
     * @return Planet id
     */
    public int getID() {
        return id;
    }

    /**
     * Return the owner of the Planet
     *
     * @return Planet owner
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Return the number of ships stationed at the Planet
     *
     * @return number of ships on the Planet
     */
    public int getNumShips() {
        return numShips;
    }

    /**
     * Return the growth rate of the Planet
     *
     * @return Planet growth rate
     */
    public int getGrowthRate() {
        return growthRate;
    }

    /**
     * Return the x coordinate of the Planet
     *
     * @return Planet x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Return the y coordinate of the Planet
     *
     * @return Planet y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Format information about the Planet in a way the engine can understand it.
     * <p>
     * You are not expected to use this
     *
     * @return Serialized version of Planet
     */
    String serialize() {
        return String.format(Locale.US, "P %f %f %d %d %d\n", x, y, owner, numShips, growthRate);
    }

    /**
     * Generate a nice printable string from a Planet
     *
     * @return Pretty planet-string
     */
    @Override
    public String toString() {
        return String.format("Planet(id=%d, owner=%d, ships=%d, growth=%d, position=[%f, %f])",
            id, owner, numShips, growthRate, x, y);
    }
}
