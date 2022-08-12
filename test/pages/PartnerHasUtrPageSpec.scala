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

import models.Utr
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class PartnerHasUtrPageSpec extends PageBehaviours {

  "PartnerHasUtrPage" - {

    beRetrievable[Boolean](PartnerHasUtrPage)

    beSettable[Boolean](PartnerHasUtrPage)

    beRemovable[Boolean](PartnerHasUtrPage)

    "Partner UTR must be removed when the answer is set to false" in {

      val initialAnswers = emptyUserAnswers.set(PartnerUtrPage, arbitrary[Utr].sample.value).success.value

      val result = initialAnswers.set(PartnerHasUtrPage, false).success.value
      result.get(PartnerUtrPage) must not be defined
    }
  }
}
