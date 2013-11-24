MemoryList:
This is a class made for managing, storing and using memory. It extends hand and adds
methods which are specific to memory actions. The memory list basically keeps track of
all of the cards still in play (which is the inverse of all of the cards that have
already been played). Useful methods of the memory list include:
Returning all of the cards of a given suit still in play.
Keeping track of whether other players have gone void in a suit.
Graphic User Interface

CardPanel:
Building on the class made by Professor Robillard, CardPanel class controls
the panel of the cards that the interactive user can see, and the non-visible
AI hands. Based on the input of the height and width of the intended CardPanel
from FHGUI, the class uses a rotated image to show AIs on both sides of the interactive
player. When the user has the chance to interact with the CardPanel in TablePlay, the
number of cards that can be popped up changes based on which mode is selected. For example,
only one card can be selected during bidding and playing, and six cards during exchange.

TableSetup:
This is the first part of UI that the user can see and interact with. The user can set the
player names and player levels. The choices made by the user here interact with GUIListener
which is the global ActionListener for the GUI portion of our implementation. Finally, the
statistics of the game get displayed for the user. A new game can be initiated from this screen.

TablePlay:
This is the main panel that deals with the majority of the game playability that the user
can see. All buttons, inputs, and outputs to the user during the regular gameplay is
initialized and set here. The interactions with the user, however, pass through the
GUIListener where the proper functions occur. The view that the user sees changes
between bidding mode, exchange mode, playing mode and end of the game differs with
the different levels of interactivity associated with each mode. For example,
bidding uses a selection combo-box while playing a card lets it pop up.

StatusBar:
The class is used in FHGUI and the GUIListener to keep the user informed of the current state
of the game, giving updates using a less verbose for of the logger. It mainly keeps track
who the leader, dealer, and contract holder are, as well as the contract.

AdvancedExchangeStrategy:
This exchange strategy took two main factors into consideration
when deciding which 6 cards to discard: first, discard low cards,
second, try to go void in as many suits as possible. Obviously,
trump cards were never discarded as they are almost more valuable
than non-trump cards. When making the decision on which cards
were the least valuable, the cards were scored based on their
rank and how many cards you have in that suit. Each card was
discarded one at a time with card scores being updated after each
discard so as to continue discarding in one suit once you have
started discarding from it.

AdvancedBiddingStrategy:
The big difference between the advanced bidding strategy and the
basic one is that in the advanced strategy, the score of the
game is taken into account and no-trump bids are evaluated
differently than suit bids. If the other team is about to win, 
you are more likely to make a higher bid to prevent them from
winning (as long as this would not put you at risk of losing).
For no-trump bids, the "score" for the bid is more heavily
influenced by aces and less influenced by queens obviously less
influenced by the number of cards in each suit.

Advanced Playing Strategy:
With the addition of a memory list (list of all cards whose
location is unknown to the player) to advanced robots, 
there are two main improvements to the basic playing strategy.
First, the advanced strategy includes a complicated and much
improved leading card choice and second, if your partner is 
winning the trick, a low card is discarded unless there is a high
chance that an opponent can still win the trick. When leading,
the card to play is decided by generally following this priority:
1. Lead with a high non-trump card that you know can't be beat
by another non-trump card and is unlikely to be trumped.
2. Lead with a high trump card that is unlikely to be beaten by
an opponent.
3. Lead with a card that is likely to be in a suit that is 
beneficial to your partner (you know what they bid).
4. Lead with your highest card.

AdvancedRobot:
The only difference between advanced AI robots and basic AI
robots is that the advanced robots contain a memory list which
keeps track of which cards are still available to be played by
another player.

FHGUI:
FHGUI is the main interface of the game. When constructed, the
FHGUI object creates all of the main GUI panels and arranges them
to form a center table surrounded by 4 player hands. It also creates
and adds in the menu bar and status bars. In addition to creating
the main GUI objects, it also acts an an observer to GameEngine
whenever certain events occur in GameEngine, FHGUI gets notified and
performs basic actions such as making buttons visible or clearing the
table of cards.


GUIListener:
This is the event handler for the GUI. It implements ActionListener. Any
buttons or cards that get clicked within the GUI cause the actionPerformed()
method to be called. This method then checks the string representation of
the event and performs the appropriate actions. GUIListener also contains
the GameEngine; this allows it to easily call methods in GameEngine. Because
GUIListener does most of the controlling of GameEngine, it also contains the
Logger and GameStatistics observers for when it adds them to GameEngine after
creating it. 


MenuBar:
This is a simple class, it simply creates a hierarchy of possible buttons to
press; these are options that can be slected at any time, and range in function
from starting a new game, to changing the location of the Log and Status files. 