from PlanetWarsAPI import PlanetWars, Planet
from math import ceil, floor

class PlanetState:
    ships = 0
    growthRate = 0
    owner = Planet.NEUTRAL
    iden = 0

    def __init__(self, ships, growthRate, owner, iden):
        self.ships = ships
        self.growthRate = growthRate
        self.owner = owner
        self.iden = iden

    def getID(self):
        return self.iden

    def getShips(self):
        return self.ships

    def getRate(self):
        return self.growthRate

    def getOwner(self):
        return self.owner

    def isNeutral(self):
        return self.owner == Planet.NEUTRAL

    def isEnemy(self):
        return self.owner == Planet.ENEMY

    def isFriendly(self):
        return self.owner == Planet.ME

    def growFleet(self):
        self.ships += self.growthRate

    def sendShipsTo(self, target):
        send = ceil(self.ships / 2.0);
        self.ships = floor(self.ships / 2.0);
        if (target.getOwner() == self.getOwner()):
            target.ships += send
        else:
            target.ships -= send
            if (target.ships <= 0):
                target.owner = self.getOwner()
                target.ships = abs(target.ships)

class WorldState:
    planets = []

    def __init__(self):
        self.planets = []

    def addPlanet(self, planet):
        self.planets.append(planet)

    def getPlanets(self):
        return self.planets

    def getPlanetById(self, iden):
        for p in self.planets:
            if p.getID() == iden:
                return p
        return None

    def getFriendlyPlanets(self):
        return [p for p in self.planets if p.isFriendly()]

    def getNeutralPlanets(self):
        return [p for p in self.planets if p.isNeutral()]

    def getEnemyPlanets(self):
        return [p for p in self.planets if p.isEnemy()]

    def performGrowth(self):
        for p in self.planets:
            if not p.isNeutral():
                p.growFleet()

class TreeNode:
    state = None
    parent = None
    score = None
    children = []
    move = None

    def __init__(self, state, parent):
        self.state = state
        self.parent = parent
        self.children = []

    def addChild(self, childNode):
        self.children.append(childNode)

    def getChildren(self):
        return self.children

    def getParent(self):
        return self.parent

    def getState(self):
        return self.state

    def setScore(self, score):
        self.score = score

    def setMove(self, src, dest):
        self.move = (src, dest)

    def getMove(self):
        return self.move

    def getScore(self):
        return self.score
