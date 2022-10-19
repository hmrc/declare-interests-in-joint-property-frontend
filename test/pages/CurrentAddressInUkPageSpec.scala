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

package pages

import generators.ModelGenerators
import models.{InternationalAddress, UkAddress}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class CurrentAddressInUkPageSpec extends PageBehaviours with ModelGenerators {

  "CurrentAddressInUkPage" - {

    beRetrievable[Boolean](CurrentAddressInUkPage)

    beSettable[Boolean](CurrentAddressInUkPage)

    beRemovable[Boolean](CurrentAddressInUkPage)

    "must delete current UK address when the answer is no" in {

      val answers =
        emptyUserAnswers
          .set(CurrentAddressUkPage, arbitrary[UkAddress].sample.value).success.value
          .set(CurrentAddressInternationalPage, arbitrary[InternationalAddress].sample.value).success.value

      val result = answers.set(CurrentAddressInUkPage, false).success.value

      result.get(CurrentAddressUkPage) must not be defined
      result.get(CurrentAddressInternationalPage) mustBe defined
    }

    "must delete current international address when the answer is yes" in {

      val answers =
        emptyUserAnswers
          .set(CurrentAddressUkPage, arbitrary[UkAddress].sample.value).success.value
          .set(CurrentAddressInternationalPage, arbitrary[InternationalAddress].sample.value).success.value

      val result = answers.set(CurrentAddressInUkPage, true).success.value

      result.get(CurrentAddressUkPage) mustBe defined
      result.get(CurrentAddressInternationalPage) must not be defined
    }
  }
}
