/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.vatregistrationstub.utils

/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

import play.api.Logging
import play.api.http.{ContentTypes, Status}
import play.api.mvc._

import java.io.InputStream
import scala.concurrent.ExecutionContext
import scala.io.Source

trait StubResource extends Results with ContentTypes with Status with Logging {

  implicit def executionContext: ExecutionContext

  def findResource(path: String): Option[String] = {
    val resource = getClass.getResourceAsStream(path)
    if (resource == null) {
      logger.warn(s"Could not find resource '$path'")
      None
    } else {
      Some(readStreamToString(resource))
    }
  }

  private def readStreamToString(is: InputStream): String =
    try Source.fromInputStream(is).mkString
    finally is.close()

}
