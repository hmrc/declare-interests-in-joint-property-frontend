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

package models

import cats.data.{Ior, IorNec, NonEmptyChain, NonEmptyList}
import cats.implicits._
import models.JourneyModel._
import pages._
import queries.{AllProperties, Query}
import uk.gov.hmrc.domain.Nino

final case class JourneyModel(applicant: Applicant, partner: Partner, properties: NonEmptyList[Property])

object JourneyModel {

  final case class Applicant(name: Name, nino: Nino, utr: Option[Utr], address: Address)
  final case class Partner(name: Name, nino: Nino, utr: Option[Utr])

  def from(answers: UserAnswers): IorNec[Query, JourneyModel] =
    (
      getApplicant(answers),
      getPartner(answers),
      getProperties(answers)
    ).parMapN(JourneyModel.apply)

  private def getApplicantUtr(answers: UserAnswers): IorNec[Query, Option[Utr]] =
    answers.getIor(ApplicantHasUtrPage).flatMap {
      case true   => answers.getIor(ApplicantUtrPage).map(Some(_))
      case false => Ior.Right(None)
    }

  private def getPartnerUtr(answers: UserAnswers): IorNec[Query, Option[Utr]] =
    answers.getIor(PartnerHasUtrPage).flatMap {
      case true   => answers.getIor(PartnerUtrPage).map(Some(_))
      case false => Ior.Right(None)
    }

  private def getApplicant(answers: UserAnswers): IorNec[Query, Applicant] =
    (
      answers.getIor(ApplicantNamePage),
      answers.getIor(ApplicantNinoPage),
      getApplicantUtr(answers),
      getCurrentAddress(answers),
      ).parMapN(Applicant.apply)

  private def getCurrentAddress(answers: UserAnswers): IorNec[Query, Address] =
    answers.getIor(CurrentAddressInUkPage).flatMap {
      case true => answers.getIor(CurrentAddressUkPage)
      case false => answers.getIor(CurrentAddressInternationalPage)
    }

  private def getPartner(answers: UserAnswers): IorNec[Query, Partner] =
    (
      answers.getIor(PartnerNamePage),
      answers.getIor(PartnerNinoPage),
      getPartnerUtr(answers)
      ).parMapN(Partner.apply)

  private def getProperties(answers: UserAnswers): IorNec[Query, NonEmptyList[Property]] =
    NonEmptyList
      .fromList(answers.getIor(AllProperties).getOrElse(Nil))
      .toRightIor(NonEmptyChain.one(AllProperties))
}