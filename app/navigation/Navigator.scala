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

import javax.inject.{Inject, Singleton}
import play.api.mvc.Call
import controllers.routes
import pages._
import models._
import queries.AllProperties

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case ApplicantNamePage          => _ => routes.ApplicantNinoController.onPageLoad(NormalMode)
    case ApplicantNinoPage          => _ => routes.ApplicantHasUtrController.onPageLoad(NormalMode)
    case ApplicantHasUtrPage        => applicantHasUtrRoute
    case ApplicantUtrPage           => _ => routes.PartnerNameController.onPageLoad(NormalMode)
    case PartnerNamePage            => _ => routes.PartnerNinoController.onPageLoad(NormalMode)
    case PartnerNinoPage            => _ => routes.PartnerHasUtrController.onPageLoad(NormalMode)
    case PartnerHasUtrPage          => partnerHasUtrRoute
    case PartnerUtrPage             => _ => routes.CurrentAddressUkController.onPageLoad(NormalMode)
    case CurrentAddressUkPage       => _ => routes.PropertyAddressController.onPageLoad(NormalMode, Index(0))
    case PropertyAddressPage(index) => _ => routes.ShareOfPropertyController.onPageLoad(NormalMode, index)
    case ShareOfPropertyPage(index) => _ => routes.CheckPropertyController.onPageLoad(index)
    case CheckPropertyPage(_)       => _ => routes.AddPropertyController.onPageLoad(NormalMode)
    case AddPropertyPage            => addPropertyRoute
    case RemovePropertyPage(_)      => removePropertyRoute
    case _                          => _ => routes.IndexController.onPageLoad
  }

  private def applicantHasUtrRoute(answers: UserAnswers): Call =
    answers.get(ApplicantHasUtrPage).map {
      case true  => routes.ApplicantUtrController.onPageLoad(NormalMode)
      case false => routes.PartnerNameController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def partnerHasUtrRoute(answers: UserAnswers): Call =
    answers.get(PartnerHasUtrPage).map {
      case true  => routes.PartnerUtrController.onPageLoad(NormalMode)
      case false => routes.CurrentAddressUkController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def addPropertyRoute(answers: UserAnswers): Call =
    answers.get(AddPropertyPage).map {
      case true =>
        answers.get(AllProperties)
          .map(properties => routes.PropertyAddressController.onPageLoad(NormalMode, Index(properties.size)))
          .getOrElse(routes.JourneyRecoveryController.onPageLoad())

      case false =>
        routes.CheckYourAnswersController.onPageLoad
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def removePropertyRoute(answers: UserAnswers): Call = {
    val properties = answers.get(AllProperties).getOrElse(Nil)

    if (properties.isEmpty) {
      routes.PropertyAddressController.onPageLoad(NormalMode, Index(0))
    }
    else {
      routes.AddPropertyController.onPageLoad(NormalMode)
    }
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case ApplicantHasUtrPage        => applicantHasUtrCheckRoute
    case PartnerHasUtrPage          => partnerHasUtrCheckRoute
    case PropertyAddressPage(index) => _ => routes.CheckPropertyController.onPageLoad(index)
    case ShareOfPropertyPage(index) => _ => routes.CheckPropertyController.onPageLoad(index)
    case _                          => _ => routes.CheckYourAnswersController.onPageLoad
  }

  private def applicantHasUtrCheckRoute(answers: UserAnswers): Call = {
    answers.get(ApplicantHasUtrPage).map {
      case true =>
        answers.get(ApplicantUtrPage)
          .map(_ => routes.CheckYourAnswersController.onPageLoad)
          .getOrElse(routes.ApplicantUtrController.onPageLoad(CheckMode))

      case false =>
        routes.CheckYourAnswersController.onPageLoad
    }
  }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def partnerHasUtrCheckRoute(answers: UserAnswers): Call = {
    answers.get(PartnerHasUtrPage).map {
      case true =>
        answers.get(PartnerUtrPage)
          .map(_ => routes.CheckYourAnswersController.onPageLoad)
          .getOrElse(routes.PartnerUtrController.onPageLoad(CheckMode))

      case false =>
        routes.CheckYourAnswersController.onPageLoad
    }
  }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
