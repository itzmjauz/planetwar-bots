def heuristicFunction(state):
    if len(state.getEnemyPlanets()) == 0:
        return float('inf')
    elif len(state.getFriendlyPlanets()) == 0:
        return float('-inf')
    # Scaling constant
    c1 = 0.5

    shipscore_player = 0
    growscore_player = 0
    for planet in state.getFriendlyPlanets():
        shipscore_player += planet.getShips()
        growscore_player += planet.getRate()

    growscore_adversary = 0
    shipscore_adversary = 0
    for planet in state.getEnemyPlanets():
        shipscore_adversary += planet.getShips()
        growscore_adversary += planet.getRate()


    strength_factor = 1
    for p1 in state.getFriendlyPlanets():
        for p2 in state.getEnemyPlanets():
            strength_factor *= (float(p1.getShips()) / p2.getShips())

    value = strength_factor * (shipscore_player - shipscore_adversary + c1 \
                                * (growscore_player - growscore_adversary))
    return value
