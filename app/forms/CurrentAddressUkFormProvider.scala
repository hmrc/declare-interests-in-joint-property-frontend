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

import forms.mappings.Mappings
import models.{Address, UkAddress}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject.Inject

class CurrentAddressUkFormProvider @Inject() extends Mappings {

  def apply(): Form[UkAddress] = Form(
    mapping(
      "line1" -> text("currentAddressUk.error.line1.required")
        .verifying(maxLength(100, "currentAddressUk.error.line1.length")),
      "line2" -> optional(text("currentAddressUk.error.line2.required")
        .verifying(maxLength(100, "currentAddressUk.error.line2.length"))),
      "townOrCity" -> text("currentAddressUk.error.townOrCity.required")
        .verifying(maxLength(100, "currentAddressUk.error.townOrCity.length")),
      "county" -> optional(text("currentAddressUk.error.county.required")
        .verifying(maxLength(100, "currentAddressUk.error.county.length"))),
      "postcode" -> text("currentAddressUk.error.postcode.required")
        .verifying(maxLength(100, "currentAddressUk.error.postcode.length"))
    )(UkAddress.apply)(UkAddress.unapply)
  )
}
