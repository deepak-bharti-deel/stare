#!/usr/bin/env bash
#production script
while [ 0 ]
do
	sleep 1
	window=`xdotool getwindowfocus getwindowname`
	date_s=`date +"%Y %m %d %T"`
	echo "$window <--> $date_s"
	window_prev=$window
done
