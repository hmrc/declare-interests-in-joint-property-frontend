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

import cats.data.NonEmptyList
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.{EitherValues, OptionValues, TryValues}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import pages._
import queries.AllProperties
import uk.gov.hmrc.domain.Nino

class JourneyModelSpec
  extends AnyFreeSpec
    with Matchers
    with TryValues
    with EitherValues
    with OptionValues
    with ModelGenerators {

  private val applicantName    = arbitrary[Name].sample.value
  private val applicantNino    = arbitrary[Nino].sample.value
  private val applicantUtr     = arbitrary[Utr].sample.value
  private val partnerName      = arbitrary[Name].sample.value
  private val partnerNino      = arbitrary[Nino].sample.value
  private val partnerUtr       = arbitrary[Utr].sample.value
  private val currentAddress   = arbitrary[Address].sample.value
  private val propertyAddress1 = arbitrary[Address].sample.value
  private val propertyAddress2 = arbitrary[Address].sample.value
  private val shareOfProperty1 = Gen.choose(1, 99).sample.value
  private val shareOfProperty2 = Gen.choose(1, 99).sample.value

  ".from" - {

    "must create a journey model" - {

      "from minimal answers" in {

        val answers = UserAnswers("id")
          .set(ApplicantNamePage, applicantName).success.value
          .set(ApplicantNinoPage, applicantNino).success.value
          .set(ApplicantHasUtrPage, false).success.value
          .set(PartnerNamePage, partnerName).success.value
          .set(PartnerNinoPage, partnerNino).success.value
          .set(PartnerHasUtrPage, false).success.value
          .set(CurrentAddressPage, currentAddress).success.value
          .set(PropertyAddressPage(Index(0)), propertyAddress1).success.value
          .set(ShareOfPropertyPage(Index(0)), shareOfProperty1).success.value

        val expectedModel = JourneyModel(
          applicant = JourneyModel.Applicant(
            name    = applicantName,
            nino    = applicantNino,
            utr     = None,
            address = currentAddress
          ),
          partner = JourneyModel.Partner(
            name = partnerName,
            nino = partnerNino,
            utr  = None
          ),
          properties = NonEmptyList(
            head = Property(propertyAddress1, shareOfProperty1),
            tail = Nil
          )
        )

        val (errors, data) = JourneyModel.from(answers).pad

        errors mustBe empty
        data.value mustEqual expectedModel
      }

      "from full answers" in {

        val answers = UserAnswers("id")
          .set(ApplicantNamePage, applicantName).success.value
          .set(ApplicantNinoPage, applicantNino).success.value
          .set(ApplicantHasUtrPage, true).success.value
          .set(ApplicantUtrPage, applicantUtr).success.value
          .set(PartnerNamePage, partnerName).success.value
          .set(PartnerNinoPage, partnerNino).success.value
          .set(PartnerHasUtrPage, true).success.value
          .set(PartnerUtrPage, partnerUtr).success.value
          .set(CurrentAddressPage, currentAddress).success.value
          .set(PropertyAddressPage(Index(0)), propertyAddress1).success.value
          .set(ShareOfPropertyPage(Index(0)), shareOfProperty1).success.value
          .set(PropertyAddressPage(Index(1)), propertyAddress2).success.value
          .set(ShareOfPropertyPage(Index(1)), shareOfProperty2).success.value

        val expectedModel = JourneyModel(
          applicant = JourneyModel.Applicant(
            name    = applicantName,
            nino    = applicantNino,
            utr     = Some(applicantUtr),
            address = currentAddress
          ),
          partner = JourneyModel.Partner(
            name = partnerName,
            nino = partnerNino,
            utr  = Some(partnerUtr)
          ),
          properties = NonEmptyList(
            head = Property(propertyAddress1, shareOfProperty1),
            tail = List(Property(propertyAddress2, shareOfProperty2))
          )
        )

        val (errors, data) = JourneyModel.from(answers).pad

        errors mustBe empty
        data.value mustEqual expectedModel
      }
    }

    "must fail and report the missing pages" - {

      "when any mandatory data is missing" in {

        val (errors, _) = JourneyModel.from(UserAnswers("id")).pad

        errors.value.toChain.toList must contain theSameElementsInOrderAs Seq(
          ApplicantNamePage,
          ApplicantNinoPage,
          ApplicantHasUtrPage,
          CurrentAddressPage,
          PartnerNamePage,
          PartnerNinoPage,
          PartnerHasUtrPage,
          AllProperties
        )
      }

      "when the user said they had a UTR but none is present" in {

        val answers = UserAnswers("id")
          .set(ApplicantNamePage, applicantName).success.value
          .set(ApplicantNinoPage, applicantNino).success.value
          .set(ApplicantHasUtrPage, true).success.value
          .set(PartnerNamePage, partnerName).success.value
          .set(PartnerNinoPage, partnerNino).success.value
          .set(PartnerHasUtrPage, false).success.value
          .set(CurrentAddressPage, currentAddress).success.value
          .set(PropertyAddressPage(Index(0)), propertyAddress1).success.value
          .set(ShareOfPropertyPage(Index(0)), shareOfProperty1).success.value

        val (errors, _) = JourneyModel.from(answers).pad

        errors.value.toChain.toList must contain only ApplicantUtrPage
      }

      "when the user said their partner had a UTR but none is present" in {

        val answers = UserAnswers("id")
          .set(ApplicantNamePage, applicantName).success.value
          .set(ApplicantNinoPage, applicantNino).success.value
          .set(ApplicantHasUtrPage, false).success.value
          .set(PartnerNamePage, partnerName).success.value
          .set(PartnerNinoPage, partnerNino).success.value
          .set(PartnerHasUtrPage, true).success.value
          .set(CurrentAddressPage, currentAddress).success.value
          .set(PropertyAddressPage(Index(0)), propertyAddress1).success.value
          .set(ShareOfPropertyPage(Index(0)), shareOfProperty1).success.value

        val (errors, _) = JourneyModel.from(answers).pad

        errors.value.toChain.toList must contain only PartnerUtrPage
      }

      "when no properties are present" in {

        val answers = UserAnswers("id")
          .set(ApplicantNamePage, applicantName).success.value
          .set(ApplicantNinoPage, applicantNino).success.value
          .set(ApplicantHasUtrPage, false).success.value
          .set(PartnerNamePage, partnerName).success.value
          .set(PartnerNinoPage, partnerNino).success.value
          .set(PartnerHasUtrPage, false).success.value
          .set(CurrentAddressPage, currentAddress).success.value

        val (errors, _) = JourneyModel.from(answers).pad

        errors.value.toChain.toList must contain only AllProperties
      }
    }
  }
}
