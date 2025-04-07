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

package controllers

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.JsString
import play.api.mvc.Results.Created
import play.api.mvc.{AnyContentAsEmpty, ControllerComponents}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.vatregistrationstub.connectors.VatRegistrationBEConnector
import uk.gov.hmrc.vatregistrationstub.controllers.DataSetupController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataSetupControllerSpec extends AnyFreeSpec with Matchers with GuiceOneAppPerSuite {
  private val mockConnector  = mock[VatRegistrationBEConnector]
  private val mockHttpClient = mock[HttpClient]
  private val cc             = app.injector.instanceOf[ControllerComponents]

  val controller = new DataSetupController(cc, mockHttpClient, mockConnector)

  implicit val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(GET, "")

  "DataSetupController" - {
    "must return Ok status with error when data is missing" in {
      val result = controller.setupMongoData("id", "regId")(fakeRequest)
      status(result) mustBe OK
      contentAsJson(result) mustBe JsString("error")
    }

    "must return Ok for valid data" in {
      when(mockConnector.setupUpscan(any(), any())(any())).thenReturn(Future.successful(Created))
      when(mockConnector.startRegistration(any(), any(), any())(any())).thenReturn(Future.successful(Created))
      val result = controller.setupMongoData("allAttachments", "regId")(fakeRequest)
      status(result) mustBe CREATED
    }
  }
}
