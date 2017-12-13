# Baboon

[![](doc/img/javadoc_button.svg)](https://juanjoarce7456.github.io/baboon)
[![](https://jitpack.io/v/juanjoarce7456/baboon.svg)](https://jitpack.io/#juanjoarce7456/baboon)

Baboon is a framework written in Java 8, it manages the user's code actions by using a Petri Net as the system's logic.
Baboon uses [JPCM](https://github.com/airabinovich/java_petri_concurrency_monitor) as the Petri Net core executor

## Main Concepts Needed to Use Baboon
* Petri Net: FSM generalization. [See in Wikipedia](https://en.wikipedia.org/wiki/Petri_net)
* Process Oriented Petri Nets: Transitions model actions, Places model resources and processes execution
* PNML file: PetriNet Markup Language. [Standard](http://www.pnml.org/), [TINA](http://projects.laas.fr/tina/) dialect used in JPCM
* Events: Represent an interaction with the outer environment: They can be of one of two types:
  * Task: It's an outgoing event. It starts within the system and towards the environment.
  * Happening: It's an ingoing event. It comes into the system from the environment.
* Action: A method to execute. It's execution is related to an event. This can be:
  * Happening Controller: it's executed when a Happening event occurs. It must be called by the user's software explicitly
  * Task Controller: it's executed every time the Petri Net allows it. It triggers a Task event.
  * Complex Secuential Task Controller: It's a chain of Task Controllers. It allows thread amount optimization. It's designed to serialize processes that are naturally sequential
* Topics: Translation layer between external events and Petri Net's transitions. A topic is conformed by:
  * Name: identification
  * Permission: Transitions array to get permission from before executing an action. If the permission doesn't allow, the executing thread is blocked and the action doesn't execute.
  * Guard Callback: Array of guards sets to set after executing an action. When executing a Complex Secuential Task Controller, each guards set is set after each Task.
  * Fire Callback: Transitions array to signal after executing an action. These firings are non-blocking
* Transitions Policy: Policy to solve firing indecisions. See [JPCM readme](https://github.com/airabinovich/java_petri_concurrency_monitor)
* Transition Event Observer: An observer suscribed to a transition firing. See [JPCM readme](https://github.com/airabinovich/java_petri_concurrency_monitor)


## Main Classes and Interfaces to Use Baboon

* BaboonFramework: Use this class to setup the system. Initialize the petri net core, specify the topics file, suscribe actions to topics, subscribe obsrvers to firing events, etc.
* BaboonApplication: Implement this interface and Baboon will call your class. It must implement two methods:
  * declare(): Create the objects your system needs
  * subscribe(): Subscribe your objects to your topis
* TaskController: Annotation to suscribe a Task Controller method
* HappeningController: Annotation to suscribe a Happening Controller method
* GuardProvider: Annotation to suscribe a Guard Provider method. It must take no arguments and return a boolean value

## Topics Format and Behaviour

The topics must be specified in a topcis file in JSON format. A topics file is a list of topic objects. A single topics object is like:
```json
{
  "name" : "topic_name",
  "permission": ["t01"],
  "fireCallback": ["t03", "t05", "t09"],
  "setGuardCallback" : [["guard1","guard2","guard3"]]
}
```
Notice:
* `name`, `permission` and `fireCallback` are mandatory and must not be empty
* More that one permission transition is used for Complex Secuential Task Controllers
* `setGuardCallback` has at most as many items (list of guard names) as transitions are in `permission`
* `fireCallback` transitions are fired in order and non-blocking
* `fireCallback` transitions are fired at the end of a Complex Secuential Task Controller and not at the end of each Task

## Usage Examples (in spanish)
https://github.com/juanjoarce7456/baboon_examples
