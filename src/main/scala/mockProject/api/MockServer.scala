package mockProject.api


import scala.concurrent.duration._
import scala.language.postfixOps





object Test {

  import mockProject.api.Transition._

  val setApplication = new StartDefined {}

  val x = setApplication whenGetting "Hi" andWhenGetting "Bye" send "Thank you" withDelay (100 millis)
  val y = setApplication whenGetting "Bye" send "Don't go"

}
