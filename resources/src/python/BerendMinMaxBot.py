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
import _HeuristicV1, _HeuristicV2, _HeuristicV3, _HeuristicV0, _HeuristicV4
import cProfile, pstats, StringIO

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

def generateInitState(pw):
    state = WorldState()
    for planet in pw.planets():
        rep = PlanetState(planet.number_ships(), \
                          planet.growth_rate(), \
                          planet.owner(), \
                          planet.id())
        state.addPlanet(rep)
    return state

def generateChildren(tree, friendly):
    state = tree.getState()

    if friendly:
        p1states = state.getFriendlyPlanets()
    else:
        p1states = state.getEnemyPlanets()

    for p1 in p1states:
        for p2 in state.getPlanets():
            # Generate new state space
            newState = deepcopy(state)
            psource = newState.getPlanetById(p1.getID())
            pdest = newState.getPlanetById(p2.getID())
            psource.sendShipsTo(pdest)
            newState.performGrowth()

            # Create new tree node and add it to children list
            newNode = TreeNode(newState, tree)
            newNode.setMove(p1.getID(), p2.getID())
            tree.addChild(newNode)
    return

def isFinished(state):
    return len(state.getEnemyPlanets()) == 0 or len(state.getFriendlyPlanets()) == 0

def minMax(tree, level, maxPlayer):
    if level == 0 or isFinished(tree.getState()):
        value = _HeuristicV0.heuristicFunction(tree.getState())
        # PlanetWars.log("At level %i, came from %s chosen move %s with value %i" % (level, tree.getParent().getMove(), tree.getMove(), value))
        return (value, tree.getMove())

    generateChildren(tree, maxPlayer)
    results = [minMax(child, level - 1, not maxPlayer) for child in tree.getChildren()]

    if maxPlayer:
        (value, move) = max(results)
    else:
        (value, move) = min(results)

    if tree.getParent() == None:
        # PlanetWars.log("At level %i, chosen move %s with value %f" % (level, move, value))
        return (value, move)
    else:
        # PlanetWars.log("At level %i, came from %s chosen move %s with value %f" % (level, tree.getParent().getMove(), tree.getMove(), value))
        return (value, tree.getMove())

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
    PlanetWars.log("Amount of planets: %i" % len(gameState.getPlanets()))
    # PlanetWars.log("Started run")
    # PlanetWars.log(formatState(gameState))

    gameTree = TreeNode(gameState, None)

    #benchmark
    start = time.clock()
    pr = cProfile.Profile()
    pr.enable()

    (value, move) = minMax(gameTree, 3, True)

    source = pw.get_planet(move[0])
    destination = pw.get_planet(move[1])
    pr.disable()

    end = time.clock()

    PlanetWars.log("Action took %f sec" % (end - start))
    PlanetWars.log("Selected action %s" % str(move))

    ## DEBUG: Dump profiling results to file
    sortby = "cumulative"
    s = StringIO.StringIO()
    ps = pstats.Stats(pr, stream=s).sort_stats(sortby)
    ps.print_stats()
    # PlanetWars.log(s.getvalue())

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
