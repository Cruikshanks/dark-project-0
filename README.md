# Dark Project 0

> Do not use this as a current reference. This code is now almost 20 years old!

<img src="/screenshots/title_screen.gif" alt="Screenshot of Dark Project 0 title screen" />

This was the next game I created back in 2002 after my initial attempt [Death Pit](https://github.com/Cruikshanks/death-pit-bb).

> *You play the role of a pilot, picked from Earth's greatest to take on the invading Tylan fleet single handed.*

In actual fact the idea for this game stemmed from just such descriptions, and was intended to try and create a back story, and in-game features to make the idea of a single pilot being Earth's final chance a plausible one. Or at the very least create a story that made it make sense.

Therefore you only have one life in this game, but a force field that can be replenished when you destroy enemy ships. Also to add interest, and some scope for tactics, you have to destroy a certain percentage of each wave (i.e. level) in order to succeed. There are no points, but what you do have is an 'Excess Energy' count. This excess energy is built up when you destroy enemy ships, but depleted if your shield and/or hull requires repairs.

The game only has two levels, as it was only meant to demonstrate an idea, but Dark Project 0 was the biggest and most complex game I built. It was also intended to be easily expanded, though I noted scripting rather than the hard coded methods used would need to be implemented before this could be done to any great affect.

## Pre-requisites

- Blitz3D

I was able to get the code up and running again by registering on [Blitzcoder](https://www.blitzcoder.org/) and then [downloading a copy](https://www.blitzcoder.org/forum/downloads.php).

## User Guide

Simply put, at the start of each wave one of the Dark Project 0 team will pop up, and give you a message along with a percentage of how many ships in that wave must be destroyed.

Your HUD will give you all the information you need. The top blue bar is the 'Wave' bar. This will tell you which Wave your facing i.e. your current level, and the small vertical yellow line is your Wave marker. If the red line is over that at the end of the level, you have failed to kill enough Tylan ships.

Below that you have your 'Excess' energy counter. This is used to replenish your shield, and is added to with each enemy you kill. The higher your excess after a successful wave, the better you performed. Next to that is your shield level, which if depleted totally means you will start taking damage to your hull. When your hull strength reaches 0, your dead. Finally you have your current weapon indicator (weapon pick-ups not currently implemented).

To control your ship use the arrow keys, press CTRL to fire and CTRL/SPACE to move through end screens.

## Screenshots

<img src="/screenshots/game1_screen.gif" alt="Screenshot of Dark Project 0 story" />

<img src="/screenshots/game2_screen.gif" alt="Screenshot of Dark Project 0 game play" />

<img src="/screenshots/game3_screen.gif" alt="Screenshot of Dark Project 0 end of level" />

## License

The gem is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).

> If you don't add a license it's neither free or open!