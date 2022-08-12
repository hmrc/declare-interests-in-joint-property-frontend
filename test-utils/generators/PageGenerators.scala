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

package generators

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryRemovePropertyPage: Arbitrary[RemovePropertyPage.type] =
    Arbitrary(RemovePropertyPage)

  implicit lazy val arbitraryShareOfPropertyPage: Arbitrary[ShareOfPropertyPage.type] =
    Arbitrary(ShareOfPropertyPage)

  implicit lazy val arbitraryPropertyAddressPage: Arbitrary[PropertyAddressPage.type] =
    Arbitrary(PropertyAddressPage)

  implicit lazy val arbitraryPartnerUtrPage: Arbitrary[PartnerUtrPage.type] =
    Arbitrary(PartnerUtrPage)

  implicit lazy val arbitraryPartnerNinoPage: Arbitrary[PartnerNinoPage.type] =
    Arbitrary(PartnerNinoPage)

  implicit lazy val arbitraryPartnerNamePage: Arbitrary[PartnerNamePage.type] =
    Arbitrary(PartnerNamePage)

  implicit lazy val arbitraryPartnerHasUtrPage: Arbitrary[PartnerHasUtrPage.type] =
    Arbitrary(PartnerHasUtrPage)

  implicit lazy val arbitraryCurrentAddressPage: Arbitrary[CurrentAddressPage.type] =
    Arbitrary(CurrentAddressPage)

  implicit lazy val arbitraryApplicantUtrPage: Arbitrary[ApplicantUtrPage.type] =
    Arbitrary(ApplicantUtrPage)

  implicit lazy val arbitraryApplicantNinoPage: Arbitrary[ApplicantNinoPage.type] =
    Arbitrary(ApplicantNinoPage)

  implicit lazy val arbitraryApplicantNamePage: Arbitrary[ApplicantNamePage.type] =
    Arbitrary(ApplicantNamePage)

  implicit lazy val arbitraryApplicantHasUtrPage: Arbitrary[ApplicantHasUtrPage.type] =
    Arbitrary(ApplicantHasUtrPage)

  implicit lazy val arbitraryAddPropertyPage: Arbitrary[AddPropertyPage.type] =
    Arbitrary(AddPropertyPage)
}
