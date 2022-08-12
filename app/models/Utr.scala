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

import play.api.libs.json._

import scala.util.matching.Regex

final case class Utr private(value: String) {
  override def toString: String = value
}

object Utr {

  val pattern: Regex = "([k]?\\d{10}[k]?|[k]?\\d{13}[k]?)".r.anchored

  def fromString(value: String): Option[Utr] =
    value match {
      case pattern(numbers) => Some(Utr(numbers))
      case _                => None
    }

  implicit val reads: Reads[Utr] = new Reads[Utr] {
    override def reads(json: JsValue): JsResult[Utr] =
      json match {
        case s: JsString =>
          fromString(s.value)
            .map(JsSuccess(_))
            .getOrElse(JsError("not a valid UTR"))

        case _ => JsError("not a valid UTR")
      }
  }

  implicit val writes: Writes[Utr] = new Writes[Utr] {
    override def writes(o: Utr): JsValue = JsString(o.value)
  }
}
