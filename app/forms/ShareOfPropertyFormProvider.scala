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

import javax.inject.Inject
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid}
import scala.util.Try

class ShareOfPropertyFormProvider @Inject() extends Mappings {

  def apply(): Form[Int] =
    Form(
      "value" -> text("shareOfProperty.error.required")
        .verifying(firstError(
          inverseRegexp("""^-?%?(\d*\.\d*)%?$""", "shareOfProperty.error.wholeNumber"),
          isNumeric("shareOfProperty.error.nonNumeric")
        ))
        .transform[Int](s => s.replaceAll("%", "").toInt, i => i.toString)
        .verifying(
          inRange(1, 99, "shareOfProperty.error.outOfRange"),
          notEqualShare)
    )

  private def notEqualShare: Constraint[Int] = Constraint {
    case n if n == 50 => Invalid("shareOfProperty.error.equalShare")
    case _            => Valid
  }

  private def inverseRegexp(regex: String, errorKey: String): Constraint[String] =
    Constraint {
      case str if str.matches(regex) =>
        Invalid(errorKey)
      case _ =>
        Valid
    }
  private def isNumeric(errorKey: String): Constraint[String] =
    Constraint {
      case str if Try(str.replaceAll("%", "").toInt).isSuccess =>
        Valid
      case _ =>
        Invalid(errorKey)
    }
}