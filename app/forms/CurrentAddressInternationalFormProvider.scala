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
import models.{Country, InternationalAddress}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject.Inject

class CurrentAddressInternationalFormProvider @Inject() extends Mappings {

  def apply(): Form[InternationalAddress] = Form(
    mapping(
      "line1" -> text("currentAddressInternational.error.line1.required")
        .verifying(maxLength(100, "currentAddressInternational.error.line1.length")),
      "line2" -> optional(text("currentAddressInternational.error.line2.required")
        .verifying(maxLength(100, "currentAddressInternational.error.line2.length"))),
      "town" -> text("currentAddressInternational.error.town.required")
        .verifying(maxLength(100, "currentAddressInternational.error.town.length")),
      "state" -> optional(text("currentAddressInternational.error.state.required")
        .verifying(maxLength(100, "currentAddressInternational.error.state.length"))),
      "postcode" -> optional(text("currentAddressInternational.error.postcode.required")
        .verifying(maxLength(100, "currentAddressInternational.error.postcode.length"))),
      "country" -> text("currentAddressInternational.error.country.required")
        .verifying("currentAddressInternational.error.country.required", value => Country.internationalCountries.exists(_.code == value))
        .transform[Country](value => Country.internationalCountries.find(_.code == value).get, _.code)
    )(InternationalAddress.apply)(InternationalAddress.unapply)
  )
}
