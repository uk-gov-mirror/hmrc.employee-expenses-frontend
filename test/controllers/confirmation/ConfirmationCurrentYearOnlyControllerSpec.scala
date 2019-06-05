/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers.confirmation

import base.SpecBase
import connectors.TaiConnector
import controllers.actions.Authed
import controllers.confirmation.routes._
import controllers.routes._
import models.TaxCodeStatus.Live
import models._
import org.mockito.Matchers._
import org.mockito.Mockito.when
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import pages._
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import service.ClaimAmountService
import views.html.confirmation._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ConfirmationCurrentYearOnlyControllerSpec extends SpecBase with MockitoSugar with ScalaFutures with IntegrationPatience {

  val mockTaiConnector: TaiConnector = mock[TaiConnector]
  val mockClaimAmountService: ClaimAmountService = mock[ClaimAmountService]
  val claimAmountService = new ClaimAmountService(frontendAppConfig)
  val claimAmount: Int = fullUserAnswers.get(ClaimAmountAndAnyDeductions).get
  val claimAmountsAndRates = StandardRate(
    frontendAppConfig.taxPercentageBasicRate,
    frontendAppConfig.taxPercentageHigherRate,
    claimAmountService.calculateTax(frontendAppConfig.taxPercentageBasicRate, claimAmount),
    claimAmountService.calculateTax(frontendAppConfig.taxPercentageHigherRate, claimAmount)
  )

  "ConfirmationCurrentYearOnlyController" must {

    "return OK and the correct ConfirmationCurrentYearOnlyView for a GET with specific answers" in {

      val application = applicationBuilder(userAnswers = Some(fullUserAnswers))
        .overrides(bind[TaiConnector].toInstance(mockTaiConnector))
        .overrides(bind[ClaimAmountService].toInstance(mockClaimAmountService))
        .build()

      when(mockTaiConnector.taiTaxCodeRecords(any(), any())(any(), any())).thenReturn(Future.successful(Seq(TaxCodeRecord("850L", Live))))
      when(mockClaimAmountService.getRates(any(),any())).thenReturn(Seq(claimAmountsAndRates))

      val request = FakeRequest(GET, ConfirmationCurrentYearOnlyController.onPageLoad().url)

      val result = route(application, request).value

      val view = application.injector.instanceOf[ConfirmationCurrentYearOnlyView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(
          claimAmountsAndRates = Seq(claimAmountsAndRates),
          claimAmount = claimAmount,
          employerInfoCorrect = Some(true),
          addressInfoCorrect = Some(true),
          freResponse = FlatRateExpenseOptions.FRENoYears
        )(request, messages, frontendAppConfig).toString

      application.stop()
    }

    "Redirect to TechnicalDifficulties when call to Tai fails" in {

      val application = applicationBuilder(userAnswers = Some(fullUserAnswers))
        .overrides(bind[TaiConnector].toInstance(mockTaiConnector))
        .overrides(bind[ClaimAmountService].toInstance(mockClaimAmountService))
        .build()

      when(mockTaiConnector.taiTaxCodeRecords(any(), any())(any(), any())).thenReturn(Future.failed(new Exception))

      val request = FakeRequest(GET, ConfirmationCurrentYearOnlyController.onPageLoad().url)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustBe TechnicalDifficultiesController.onPageLoad().url

      application.stop()
    }

    "Redirect to SessionExpired when missing userAnswers" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request = FakeRequest(GET, ConfirmationCurrentYearOnlyController.onPageLoad().url)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustBe SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "Remove session on page load" in {

      val application = applicationBuilder(userAnswers = Some(fullUserAnswers))
        .overrides(bind[TaiConnector].toInstance(mockTaiConnector))
        .overrides(bind[ClaimAmountService].toInstance(mockClaimAmountService))
        .build()

      when(mockTaiConnector.taiTaxCodeRecords(any(), any())(any(), any())).thenReturn(Future.successful(Seq(TaxCodeRecord("850L", Live))))

      val request = FakeRequest(GET, ConfirmationCurrentYearOnlyController.onPageLoad().url)

      val result = route(application, request).value

      whenReady(result) {
        _ =>
          val sessionRepository = application.injector.instanceOf[SessionRepository]
          sessionRepository.get(Authed(userAnswersId)).map(_ mustBe None)
      }

      application.stop()
    }
  }
}
