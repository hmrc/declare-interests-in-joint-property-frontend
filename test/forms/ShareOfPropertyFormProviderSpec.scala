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

package forms

import forms.behaviours.IntFieldBehaviours
import org.scalacheck.Gen
import play.api.data.FormError

class ShareOfPropertyFormProviderSpec extends IntFieldBehaviours {

  val form = new ShareOfPropertyFormProvider()()

  ".value" - {

    val fieldName = "value"

    val minimum = 1
    val maximum = 99

    val validDataGenerator = Gen.choose(1, 99).suchThat(_ != 50).map(_.toString)

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validDataGenerator
    )

    behave like intField(
      form,
      fieldName,
      nonNumericError  = FormError(fieldName, "shareOfProperty.error.nonNumeric"),
      wholeNumberError = FormError(fieldName, "shareOfProperty.error.wholeNumber")
    )

    behave like intFieldWithRange(
      form,
      fieldName,
      minimum       = minimum,
      maximum       = maximum,
      expectedError = FormError(fieldName, "shareOfProperty.error.outOfRange", Seq(minimum, maximum))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, "shareOfProperty.error.required")
    )

    "must not bind a value of exactly 50" in {
      val result = form.bind(Map("value" -> "50"))
      result.errors must contain only FormError(fieldName, "shareOfProperty.error.equalShare")
    }
  }
}
