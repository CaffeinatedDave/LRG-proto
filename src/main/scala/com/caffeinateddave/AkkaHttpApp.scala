package com.caffeinateddave

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise}

object AkkaHttpApp
  extends App
    with Configuration {

  implicit val system = ActorSystem("application")

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val server = new HttpServer()

  val f = for {
    bindingFuture <- server.go(host, port)
    waitOnFuture  <- Promise[Done].future
  } yield waitOnFuture

  sys.addShutdownHook {
    // cleanup logic?
  }

  Await.ready(f, Duration.Inf)

}
