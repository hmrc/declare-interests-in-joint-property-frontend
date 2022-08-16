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

package audit

import audit.DownloadAuditEvent._
import models.JourneyModel
import play.api.libs.json.{Format, Json}

final case class DownloadAuditEvent(applicant: Applicant, partner: Partner, properties: List[Property])

object DownloadAuditEvent {

  def from(model: JourneyModel): DownloadAuditEvent =
    DownloadAuditEvent(
      applicant = Applicant(
        name    = Name(model.applicant.name.firstName, model.applicant.name.lastName),
        nino    = model.applicant.nino.value,
        utr     = model.applicant.utr.map(_.value),
        address = convertAddress(model.applicant.address)
      ),
      partner = Partner(
        name = Name(model.partner.name.firstName, model.partner.name.lastName),
        nino = model.partner.nino.value,
        utr  = model.partner.utr.map(_.value)
      ),
      properties = model.properties.toList.map(convertProperty)
    )

  private def convertAddress(address: models.Address): Address =
    Address(
      line1      = address.line1,
      line2      = address.line2,
      townOrCity = address.townOrCity,
      county     = address.county,
      postcode   = address.postcode
    )

  private def convertProperty(property: models.Property): Property =
    Property(
      address        = convertAddress(property.address),
      applicantShare = property.applicantShare
    )

  private[audit] final case class Applicant(name: Name, nino: String, utr: Option[String], address: Address)
  object Applicant {
    implicit lazy val formats: Format[Applicant] = Json.format
  }

  private[audit] final case class Partner(name: Name, nino: String, utr: Option[String])
  object Partner {
    implicit lazy val formats: Format[Partner] = Json.format
  }

  private[audit] final case class Name(firstName: String, lastName: String)
  object Name {
    implicit lazy val formats: Format[Name] = Json.format
  }

  private[audit] final case class Address(line1: String, line2: Option[String], townOrCity: String, county: Option[String], postcode: String)
  object Address {
    implicit lazy val formats: Format[Address] = Json.format
  }

  private[audit] final case class Property(address: Address, applicantShare: Int)
  object Property {
    implicit lazy val formats: Format[Property] = Json.format
  }

  implicit lazy val formats: Format[DownloadAuditEvent] = Json.format
}
