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

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.JsonSchemaFactory
import play.api.Environment
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
class ValidationController @Inject()(cc: ControllerComponents,
                                     val authConnector: AuthConnector,
                                     val environment: Environment,
                                     override implicit val executionContext: ExecutionContext)
    extends BackendController(cc) with StubResource with AuthorisedFunctions {

  val basePath = "/conf/resources.data/apiSchemas"


  def validateJsonToJsonSchema(id: String): Action[AnyContent] = Action.async { implicit request =>

    def validate(jsonAsString: String, schemaPath: String): Boolean = {
      val jsonSchemaFile = environment.getExistingFile(schemaPath)
      val uri = jsonSchemaFile.get.toURI
      val factory = JsonSchemaFactory.byDefault.getJsonSchema(uri.toString)
      val json: JsonNode = JsonLoader.fromString(jsonAsString)

      val res = factory.validate(json)
      logger.info("JSON REUSLTS " + res.toString)
      factory.validate(json).isSuccess
    }

    val jsonAsString = request.body.asJson.get.toString()
    val schema = id match {
      case "1365" => s"$basePath/api1365Schema.json"
      case _ => s"$basePath/api1365Schema.json"
    }

    validate(jsonAsString, schema) match {
      case true =>
        logger.info(s"********************************************* ${id} Json received in Stub" + jsonAsString)
        Future.successful(Ok("Success"))
      case false =>
        logger.info(s"********************************************* ${id} Invalid Json received in Stub" + jsonAsString )
        Future.successful(BadRequest("check logs for reason in dugger"))
    }
  }

}
