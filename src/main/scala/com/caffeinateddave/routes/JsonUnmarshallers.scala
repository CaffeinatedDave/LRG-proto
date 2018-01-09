package com.caffeinateddave.routes

import com.caffeinateddave.actor.LoginAttempt
import spray.json.DefaultJsonProtocol

trait JsonUnmarshallers extends DefaultJsonProtocol {
  implicit val loginAttemptFormat = jsonFormat2(LoginAttempt)
}
