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

package navigation

import base.SpecBase
import controllers.routes
import pages._
import models._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id")) mustBe routes.IndexController.onPageLoad
      }

      "must go from Applicant Name to Applicant Nino" in {

        navigator.nextPage(ApplicantNamePage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantNinoController.onPageLoad(NormalMode)
      }

      "must go from Applicant Nino to Applicant has UTR" in {

        navigator.nextPage(ApplicantNinoPage, NormalMode, emptyUserAnswers) mustBe routes.ApplicantHasUtrController.onPageLoad(NormalMode)
      }

      "must go from Applicant Has UTR to Applicant UTR when the answer is yes" in {

        val answers = emptyUserAnswers.set(ApplicantHasUtrPage, true).success.value
        navigator.nextPage(ApplicantHasUtrPage, NormalMode, answers) mustBe routes.ApplicantUtrController.onPageLoad(NormalMode)
      }

      "must go from Applicant Has UTR to Partner Name when the answer is no" in {

        val answers = emptyUserAnswers.set(ApplicantHasUtrPage, false).success.value
        navigator.nextPage(ApplicantHasUtrPage, NormalMode, answers) mustBe routes.PartnerNameController.onPageLoad(NormalMode)
      }

      "must go from Applicant UTR to Partner Name" in {

        navigator.nextPage(ApplicantUtrPage, NormalMode, emptyUserAnswers) mustBe routes.PartnerNameController.onPageLoad(NormalMode)
      }

      "must go from Partner Name to Partner Nino" in {

        navigator.nextPage(PartnerNamePage, NormalMode, emptyUserAnswers) mustBe routes.PartnerNinoController.onPageLoad(NormalMode)
      }

      "must go from Partner Nino to Partner has UTR" in {

        navigator.nextPage(PartnerNinoPage, NormalMode, emptyUserAnswers) mustBe routes.PartnerHasUtrController.onPageLoad(NormalMode)
      }

      "must go from Partner Has UTR to Partner UTR when the answer is yes" in {

        val answers = emptyUserAnswers.set(PartnerHasUtrPage, true).success.value
        navigator.nextPage(PartnerHasUtrPage, NormalMode, answers) mustBe routes.PartnerUtrController.onPageLoad(NormalMode)
      }

      "must go from Partner Has UTR to Current Address when the answer is no" in {

        val answers = emptyUserAnswers.set(PartnerHasUtrPage, false).success.value
        navigator.nextPage(PartnerHasUtrPage, NormalMode, answers) mustBe routes.CurrentAddressController.onPageLoad(NormalMode)
      }

      "must go from Partner UTR to Current Address" in {

        navigator.nextPage(PartnerUtrPage, NormalMode, emptyUserAnswers) mustBe routes.CurrentAddressController.onPageLoad(NormalMode)
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }
  }
}
