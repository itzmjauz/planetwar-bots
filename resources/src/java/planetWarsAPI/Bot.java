package planetWarsAPI;

/*
 * You do not need to worry about anything in this file. This is just
 * helper code that does the boring stuff for you, so you can focus on the
 * interesting stuff. That being said, you're welcome to change anything in
 * this file if you know what you're doing.
 */

/**
 * The interface which defines everything a bot needs to have
 * to be considered a bot.
 * All your bots should implement this.
 *
 * Example:
 * <pre>{@code
 * class EmptyBot implements Bot
 * }</pre>
 *
 * @author Jur van den Berg
 * @version 1.0
 */
public interface Bot {
    /**
     * Function that gets called every turn.
     * <p>
     * This is where you add your implementation
     *
     * @param pw The game state
     */
    void doTurn(PlanetWars pw);
}
