package com.caffeinateddave.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration._
import scala.util.Random

case class LoginAttempt(username: String, password: String)
case class LoginDelay(actorRef: ActorRef)

sealed trait LoginResult

case class LoginSuccess(token: String) extends LoginResult
case object LoginFailure extends LoginResult

class AccountActor extends Actor with ActorLogging{

  implicit val executionContext = context.system.dispatcher

  override def receive: Receive = {
    case _: LoginAttempt => {
      val delay = Random.nextInt(3000) + 1000
      context.system.scheduler.scheduleOnce(delay milliseconds, self, LoginDelay(sender()))
    }
    case delay: LoginDelay => {
      val response = if (Random.nextBoolean()) {
        LoginSuccess("token")
      } else {
        LoginFailure
      }

      delay.actorRef ! response
    }
  }
}

object AccountActor {
  def instance = {
    Props(classOf[AccountActor])
  }
}
