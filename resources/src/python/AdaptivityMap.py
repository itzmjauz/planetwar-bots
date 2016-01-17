#!/usr/bin/env python

"""
For a TL;DR of how AdaptiveBot and AdaptivityMap work, see AdaptiveBot.py
AdaptivityMap - An adaptivity map implementation.

In this example, we provide two environment parameters (number of neutral planets, average growth ratio of neutral
planets), and we match them to a specific bot.

The first upper left element of the botValue can be read as: "given that the map has 0 neutral planets and that the
average growth rate of the neutral planets in the map is 0, then use the RandomBot"; the next element to the right:
"given 0 neutral planets and an average growth of 1, use BullyBot; one down the upper left: given 1 neutral planet and
average growth 0, use RandomBot". The given example is random and does not have to be smart at all. We recommend start
editing this array to get used to adaptivity and discover which other features would make your bot more
smartly adaptive.
"""


class AdaptivityMap:
    def __init__(self):
        # A matrix in two dimensions: the columns represent different planet growth rates (range: 0-5)
        # the rows the number of neutral planets (range: 0-25).
        # The values are completely random, you are encourage to fit in the table your own data.
        # Possibly you can also add your other bots you developed.

        self.adaptive_map = {
            # average growth ratio per planet ->
            #      0               1               2               3               4               5
            0: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],  # 0 neutral planets
            1: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],  # 1 neutral planet
            2: ["BullyBot", "RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot"],  # 2 neutral planets
            3: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "RandomBot"],  # ...
            4: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],
            5: ["RandomBot", "BullyBot", "RandomBot", "RandomBot", "BullyBot", "RandomBot"],
            6: ["LookaheadBot", "LookaheadBot", "LookaheadBot", "BullyBot", "BullyBot", "BullyBot"],
            7: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "RandomBot", "RandomBot"],
            8: ["BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot", "BullyBot"],
            9: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "RandomBot"],
            10: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],
            11: ["BullyBot", "BullyBot", "RandomBot", "RandomBot", "BullyBot", "RandomBot"],
            12: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],
            13: ["RandomBot", "BullyBot", "BullyBot", "BullyBot", "BullyBot", "BullyBot"],
            14: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "RandomBot"],
            15: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "RandomBot", "BullyBot"],
            16: ["RandomBot", "BullyBot", "RandomBot", "RandomBot", "BullyBot", "RandomBot"],
            17: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],
            18: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],
            19: ["BullyBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "RandomBot"],
            20: ["RandomBot", "BullyBot", "RandomBot", "RandomBot", "BullyBot", "BullyBot"],
            21: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],
            22: ["BullyBot", "RandomBot", "BullyBot", "RandomBot", "BullyBot", "RandomBot"],
            23: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],
            24: ["RandomBot", "BullyBot", "BullyBot", "RandomBot", "BullyBot", "BullyBot"],  # 24 neutral planets
            25: ["BullyBot", "RandomBot", "BullyBot", "BullyBot", "BullyBot", "BullyBot"]  # 25 neutral planets
        }

    def get_best_bot(self, num_neutral_planets, average_growth_rate):
        """Returns best bot based on number of neutral planets and average growth rate

        @param num_neutral_planets Number of neutral planets
        @type num_neutral_planets : int
        @param average_growth_rate Average growth rate
        @type average_growth_rate : int
        @rtype : str
        """
        if num_neutral_planets not in self.adaptive_map.keys():
            return None
        row = self.adaptive_map[num_neutral_planets]
        if average_growth_rate > len(row):
            return None
        return row[average_growth_rate]
