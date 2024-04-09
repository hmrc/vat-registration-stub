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

package uk.gov.hmrc.vatregistrationstub.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.vatregistrationstub.connectors.VatRegistrationBEConnector
import uk.gov.hmrc.vatregistrationstub.utils.StubResource

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class DataSetupController @Inject() (
    cc: ControllerComponents,
    val httpClient: HttpClient,
    vatRegistrationBEConnector: VatRegistrationBEConnector,
    override implicit val executionContext: ExecutionContext
) extends BackendController(cc) with StubResource {

  val basePath = "/resources.data"

  def setupMongoData(id: String, regId: String): Action[AnyContent] = Action.async { implicit request =>
    (findResource(s"$basePath/$id/VatRegBEData.json"), findResource(s"$basePath/$id/UpscanBEData.json")) match {
      case (Some(content), maybeUpscan) =>
        maybeUpscan.map(upscanDetails => vatRegistrationBEConnector.setupUpscan(Json.parse(upscanDetails), regId))
        vatRegistrationBEConnector.startRegistration(Json.parse(content), id, regId)
      case _ => Future.successful(Ok(Json.toJson("error")))
    }
  }

}
