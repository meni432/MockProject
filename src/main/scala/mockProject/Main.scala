package mockProject

import akka.actor.ActorSystem
import mockProject.api.StartDefined
import mockProject.server.MockWorker
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App{
    import api.Transition._

    val setApplication = new StartDefined {}

    val x = setApplication whenGetting "Hi" andWhenGetting "Bye" send "Thank you" withDelay (100 millis)
    val y = setApplication whenGetting "Bye" andWhenGetting "Hi" send "Don't go"

    val actorSystem = ActorSystem("http-actor-system")
    actorSystem.actorOf(MockWorker.props(y))
}
