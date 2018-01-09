package com.caffeinateddave.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.caffeinateddave.actor.HealthcheckResult
import org.json4s.{Formats, NoTypeHints}
import org.json4s.native.Serialization.write
import org.json4s.native.Serialization

trait HealthcheckRoutes {

  def getHealthcheckStatus(): HealthcheckResult

  implicit val formats: AnyRef with Formats

  val healthcheckRoute: Route = {
    path("healthcheck" / "ping") {
      get {
        complete("pong")
      }
    } ~
    path("healthcheck" / "status") {
      get {
        complete(write(getHealthcheckStatus()))
      }
    }
  }

}
