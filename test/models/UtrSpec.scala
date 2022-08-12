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

import base.SpecBase
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsSuccess, Json}

class UtrSpec extends SpecBase with OptionValues with ScalaCheckPropertyChecks with ModelGenerators {

  "a Utr" - {

    "must serialise and deserialise to / from JSON" in {

      forAll(arbitrary[Utr]) {
        utr =>
          val json = Json.toJson(utr)
          json.validate[Utr] mustBe a[JsSuccess[_]]
      }
    }
  }


  ".fromString" - {

    "must return a UTR" - {

      "when given 10 digits" in {

        forAll(Gen.listOfN(10, Gen.numChar).map(_.mkString)) {
          validString =>
            Utr.fromString(validString).value mustEqual Utr(validString)
        }
      }

      "when given 10 digits with leading and trailing `k`" in {

        forAll(Gen.listOfN(10, Gen.numChar).map(_.mkString)) {
          digits =>
            val validString = s"k${digits}k"
            Utr.fromString(validString).value mustEqual Utr(validString)
        }
      }

      "when given 13 digits" in {

        forAll(Gen.listOfN(13, Gen.numChar).map(_.mkString)) {
          validString =>
            Utr.fromString(validString).value mustEqual Utr(validString)
        }
      }

      "when given 13 digits with leading and trailing `k`" in {

        forAll(Gen.listOfN(13, Gen.numChar).map(_.mkString)) {
          digits =>
            val validString = s"k${digits}k"
            Utr.fromString(validString).value mustEqual Utr(validString)
        }
      }
    }

    "must not return a UTR" - {

      "when given fewer than 10 digits" in {

        val gen = for {
          numDigits <- Gen.choose(1, 9)
          digits <- Gen.listOfN(numDigits, Gen.numChar)
        } yield digits.mkString

        forAll(gen) {
          digits =>
            Utr.fromString(digits) must not be defined
        }
      }

      "when given 11 or 12 digits" in {

        val gen = for {
          numDigits <- Gen.choose(11, 12)
          digits <- Gen.listOfN(numDigits, Gen.numChar)
        } yield digits.mkString

        forAll(gen) {
          digits =>
            Utr.fromString(digits) must not be defined
        }
      }

      "when given more than 13 digits" in {

        val gen = for {
          numDigits <- Gen.choose(14, 100)
          digits <- Gen.listOfN(numDigits, Gen.numChar)
        } yield digits.mkString

        forAll(gen) {
          digits =>
            Utr.fromString(digits) must not be defined
        }
      }

      "when given characters other than `k` at the start or end" in {

        Utr.fromString("a1234567890") must not be defined
        Utr.fromString("1234567890z") must not be defined
      }
    }
  }
}
