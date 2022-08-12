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

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryRemovePropertyUserAnswersEntry: Arbitrary[(RemovePropertyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RemovePropertyPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryShareOfPropertyUserAnswersEntry: Arbitrary[(ShareOfPropertyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ShareOfPropertyPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPropertyAddressUserAnswersEntry: Arbitrary[(PropertyAddressPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PropertyAddressPage.type]
        value <- arbitrary[PropertyAddress].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnerUtrUserAnswersEntry: Arbitrary[(PartnerUtrPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnerUtrPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnerNinoUserAnswersEntry: Arbitrary[(PartnerNinoPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnerNinoPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnerNameUserAnswersEntry: Arbitrary[(PartnerNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnerNamePage.type]
        value <- arbitrary[PartnerName].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnerHasUtrUserAnswersEntry: Arbitrary[(PartnerHasUtrPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnerHasUtrPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCurrentAddressUserAnswersEntry: Arbitrary[(CurrentAddressPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CurrentAddressPage.type]
        value <- arbitrary[CurrentAddress].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantUtrUserAnswersEntry: Arbitrary[(ApplicantUtrPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantUtrPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantNinoUserAnswersEntry: Arbitrary[(ApplicantNinoPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantNinoPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantNameUserAnswersEntry: Arbitrary[(ApplicantNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantNamePage.type]
        value <- arbitrary[ApplicantName].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantHasUtrUserAnswersEntry: Arbitrary[(ApplicantHasUtrPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantHasUtrPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddPropertyUserAnswersEntry: Arbitrary[(AddPropertyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddPropertyPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }
}
