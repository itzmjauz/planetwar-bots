#!/usr/bin/env python
"""
EmptyBot - a skeleton of a bot that you can modify.
Over-commented for educational purposes.
"""

# Import the PlanetWars class from the PlanetWars module. (The planet class here is not necessary
# You can use the planet class to get specific information about a particular planet
from PlanetWarsAPI import PlanetWars, Planet
from math import ceil, floor
from copy import deepcopy
from _Helper import *
import time
import _HeuristicV0, _HeuristicV1, _HeuristicV2, _HeuristicV3, _HeuristicV4

def formatState(state):
    string = "State dump: \n"
    for p in state.getPlanets():
        string += "Planet %i \t has  %i \t ships from player %i \n" \
                    % (p.getID(), p.getShips(), p.getOwner())
    return string

def formatResult(moves):
    string = "Result chosen from %i possible moves\n" % (len(moves))
    string += "chosen state %s \n" % (str(moves[0][0:2]))
    string += "Which is predicted to result in \n"
    string += formatState(moves[0][3])
    return string

def getStateValue(state, source, dest):
    value = _HeuristicV1.heuristicFunction(state)
    return (value, source, dest, state)

def generateInitState(pw):
    state = WorldState()
    for planet in pw.planets():
        rep = PlanetState(planet.number_ships(), \
                          planet.growth_rate(), \
                          planet.owner(), \
                          planet.id())
        state.addPlanet(rep)
    return state

def hillClimb(state):
    moves = []
    for p1 in state.getFriendlyPlanets():
        for p2 in state.getEnemyPlanets():
            # Generate variations on state space
            newState = deepcopy(state)
            psource = newState.getPlanetById(p1.getID())
            pdest = newState.getPlanetById(p2.getID())
            psource.sendShipsTo(pdest)
            newState.performGrowth()

            moves.append(getStateValue(newState, p1.getID(), p2.getID()))
        for p2 in state.getNeutralPlanets():
            # Generate variations on state space
            newState = deepcopy(state)
            psource = newState.getPlanetById(p1.getID())
            pdest = newState.getPlanetById(p2.getID())
            psource.sendShipsTo(pdest)
            newState.performGrowth()

            moves.append(getStateValue(newState, p1.getID(), p2.getID()))
    return moves

def do_turn(pw):
    """
    Function that gets called every turn.
    This is where to implement the strategies.

    Notice that a PlanetWars object called pw is passed as a parameter which you could use
    if you want to know what this object does, then read the API.

    The next line is to tell PyCharm what kind of object pw is (A PlanetWars object here)

    @type pw: PlanetWars
    """
    # The source variable will contain the planet from which we send the ships.
    # Create a source planet, if you want to know what this object does, then read the API
    source = None
    # The dest variable will contain the destination, the planet to which we send the ships.
    destination = None

    # generate state space from pw object
    gameState = generateInitState(pw)

    start = time.clock()
    ## Iterate over all possible positions and calculate score
    ### Right now, only iterate through all player planets and iterate
    moves = []
    moves = hillClimb(gameState)

    # Decide which actions can be taken for a selected planet
     ## Consist of: self, neutral, adversary
    if (len(moves) > 0):
        moves.sort(reverse=True)
        source = pw.get_planet(moves[0][1])
        destination = pw.get_planet(moves[0][2])

    end = time.clock()

    PlanetWars.log(formatResult(moves))
    PlanetWars.log("Action took %f sec" % (end - start))

    # (3) Attack/Defend
    # If the source and destination variables contain actual planets, then
    # send half of the ships from source to destination.
    if source is not None and destination is not None:
        pw.issue_order(source, destination)


def main():
    while True:
        # get the new state of the game
        pw = PlanetWars()

        # make a turn (your code)
        do_turn(pw)

        # finish the turn
        pw.finish_turn()

# If this is the main program -> execute main, catch Ctrl-C if pressed
if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print 'ctrl-c, leaving ...'
