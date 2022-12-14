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
import uk.gov.hmrc.domain.Nino

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryCurrentAddressInternationalUserAnswersEntry: Arbitrary[(CurrentAddressInternationalPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CurrentAddressInternationalPage.type]
        value <- arbitrary[InternationalAddress].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCurrentAddressInUkUserAnswersEntry: Arbitrary[(CurrentAddressInUkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CurrentAddressInUkPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryShareOfPropertyUserAnswersEntry: Arbitrary[(ShareOfPropertyPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ShareOfPropertyPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPropertyAddressUserAnswersEntry: Arbitrary[(PropertyAddressPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PropertyAddressPage]
        value <- arbitrary[UkAddress].map(Json.toJson(_))
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
        value <- arbitrary[Nino].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnerNameUserAnswersEntry: Arbitrary[(PartnerNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnerNamePage.type]
        value <- arbitrary[Name].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnerHasUtrUserAnswersEntry: Arbitrary[(PartnerHasUtrPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnerHasUtrPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCurrentAddressUkUserAnswersEntry: Arbitrary[(CurrentAddressUkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CurrentAddressUkPage.type]
        value <- arbitrary[UkAddress].map(Json.toJson(_))
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
        value <- arbitrary[Nino].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryApplicantNameUserAnswersEntry: Arbitrary[(ApplicantNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ApplicantNamePage.type]
        value <- arbitrary[Name].map(Json.toJson(_))
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
