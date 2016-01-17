import sys
from PlanetWarsAPI import Planet


class PlanetWars:
    """A class representing a single state of PlanetWars.
    Also grants access to command the PlanetWars Engine.

    Attributes:
        done: True if the game has finished, false otherwise

    @author Arthur Fluiter
    @author Jur van den Berg
    """

    def __init__(self, game_state=None, clone=None):
        """Constructs a PlanetWars object instance
        given a string containing a description of a game state.

        @param game_state String representing the game state. If it equals None, begin reading from stdin
        @type game_state : str
        @param clone If clone is not None, copy the game state from clone
        @type clone : bool
        """
        # Container for Planets, please use the planets() method to access
        self._planets = []

        # for performance, these will only be created once
        self._cached_mine = None
        self._cached_neutral = None
        self._cached_enemy = None
        self._cached_not_mine = None

        self.done = False

        # If a clone needs to be made
        if clone is not None:
            self._planets = [p.clone() for p in clone._planets]
            return

        # Originally the agents had to run this code, which looked ugly (backwards compatible)
        if game_state is None:
            game_state = []
            while True:
                line = raw_input()
                if len(line) >= 2 and line.startswith("go"):
                    break
                game_state.append(line)
        elif isinstance(game_state, basestring):
            game_state = game_state.split("\n")

        self.parse_game_state(game_state)

    def number_planets(self):
        """Return the number of planets

        @return the number of planets
        @rtype : int
        """

        return len(self._planets)

    def get_planet(self, planet_id):
        """Retrieve a given planet by ID

        @param planet_id id of planet to retrieve.
                         id numbering starts at 0.
        @type planet_id : int
        @return Planet referenced by id
        @rtype : Planet
        """

        # planet id must be in a valid range
        if planet_id < 0 or planet_id > len(self._planets):
            sys.stderr.write("Invalid Index: %d (There are %d planets)" % (planet_id, len(self._planets)))

        return self._planets[planet_id]

    def planets(self):
        """Returns a list of all planets.

        @return list of planets
        @rtype : list[Planet]
        """

        return self._planets

    def my_planets(self):
        """Returns a list of all planets owned by the current player.

        @return list of planets
        @rtype : list[Planet]
        """
        if self._cached_mine is None:
            self._cached_mine = [planet for planet in self._planets if planet.owner() is Planet.ME]
        return self._cached_mine

    def neutral_planets(self):
        """Returns a list of all planets not owned by any player.

        @return list of planets
        @rtype : list[Planet]
        """
        if self._cached_neutral is None:
            self._cached_neutral = [planet for planet in self._planets if planet.owner() is Planet.NEUTRAL]
        return self._cached_neutral

    def enemy_planets(self):
        """Returns a list of all planets owned by the enemy.

        @return list of planets
        @rtype : list[Planet]
        """

        if self._cached_enemy is None:
            self._cached_enemy = [planet for planet in self._planets if planet.is_enemy()]
        return self._cached_enemy

    def not_my_planets(self):
        """Returns a list of all planets not owned by the player.

        @return list of planets
        @rtype : list[Planet]
        """

        if self._cached_not_mine is None:
            self._cached_not_mine = [planet for planet in self._planets if planet.owner() is not Planet.ME]
        return self._cached_not_mine

    def issue_order_id(self, source_id, destination_id, final=False):
        """Sends an order to the game engine using the ids of the planets.

        This will trigger the engine to send half of the ships on the source planet
        to the destination planet.

        Keep these things in mind:
            * The planets are indexed, starting at zero
            * You must own the source planet.
              If you order from an enemy planet, the engine will kick you out immediately.

        @param source_id id of the source planet
        @type source_id : int
        @param destination_id id of the destination planet
        @type destination_id : int
        @param final If True, immediately end the turn
        @type final : bool
        """

        # verify that id's are valid planets
        self.get_planet(source_id)
        self.get_planet(destination_id)

        # communicate move to engine
        print("%d %d" % (source_id, destination_id))
        if final:
            print("go")
            self.done = True
        sys.stdout.flush()

    def issue_order(self, source, destination, final=False):
        """Sends an order to the game engine.

        This will trigger the engine to send half of the ships on the source planet
        to the destination planet.

        Keep these things in mind:
            * The planets are indexed, starting at zero
            * You must own the source planet.
              If you order from an enemy planet, the engine will kick you out immediately.

        @param source Planet to send from
        @type source : Planet
        @param destination Planet to send to
        @type destination : Planet
        @param final If True, immediately end the turn
        @type final : bool
        """
        dst_id = src_id = 0

        if source is None or not isinstance(source, Planet):
            self.log("issue_order's source planet was not a planet")
        elif destination is None or not isinstance(destination, Planet):
            self.log("issue_order's destination planet was not a planet")
        else:
            if not source.is_mine():
                self.log("issue order was called with invalid source planet (not mine)")
            src_id = source.id()
            dst_id = destination.id()

        self.issue_order_id(src_id, dst_id, final=final)

    def finish_turn(self):
        """Notify the game engine that we have finished our turn.

        Make sure you have issued your order before you call this method.
        All further orders will be discarded by the engine.

        """
        if not self.done:
            print("go")
            sys.stdout.flush()
            self.done = True

    def statistics(self):
        """Retrieve a dict of various pieces of state

        @return Dict of various stats
        @rtype : dict[str,int]
        """
        my_planets = self.my_planets()
        en_planets = self.enemy_planets()

        stats = {
            'my_ships': sum(p._num_ships for p in my_planets),
            'my_growth': sum(p._growth_rate for p in my_planets),
            'my_planets': len(my_planets),
            'enemy_ships': sum(p._num_ships for p in en_planets),
            'enemy_growth': sum(p._growth_rate for p in en_planets),
            'enemy_planets': len(self.enemy_planets())
        }

        return stats

    def parse_game_state(self, lines):
        """Modifies the current instance to represent the game state

        @param lines Description of game state
        @type lines : str
        @return 1 on success
                0 on failure

        @rtype : int
        """
        self._planets = []
        planet_id = 0

        for line in lines:
            line = line.split("#")[0]  # remove comments
            tokens = line.split(" ")
            if len(tokens) == 1:
                continue
            if tokens[0] == "P":
                if len(tokens) != 6:
                    raise Exception("Error in parsing the game state, please contact the TA's (too many tokens)")
                p = Planet(planet_id,  # The ID of this planet
                           int(tokens[3]),  # Owner
                           int(tokens[4]),  # Num ships
                           int(tokens[5]),  # Growth rate
                           float(tokens[1]),  # X
                           float(tokens[2]))  # Y
                planet_id += 1
                self._planets.append(p)
            elif tokens[0] is 'F':
                # ignore
                self.log("Fleet detected, this is not supposed to happen")
            else:
                raise Exception("Error in parsing the game state, please contact the TA's (unknown token)")

    def __str__(self):
        """Generate a state string from the current instance

        @return parsable game state
        @rtype : str
        """
        return "\n".join(str(p) for p in self._planets)

    @staticmethod
    def log(*args):
        """Log list of parameters, concatenated by spaces, to output of engine.

        Normal println will not help as the output is consumed by the engine

        @param args Objects to log
        """
        lst = [str(arg) for arg in args]
        # needed, otherwise line won't show in console
        lst.append('\n')
        sys.stderr.write(' '.join(lst))
        sys.stderr.flush()
