Intelligent Systems 2016
========================
This is the practical material for the Intelligent Systems course, based on the Google's AI challenge 'Planet Wars'.

The structure of the project has been significantly changed once more. 
This means that last year's program doesn't work anymore, again.

## Getting started

To get to know the concept of the game, please visit [Galcon's website](http://www.galcon.com/).
The challenge was inspired by this project and resembles it a great deal. <br>
(Free downloads for android and iphone [here](http://www.galcon.com/g2/download.php)).

Your job is to make bots, using our API, which is available in python or java.

### Setting up the environment

Install the following platforms (required to make everything function):

- python 2.7
- java jre 1.6+ (the jdk is also required if you're programming the bot in java)
- a nice IDE or text editor (PyCharm/IDEA, Eclipse, vim, or whatever editor you like)

### Bots

A bot is an agent, it gets the current game state and according to the provided strategy will perform an action. Through the API you can see what 
the game is like at that point in time. You can request for example the planets (your planets, enemy planets, or neutral planets), it allows you to
log, and submit commands back to the engine.

API documentation (for both java and python) can be found [here](https://docs.blacknova.io/IS/).

Some examples have already been given like BullyBot and RandomBot (skip AdaptiveBot for now).

**Quick note: if you use print functions in a bot, it cannot be guaranteed to work; use the planetwars.log method instead. 
Most of the time it will just be logged, but it could mess up your orders.**

### Java Developers only

For java developers, you would need to compile the bots. Gradle does this for you (like a makefile), if you want
to use your own IDE's compilation, no additions need to be made if you just compile to a folder `out/` directly.

### Starting the engines

To make your lives easier we created a file `play.py` this serves as a very simple tool to start the engine. To get to
know it you can run `python play.py --help`, it should show something like this:

![Play.py](//imgur.com/PbBryi0.jpg)

When run without arguments it just takes the defaults only and runs the engine to resolve a match.
This will be a match between RandomBot.py and RandomBot.py, on some map, in serial mode (players wait for each other to move, in 
parallel mode both players can move in the same turn), with a maxNumber of turns set to 100 (if no one wins before 100 turns,
it becomes a draw) and turn time of 1000 msec (1 sec, the time allowed for the bot to make a decision)

## Structure

The internal structure of the project is as follows:

**src**: the source files are kept here.

**out**: for java compiled output.

**maps**: for the normal maps.

**simple maps**: for the testing maps.

**tools**: the actual game engine and visualizer are located here. (all done for you in play.py)

## Advanced engine guide

If you want to get into how the engine itself works; the engine is the tools/PlayGame.jar. It, of course, has it's own 
help section.

The engine outputs the json of the game state, which can be used from command line to pipe into the visualizer.
