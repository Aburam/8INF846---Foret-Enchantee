/*Define square states*/
:- dynamic(light/2).
:- dynamic(poop/2).
:- dynamic(walkable/2).
:- dynamic(wind/2).
:- dynamic(visited/2).
:- dynamic(shooted/2).
:- dynamic(hole/2).

/*Basic operations on squares*/
setWalkable(X,Y) :- assert(walkable(X,Y)).
setVisited(X,Y) :- assert(visited(X,Y)).
setPoop(X,Y) :- assert(poop(X,Y)).
setWind(X,Y) :- assert(wind(X,Y)).
setShooted(X,Y) :- assert(shooted(X,Y)).
setLight(X,Y) :- assert(light(X,Y)).
setHole(X,Y) :- assert(hole(X,Y)).
removeLight(X,Y) :- retractall(light(X,Y)).
removePoop(X,Y) :- retractall(poop(X,Y)).
removeWalkable(X,Y) :- retractall(walkable(X,Y)).
removeWind(X,Y) :- retractall(wind(X,Y)).
removeVisited(X,Y) :- retractall(visited(X,Y)).
removeShooted(X,Y) :- retractall(shooted(X,Y)).
removeHole(X,Y) :- retractall(hole(X,Y)).

/*Check if square is safe to move in*/
safe(X,Y) :- walkable(X,Y), visited(X,Y), not(poop(X,Y)), not(wind(X,Y)).

/*The Agent wants to get out of there ASAP*/
goNorth(X,Y) :- N is Y-1, not(visited(X,N)).
goSouth(X,Y) :- S is Y+1, not(visited(X,S)).
goEast(X,Y) :- E is X+1, not(visited(E,Y)).
goWest(X,Y) :- W is X-1, not(visited(W,Y)).

/*Should the agent shoot a giant rock*/
shoot(X,Y) :- N is Y-1, S is Y+1, W is X-1, E is X+1,
NN is Y-2, SS is Y+2, WW is X-2, EE is X+2,
poop(X,Y), 
((visited(X,N);not(walkable(X,N)),visited(W,N);not(walkable(W,N)),visited(E,N);not(walkable(E,N)),visited(X,NN);not(walkable(X,NN)));
(visited(X,S);not(walkable(X,S)),visited(W,S);not(walkable(W,S)),visited(E,S);not(walkable(E,S)),visited(X,SS);not(walkable(X,SS)));
(visited(E,Y);not(walkable(E,Y)),visited(E,N);not(walkable(E,N)),visited(E,S);not(walkable(E,S)),visited(EE,Y);not(walkable(EE,Y)));
(visited(W,Y);not(walkable(W,Y)),visited(W,N);not(walkable(W,N)),visited(W,S);not(walkable(W,S)),visited(WW,Y);not(walkable(WW,Y)))).

/*Should the agent run away*/
fleeNorth(X,Y) :- N is Y-1, safe(X,N).
fleeSouth(X,Y) :- S is Y+1, safe(X,S).
fleeEast(X,Y) :- E is X+1, safe(E,Y).
fleeWest(X,Y) :- W is X-1, safe(W,Y).

/*Has Agent Succeded*/
portalFound(X,Y) :- light(X,Y).

/*Should Agent try to go for another case*/
cross(X,Y) :- N is Y-1, S is Y+1, W is X-1, E is X+1,
NN is Y-2, SS is Y+2, WW is X-2, EE is X+2,
wind(X,Y), 
((visited(X,N);not(walkable(X,N)),visited(W,N);not(walkable(W,N)),visited(E,N);not(walkable(E,N)),visited(X,NN);not(walkable(X,NN)));
(visited(X,S);not(walkable(X,S)),visited(W,S);not(walkable(W,S)),visited(E,S);not(walkable(E,S)),visited(X,SS);not(walkable(X,SS)));
(visited(E,Y);not(walkable(E,Y)),visited(E,N);not(walkable(E,N)),visited(E,S);not(walkable(E,S)),visited(EE,Y);not(walkable(EE,Y)));
(visited(W,Y);not(walkable(W,Y)),visited(W,N);not(walkable(W,N)),visited(W,S);not(walkable(W,S)),visited(WW,Y);not(walkable(WW,Y)))).