def heuristicFunction(state):
    if len(state.getEnemyPlanets()) == 0:
        return float('inf')
    elif len(state.getFriendlyPlanets()) == 0:
        return float('-inf')

    # Scaling constant
    c1 = 1

    shipscore_player = 0
    growscore_player = 0
    for planet in state.getFriendlyPlanets():
        shipscore_player += planet.getShips()
        growscore_player += planet.getRate()

    value = shipscore_player + growscore_player

    return value
