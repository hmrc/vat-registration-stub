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

package uk.gov.hmrc.vatregistrationstub.connectors

import play.api.http.Status._
import play.api.libs.json._
import play.api.mvc.Result
import play.api.mvc.Results._
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}
import uk.gov.hmrc.vatregistrationstub.config.AppConfig

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class VatRegistrationBEConnector @Inject() (appConfig: AppConfig, val httpClient: HttpClient)(
  implicit ec: ExecutionContext
) {

  def setupDataUrl(regId: String)   = s"${appConfig.vatRegUrl}/vatreg/test-only/setup-data/${regId}"
  def setupUpscanUrl(regId: String) = s"${appConfig.vatRegUrl}/vatreg/test-only/setup-upscan/${regId}"

  def startRegistration(data: JsValue, id: String, regId: String)(implicit hc: HeaderCarrier): Future[Result] =
    httpClient.POST[JsValue, HttpResponse](setupDataUrl(regId = regId), data).map { idd =>
      idd.status match {
        case CREATED => Created(Json.toJson(idd.json))
        case _       => InternalServerError(Json.toJson(idd.json))
      }
    }.recover {
      case e: Exception =>
        Ok(Json.toJson(e.getMessage))
    }

  def setupUpscan(data: JsValue, regId: String)(implicit hc: HeaderCarrier): Future[Result] =
    httpClient.POST[JsValue, HttpResponse](setupUpscanUrl(regId = regId), data).map { idd =>
      idd.status match {
        case CREATED => Created(Json.toJson(idd.json))
        case _       => InternalServerError(Json.toJson(idd.json))
      }
    }.recover {
      case e: Exception => Ok(Json.toJson(e.getMessage))
    }

}
