package com.caffeinateddave

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.caffeinateddave.actor._
import com.caffeinateddave.routes.{AccountRoutes, HealthcheckRoutes}
import com.caffeinateddave.traits.LogRequestDetailTrait
import org.json4s.NoTypeHints
import org.json4s.native.Serialization

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

class HttpServer(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer)
  extends HealthcheckRoutes
    with AccountRoutes
    with LogRequestDetailTrait {

  implicit val timeout: Timeout = 30 seconds
  implicit val formats = Serialization.formats(NoTypeHints)
  val allRoutes = logRequestPrintln(healthcheckRoute ~ accountRoute)

  override def getHealthcheckStatus() = {
    lazy val hcActor = system.actorOf(HealthcheckActor.instance)
    Await.result(hcActor ? GetHealthcheck, Duration.Inf).asInstanceOf[HealthcheckResult]
  }

  override def doLogin(username: String, password: String) = {
    lazy val accountActor = system.actorOf(AccountActor.instance)

    Await.result(accountActor ? LoginAttempt(username, password), Duration.Inf).asInstanceOf[LoginResult]
  }

  def go(host: String, port: Int) = {
    println(s"Server online at http://$host:$port/")
    Http().bindAndHandle(RouteResult.route2HandlerFlow(allRoutes), host, port)
  }
}
