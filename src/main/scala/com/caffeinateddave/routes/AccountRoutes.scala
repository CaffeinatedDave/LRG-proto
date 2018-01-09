package com.caffeinateddave.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import com.caffeinateddave.actor.{LoginAttempt, LoginFailure, LoginResult, LoginSuccess}
import org.json4s.{DateFormat, Formats, NoTypeHints, TypeHints}
import org.json4s.native.Serialization
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsObject}

trait AccountRoutes extends Directives with SprayJsonSupport {
  import DefaultJsonProtocol._

  def doLogin(username: String, password: String): LoginResult

  implicit val materializer: ActorMaterializer

  implicit val formats: AnyRef with Formats

  implicit val loginAttemptFormat = jsonFormat2(LoginAttempt)

  val accountRoute: Route = {
    path("account" / "login") {
      post {
        entity(as[LoginAttempt]) {
          login => {
            doLogin(login.username, login.password) match {
              case x: LoginSuccess => complete("success")
              case LoginFailure => complete(HttpResponse(status = StatusCodes.Forbidden))
            }
          }
        }
      }
    }
  }

}
