#!/bin/sh

botname="$1"
resultfile="$2"
bots_to_fight=`ls -1 src/python | grep Bot.py | grep -v .pyc | grep -v Berend`

rm $resultfile
if [ -z $2 ]
then
	echo "syntax ./benchmark <botname> <resultfile> <small>(opt) <timeout>(opt)"
	exit 1
fi

if [ "$3" = "small" ]
then
	maps=`ls -1 maps/ | grep -v "larger"`
	echo "Running on small (<= 8 planets) maps"
else
	maps=`ls -1 maps/`
	echo "Running all maps"
fi

if [ -z $4 ]
then
	time=""
else
	echo "running with $4 seconds time limit" 
	time="-t $4"
fi

for type in $maps; do
	for map in `ls -1 maps/$type`; do
		for bot in $bots_to_fight; do
			echo "Running $botname vs $bot on maps/$type/$map" >> $resultfile
			#echo "python ./play.py -m "maps/$type/$map" -1 "$botname" -2 "$bot""
			python ./play.py -m "maps/$type/$map" -1 "$botname" -2 "$bot" $time >> "$resultfile"
			turns=`cat log.txt | grep "Game state turn" | tail -1`
			timeouts=`cat log.txt | grep "Player 1 : timeout" | wc -l`
			echo "Game took $turns turns, had $timeouts timeouts" >> "$resultfile"
		done
	done		
done

win=`cat $resultfile | grep "Player 1" | wc -l`
lose=`cat $resultfile | grep "Player 2" | wc -l`
draw=`cat $resultfile | grep "Draw" | wc -l`
timeout=`cat $resultfile | grep "timeout" | grep -v "0 timeouts" | wc -l`
echo "Summary: $win wins, $lose losses, $draw draws, $timeout timeouts" >> $resultfile
echo "Summary: $win wins, $lose losses, $draw draws, $timeout timeouts"
