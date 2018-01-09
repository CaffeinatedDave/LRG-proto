package com.caffeinateddave.actor

import akka.actor.{Actor, ActorLogging, Props}

import scala.concurrent.duration._

case object GetHealthcheck

case object ScheduleCheck

case class HealthcheckComponent(name: String, status: Boolean, description: String)
case class HealthcheckResult(healthy: Boolean, components: List[HealthcheckComponent])

class HealthcheckActor extends Actor with ActorLogging {

  implicit val executionContext = context.system.dispatcher

  var schedulerStatus = HealthcheckComponent("scheduler", status = false, "description")

  private def checkStatus(): Unit = {
    val time = System.currentTimeMillis()

    schedulerStatus = schedulerStatus.copy(status = true, description = s"last checked at $time")
  }

  context.system.scheduler.schedule(3 seconds, 10 seconds, self, ScheduleCheck)

  override def receive: Receive = {
    case ScheduleCheck => {
      checkStatus()
    }
    case GetHealthcheck => {
      val response = HealthcheckResult(healthy = true, List(schedulerStatus))
      sender ! response
    }
  }
}

object HealthcheckActor {
  def instance = {
    Props(classOf[HealthcheckActor])
  }
}
