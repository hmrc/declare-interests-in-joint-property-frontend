/*
 * Copyright 2022 HM Revenue & Customs
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

import base.SpecBase
import models.{Address, Index, UkAddress}
import pages.{PropertyAddressPage, ShareOfPropertyPage}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.checkAnswers.{PropertyAddressSummary, ShareOfPropertySummary}
import viewmodels.govuk.SummaryListFluency
import views.html.CheckPropertyView

class CheckPropertyControllerSpec extends SpecBase with SummaryListFluency {

  private val index = Index(0)

  "Check Property Controller" - {

    "must return OK and the correct view for a GET" in {

      val address = UkAddress("line 1", None, "town", None, "postcode")
      val share = 1

      val answers =
        emptyUserAnswers
          .set(PropertyAddressPage(index), address).success.value
          .set(ShareOfPropertyPage(index), share).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckPropertyController.onPageLoad(index).url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CheckPropertyView]
        implicit val msgs = messages(application)

        val list =
          SummaryListViewModel(Seq(
            PropertyAddressSummary.row(answers, index),
            ShareOfPropertySummary.row(answers, index),
            ShareOfPropertySummary.partnerRow(answers, index)
          ).flatten)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(list, index)(request, implicitly).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
