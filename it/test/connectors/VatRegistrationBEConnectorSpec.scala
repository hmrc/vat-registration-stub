/*
 * Copyright 2024 HM Revenue & Customs
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

package connectors

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import helpers.WireMockServerHandler
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import play.api.Application
import play.api.http.Status.{BAD_REQUEST, CREATED, INTERNAL_SERVER_ERROR, OK}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsString, Json}
import play.api.mvc.Result
import play.api.test.Helpers.{defaultAwaitTimeout, status}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.vatregistrationstub.connectors.VatRegistrationBEConnector

import scala.concurrent.Future

class VatRegistrationBEConnectorSpec extends AnyFreeSpec with WireMockServerHandler with Matchers {

  private implicit val hc: HeaderCarrier = HeaderCarrier()

  lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      conf = "microservice.services.vat-registration.port" -> server.port()
    )
    .build()

  lazy val connector: VatRegistrationBEConnector = app.injector.instanceOf[VatRegistrationBEConnector]

  "VatRegistrationBEConnector" - {

    "startRegistration" - {
      "must return status as CREATED for a valid input request" in {
        stubResponse("/vatreg/test-only/setup-data/Id", CREATED, JsString("Idd").toString())
        val result: Future[Result] = connector.startRegistration(Json.obj(), "id", "Id")
        status(result) mustBe CREATED
      }

      "must return status as BAD_REQUEST for a invalid input request" in {
        stubResponse("/vatreg/test-only/setup-data/Id", BAD_REQUEST, JsString("Idd").toString())
        val result: Future[Result] = connector.startRegistration(Json.obj(), "id", "Id")
        status(result) mustBe INTERNAL_SERVER_ERROR
      }

      "must return status as OK when an exception is thrown" in {
        val result: Future[Result] = connector.startRegistration(Json.obj(), "id", "Id")
        status(result) mustBe OK
      }
    }

    "setupUpscan" - {
      "must return status as CREATED for a valid input request" in {
        stubResponse("/vatreg/test-only/setup-upscan/Id", CREATED, JsString("Idd").toString())
        val result: Future[Result] = connector.setupUpscan(Json.obj(), "Id")
        status(result) mustBe CREATED
      }

      "must return status as BAD_REQUEST for a invalid input request" in {
        stubResponse("/vatreg/test-only/setup-upscan/Id", BAD_REQUEST, JsString("Idd").toString())
        val result: Future[Result] = connector.setupUpscan(Json.obj(), "Id")
        status(result) mustBe INTERNAL_SERVER_ERROR
      }

      "must return status as OK when an exception is thrown" in {
        val result: Future[Result] = connector.setupUpscan(Json.obj(), "Id")
        status(result) mustBe OK
      }
    }
  }

  private def stubResponse(expectedUrl: String,
                           expectedStatus: Int,
                           response: String
                          ): StubMapping =
    server.stubFor(
      post(urlEqualTo(expectedUrl))
        .willReturn(
          aResponse()
            .withStatus(expectedStatus)
            .withBody(response)
        )
    )
}