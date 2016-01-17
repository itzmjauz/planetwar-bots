class Planet:
    """A data object representing a single planet.

    @author Arthur Fluiter
    @author Jur van den Berg
    """

    ## ID for current player
    ME = 1
    ## ID for neutral
    NEUTRAL = 0
    ## ID for enemies
    ENEMY = 2

    def __init__(self, planet_id, owner, num_ships, growth_rate, x, y):
        """Create an instance of a Planet

        @param planet_id unique identifier for planet
        @type planet_id : int
        @param owner owner of the planet
        @type owner : int
        @param num_ships number of ships on the planet
        @type num_ships : int
        @param growth_rate growth rate of the planet
        @type growth_rate : int
        @param x x location of the planet
        @type x : float
        @param y y location of the planet
        @type y : float
        """
        self._planet_id = planet_id
        self._owner = owner
        self._num_ships = num_ships
        self._growth_rate = growth_rate

        # coordinates in the space.
        self._x = x
        self._y = y

    def clone(self):
        """Clone the current planet

        @return another Planet instance with the same attributes.
        @rtype : Planet
        """
        return Planet(self._planet_id, self._owner, self._num_ships, self._growth_rate, self._x, self._y)

    def id(self):
        """Return the ID of the planet

        @return planet id
        @rtype : int
        """
        return self._planet_id

    def owner(self):
        """Returns the owner of the planet

        @return planet owner
        @rtype : int
        """
        return self._owner

    def is_mine(self):
        """Check if planet is owned by me

        @return True if planet is owned by me
                False otherwise
        @rtype : bool
        """
        return self._owner is Planet.ME

    def is_enemy(self):
        """Check if planet is owned by the enemy

        @return True if planet is owned the enemy
                False otherwise
        @rtype : bool
        """
        return self._owner is Planet.ENEMY

    def is_neutral(self):
        """Check if planet is not owned by anyone

        @return True if planet is not owned by anyone
                False otherwise
        @rtype : bool
        """
        return self._owner is Planet.NEUTRAL

    """these are the functions to change ownership"""

    def set_owner(self, owner_id):
        """Set owner of this planet

        @param owner_id id of new owner
        @type owner_id : int
        """
        self._owner = owner_id

    def set_enemy(self):
        """Set owner of this planet to the enemy"""
        self.set_owner(Planet.ENEMY)

    def set_mine(self):
        """Set owner of this planet to the player"""
        self.set_owner(Planet.ME)

    def set_neutral(self):
        """Set owner of this planet to no-one"""
        self.set_owner(Planet.NEUTRAL)

    def number_ships(self):
        """Return the number of ships at this planet

        @return number of ships
        @rtype : int
        """
        return self._num_ships

    def set_number_ships(self, ships):
        """Set the number of ships at this planet

        @param ships new quantity of ships
        @type ships : int
        """
        self._num_ships = ships

    def growth_rate(self):
        """Return the growth rate of this planet

        @return growth rate
        @rtype : int
        """
        return self._growth_rate

    def __str__(self):
        """Return a serialized version of the planet

        This is the format used by the game engine.
        @return serialized planet state
        @rtype : str
        """
        return "P %f %f %d %d %d" % (self._x, self._y, self._owner, self._num_ships, self._growth_rate)

    def __repr__(self):
        return "Planet(id=%d, owner=%d, ships=%d, growth=%d, position=[%f, %f])" % \
               (self._planet_id, self._owner, self._num_ships, self._growth_rate, self._x, self._y)
