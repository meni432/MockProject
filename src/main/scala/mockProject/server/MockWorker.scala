package mockProject.server

import java.net.InetSocketAddress

import mockProject.actor.Actor
import akka.actor.Props
import akka.actor.Status.{Failure, Success}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import mockProject.api.MockProps
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.pattern.pipe

object MockWorker {
  def props(mockProps: MockProps) = Props(new MockWorker(mockProps))
}

class MockWorker(m_mockProps: MockProps) extends Actor {
  implicit val m_system = context.system
  implicit val actorMaterializer = ActorMaterializer()
  implicit val executionContext = m_system.dispatcher

  override def preStart(): Unit = {
    Http().bindAndHandle(route, "0.0.0.0", 0).pipeTo(self)
  }

  override def receive: Receive = {
    case ServerBinding(localAddress) => onServerBinding(localAddress)
    case Failure(cause)              => onServerBindingFailure(cause)
  }

  private val route = {
    val routeSeq = mockPropsToRoute(m_mockProps)
    DebuggingDirectives.logRequest(("get-request", Logging.InfoLevel)) {
      routeSeq.foldLeft[Route](reject)(_ ~ _)
    }
  }

  private def mockPropsToRoute(mockProps: MockProps): Seq[Route] = {
    mockProps.when.map { whenPath =>
      {
        path(whenPath) {
          complete(mockProps.what)
        }
      }
    }
  }

  private def onServerBinding(localAddress: InetSocketAddress) {
    log.info(s"start listening on $localAddress")
  }

  private def onServerBindingFailure(cause: Throwable) {
    log.error(cause, "binding failure")
  }
}
