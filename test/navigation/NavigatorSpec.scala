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
        navigator.nextPage(PartnerHasUtrPage, NormalMode, answers) mustBe routes.CurrentAddressUkController.onPageLoad(NormalMode)
      }

      "must go from Partner UTR to Current Address" in {

        navigator.nextPage(PartnerUtrPage, NormalMode, emptyUserAnswers) mustBe routes.CurrentAddressUkController.onPageLoad(NormalMode)
      }

      "must go from Current Address to Property Address for index 0" in {

        navigator.nextPage(CurrentAddressUkPage, NormalMode, emptyUserAnswers) mustBe routes.PropertyAddressController.onPageLoad(NormalMode, Index(0))
      }

      "must go from Property Address to Share of Property for the same index" in {

        navigator.nextPage(PropertyAddressPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.ShareOfPropertyController.onPageLoad(NormalMode, Index(0))
        navigator.nextPage(PropertyAddressPage(Index(1)), NormalMode, emptyUserAnswers) mustBe routes.ShareOfPropertyController.onPageLoad(NormalMode, Index(1))
      }

      "must go from Share of Property to Check Property for the same index" in {

        navigator.nextPage(ShareOfPropertyPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.CheckPropertyController.onPageLoad(Index(0))
        navigator.nextPage(ShareOfPropertyPage(Index(1)), NormalMode, emptyUserAnswers) mustBe routes.CheckPropertyController.onPageLoad(Index(1))
      }

      "must go from Check Property to Add Property" in {

        navigator.nextPage(CheckPropertyPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.AddPropertyController.onPageLoad(NormalMode)
        navigator.nextPage(CheckPropertyPage(Index(1)), NormalMode, emptyUserAnswers) mustBe routes.AddPropertyController.onPageLoad(NormalMode)
      }

      "must go from Add Property to Property Address for the next index when the answer is yes" in {

        val address = UkAddress("line 1", None, "town", None, "postcode")
        val answers =
          emptyUserAnswers
            .set(PropertyAddressPage(Index(0)), address).success.value
            .set(ShareOfPropertyPage(Index(0)), 1).success.value
            .set(AddPropertyPage, true).success.value

        navigator.nextPage(AddPropertyPage, NormalMode, answers) mustBe routes.PropertyAddressController.onPageLoad(NormalMode, Index(1))
      }

      "must go from Add Property to Check Answers when the answer is no" in {

        val answers = emptyUserAnswers.set(AddPropertyPage, false).success.value
        navigator.nextPage(AddPropertyPage, NormalMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Remove Property to Add Property when there is at least one property left" in {

        val address = UkAddress("line 1", None, "town", None, "postcode")
        val answers =
          emptyUserAnswers
            .set(PropertyAddressPage(Index(0)), address).success.value
            .set(ShareOfPropertyPage(Index(0)), 1).success.value

        navigator.nextPage(RemovePropertyPage(Index(0)), NormalMode, answers) mustBe routes.AddPropertyController.onPageLoad(NormalMode)
      }

      "must go from Remove Property to Property Address when index 0 when there are no properties left" in {

        navigator.nextPage(RemovePropertyPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.PropertyAddressController.onPageLoad(NormalMode, Index(0))
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Applicant Name to Check Answers" in {

        navigator.nextPage(ApplicantNamePage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Applicant Nino to Check Answers" in {

        navigator.nextPage(ApplicantNinoPage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Applicant Has UTR to Check Answers when the answer is yes and Applicant UTR is already answered" in {

        val answers =
          emptyUserAnswers
            .set(ApplicantUtrPage, Utr.fromString("1234567890").value).success.value
            .set(ApplicantHasUtrPage, true).success.value

        navigator.nextPage(ApplicantHasUtrPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Applicant Has UTR to Applicant UTR when the answer is yes and Applicant UTR is not already answered" in {

        val answers = emptyUserAnswers.set(ApplicantHasUtrPage, true).success.value
        navigator.nextPage(ApplicantHasUtrPage, CheckMode, answers) mustBe routes.ApplicantUtrController.onPageLoad(CheckMode)
      }

      "must go from Applicant Has UTR to Check Answers when the answer is no" in {

        val answers = emptyUserAnswers.set(ApplicantHasUtrPage, false).success.value
        navigator.nextPage(ApplicantHasUtrPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Applicant UTR to Check Answers" in {

        navigator.nextPage(ApplicantUtrPage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }
      
      "must go from Partner Name to Check Answers" in {

        navigator.nextPage(PartnerNamePage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Partner Nino to Check Answers" in {

        navigator.nextPage(PartnerNinoPage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Partner Has UTR to Check Answers when the answer is yes and Partner UTR is already answered" in {

        val answers =
          emptyUserAnswers
            .set(PartnerUtrPage, Utr.fromString("1234567890").value).success.value
            .set(PartnerHasUtrPage, true).success.value

        navigator.nextPage(PartnerHasUtrPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Partner Has UTR to Partner UTR when the answer is yes and Partner UTR is not already answered" in {

        val answers = emptyUserAnswers.set(PartnerHasUtrPage, true).success.value
        navigator.nextPage(PartnerHasUtrPage, CheckMode, answers) mustBe routes.PartnerUtrController.onPageLoad(CheckMode)
      }

      "must go from Partner Has UTR to Check Answers when the answer is no" in {

        val answers = emptyUserAnswers.set(PartnerHasUtrPage, false).success.value
        navigator.nextPage(PartnerHasUtrPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Partner UTR to Check Answers" in {

        navigator.nextPage(PartnerUtrPage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Current Address to Check Answers" in {

        navigator.nextPage(CurrentAddressUkPage, CheckMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Property Address to Check Property" in {

        navigator.nextPage(PropertyAddressPage(Index(0)), CheckMode, emptyUserAnswers) mustBe routes.CheckPropertyController.onPageLoad(Index(0))
        navigator.nextPage(PropertyAddressPage(Index(1)), CheckMode, emptyUserAnswers) mustBe routes.CheckPropertyController.onPageLoad(Index(1))
      }

      "must go from Share of Property to Check Property" in {

        navigator.nextPage(ShareOfPropertyPage(Index(0)), CheckMode, emptyUserAnswers) mustBe routes.CheckPropertyController.onPageLoad(Index(0))
        navigator.nextPage(ShareOfPropertyPage(Index(1)), CheckMode, emptyUserAnswers) mustBe routes.CheckPropertyController.onPageLoad(Index(1))
      }
    }
  }
}
