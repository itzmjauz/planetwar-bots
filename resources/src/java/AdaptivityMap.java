/**
 * An adaptivity map implementation
 * <p/>
 * In this example, we provide two environment parameters:
 * - number of neutral planets
 * - average growth ratio of neutral planets
 * <p/>
 * We use these to determine what bot to use.
 * <p/>
 * The first upper left element of the botValue can be read as: "given that the map has 0 neutral planets
 * and that the average growth rate of the neutral planets in the map is 0, then use the RandomBot";
 * the next element to the right: "given 0 neutral planets and an average growth of 1, use BullyBot;
 * one down the upper left: given 1 neutral planet and average growth 0, use RandomBot";
 * <p/>
 * The given example is random and does not have to be smart at all. We recommend start editing this array to
 * get used to adaptivity and discover which other features would make your bot more smartly adaptive.
 *
 * @author Jur van den Berg
 * @author Arthur de Fluiter
 * @version 3.0
 */
class AdaptivityMap {
    private static String[][] botValue = {
        // Average growth ratio of:
        //    0            1            2            3            4            5
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"}, // 1 neutral planet
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"}, // 0 neutral planets on the map
        {"BullyBot",  "RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot"}, // 2 neutral planets
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "RandomBot"},// ...
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "RandomBot", "RandomBot", "BullyBot",  "RandomBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "BullyBot",  "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "RandomBot", "RandomBot"},
        {"BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "RandomBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"BullyBot",  "BullyBot",  "RandomBot", "RandomBot", "BullyBot",  "RandomBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "BullyBot",  "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "RandomBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "RandomBot", "BullyBot"},
        {"RandomBot", "BullyBot",  "RandomBot", "RandomBot", "BullyBot",  "RandomBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"BullyBot",  "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "RandomBot"},
        {"RandomBot", "BullyBot",  "RandomBot", "RandomBot", "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"BullyBot",  "RandomBot", "BullyBot",  "RandomBot", "BullyBot",  "RandomBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"RandomBot", "BullyBot",  "BullyBot",  "RandomBot", "BullyBot",  "BullyBot"},
        {"BullyBot",  "RandomBot", "BullyBot",  "BullyBot",  "BullyBot",  "BullyBot"} //25
    };

    /**
     * Get the bot name for these environment characteristics
     */
    public static String get(int neutralPlanets, int planetsSize) {
        // if it can't find the proper index
        if (neutralPlanets < 0 || neutralPlanets > botValue.length ||
            planetsSize < 0 || planetsSize > botValue[neutralPlanets].length) {
            return null;
        }

        return botValue[neutralPlanets][planetsSize];
    }
}
