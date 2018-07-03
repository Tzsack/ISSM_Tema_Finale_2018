/*
===============================================================
resourceModel.pl
===============================================================
*/

/*
 * Resources are modelled like:
 * resource(name(NAME), STATE)
 */

resource(name(robot), state(movement(stopped), obstacleDetected(false))).
/* 
 * for the robot movement can be:
 * - stopped
 * - movingForward
 * - movingBackward
 * - turningLeft
 * - turningRight
 */
 
resource(name(temp), state(temperature(0))). 
resource(name(timer), state(currentTime(hours(0), minutes(0), seconds(0)))).
 
resource(name(sonar1), state(somethingDetected(false), distance(0))).
resource(name(sonar2), state(somethingDetected(false), distance(0))).

getResource(NAME, STATE) :-
		resource(name(NAME), STATE).


changeModelItem(robot, movement(VALUE)) :-
		resource(name(robot), state(movement(_), obstacleDetected(X))),
 		(
			retract(resource(name(robot), state(movement(_), obstacleDetected(X))));
			true
		),
 		assert(resource(name(robot), state(movement(VALUE), obstacleDetected(X)))),
		!,
		emitevent(modelChanged, modelChanged(resource(name(robot), state(movement(VALUE), obstacleDetected(X))))),
		(
			changedModelAction(resource(name(robot), state(movement(VALUE), obstacleDetected(X)))) 	
		 	; true								
		).		


changeModelItem(robot, obstacleDetected(VALUE)) :-
		resource(name(robot), state(movement(X), obstacleDetected(_))),
		(
			retract(resource(name(robot), state(movement(X), obstacleDetected(_))));
			true
		),
 		assert(resource(name(robot), state(movement(X), obstacleDetected(VALUE)))),
		!,
		emitevent(modelChanged, modelChanged(resource(name(robot), state(movement(X), obstacleDetected(VALUE))))),
		(
			changedModelAction(resource(name(robot), state(movement(X), obstacleDetected(VALUE)))) 	
		 	; true								
		).


changeModelItem(NAME, turnLed(VALUE)) :-
		resource(name(NAME), state(OLD_STATE)),
 		(
			retract(resource(name(NAME), state(OLD_STATE)));
			true
		),
 		assert(resource(name(NAME), state(VALUE))),
		!,
		emitevent(modelChanged, modelChanged(resource(name(NAME), state(VALUE)))),
		(
			changedModelAction(resource(name(NAME), state(VALUE))) 	
		 	; true								
		).
		
changeModelItem(temp, updateTemperature(VALUE)) :-
		resource(name(temp), state(OLD_STATE)),
 		(
			retract(resource(name(temp), state(OLD_STATE)));
			true
		),
 		assert(resource(name(temp), state(temperature(VALUE)))),
		!,
		emitevent(modelChanged, modelChanged(resource(name(temp), state(temperature(VALUE))))),
		(
			changedModelAction(resource(name(temp), state(temperature(VALUE)))) 	
		 	; true								
		).
		
changeModelItem(timer, updateTime(currentTime(hours(HOURS), minutes(MINUTES), seconds(SECONDS)))) :-
		resource(name(timer), state(OLD_STATE)),
 		(
			retract(resource(name(timer), state(OLD_STATE)));
			true
		),
 		assert(resource(name(timer), state(currentTime(hours(HOURS), minutes(MINUTES), seconds(SECONDS))))),
		!,
		emitevent(modelChanged, modelChanged(resource(name(timer), state(currentTime(hours(HOURS), minutes(MINUTES), seconds(SECONDS)))))),
		(
			changedModelAction(resource(name(timer), state(currentTime(hours(HOURS), minutes(MINUTES), seconds(SECONDS))))) 	
		 	; true								
		).

eval( ge, X, X ) :- !. 
eval( ge, X, V ) :- eval( gt, X , V ) .


emitevent( EVID, EVCONTENT ) :- 
	actorobj( Actor ), 
	Actor <- emit( EVID, EVCONTENT ).


%%%  initialize
initResourceTheory :- output("initializing the initResourceTheory ...").
:- initialization(initResourceTheory).

