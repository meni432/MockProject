# MockProject

[![Build Status](https://travis-ci.com/meni432/MockProject.svg?branch=master)](https://travis-ci.com/meni432/MockProject)

This is simple Mock project writing with Scala.
currently example of use:

Simple usage example
```scala
  import api.Transition._

  val setApplication = new StartDefined {}

  val first = setApplication whenGetting "Hi" andWhenGetting "Bye" send "Thank you" withDelay (100 millis)
  val seconds = setApplication whenGetting "Bye" andWhenGetting "Hi" send "Don't go"

  val actorSystem = ActorSystem("http-actor-system")
  actorSystem.actorOf(MockWorker.props(first))
  actorSystem.actorOf(MockWorker.props(seconds))
```

This is a first example, but we want to move really fast! Take a look on Development branch to see the process.
