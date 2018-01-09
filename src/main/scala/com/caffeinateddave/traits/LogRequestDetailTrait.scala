package com.caffeinateddave.traits

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LoggingMagnet}
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.StreamConverters

import scala.concurrent.duration.FiniteDuration

trait LogRequestDetailTrait {
  implicit val materializer: ActorMaterializer

  val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private def timestampNow: String = df.format(System.currentTimeMillis())

  private def printRequestMethod(req: HttpRequest): Unit = {
    val requestContent = try {
      scala.io.Source.fromInputStream(req.entity.dataBytes.runWith(
        StreamConverters.asInputStream(FiniteDuration(3, TimeUnit.SECONDS))
      )).mkString
    } catch {
      case _ => "null"
    }

    println(s"LRG: $timestampNow ${req.uri} ${req.method.name} $requestContent")
  }

  val logRequestPrintln = DebuggingDirectives.logRequest(LoggingMagnet(_ => printRequestMethod))
}
