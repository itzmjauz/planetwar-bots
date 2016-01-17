#!/usr/bin/env python

"""
BullyBot - A bit smarter kind of bot, who searches for its strongest planet and then attacks the weakest planet.
The score is computed based on the number of ships.
"""
# Import the PlanetWars class from the PlanetWars module.
from PlanetWarsAPI import PlanetWars


def do_turn(pw):
    """@type pw: PlanetWars"""

    # The source variable will contain the planet from which we send the ships.
    source = None
    # The dest variable will contain the destination, the planet to which we send the ships.
    dest = None

    source_score = -1  # source score must be as large as possible (start with very low score)
    dest_score = 100000  # destination score must be as little as possible (start with high score)

    # Find my strongest planet (highest amount of ships)
    for my_planet in pw.my_planets():
        score_max = my_planet.number_ships()
        if score_max > source_score:
            source_score = score_max
            source = my_planet

    # Find the weakest enemy or neutral planet (lowest amount of ships).
    for not_my_planet in pw.not_my_planets():
        score_min = not_my_planet.number_ships()
        if score_min < dest_score:
            dest_score = score_min
            dest = not_my_planet

    # Attack.
    # If the source and dest variables contain actual planets, then send half of the ships from source to dest.
    if source is not None and dest is not None:
        pw.issue_order(source, dest)


# Don't change from this point on. Also not necessary to understand all the details.
# Machinery that reads the status of the game and puts it into PlanetWars.
# It calls do_turn.
def main():
    while True:
        pw = PlanetWars()
        do_turn(pw)
        pw.finish_turn()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print 'ctrl-c, leaving ...'
