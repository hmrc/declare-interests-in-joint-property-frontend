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
import cats.data.NonEmptyList
import generators.ModelGenerators
import models.{JourneyModel, Utr}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import scala.concurrent.ExecutionContext.Implicits.global

class AuditServiceSpec extends AnyFreeSpec with Matchers with MockitoSugar with ModelGenerators with OptionValues {

  val mockAuditConnector: AuditConnector = mock[AuditConnector]
  val configuration: Configuration = Configuration(
    "auditing.downloadEventName" -> "downloadAuditEvent"
  )
  val service = new AuditService(mockAuditConnector, configuration)

  ".auditDownload" - {

    "must call the audit connector with the correct payload" in {

      val applicantNino = arbitrary[Nino].sample.value
      val applicantUtr  = arbitrary[Utr].sample.value
      val partnerNino   = arbitrary[Nino].sample.value
      val partnerUtr    = arbitrary[Utr].sample.value

      val model = JourneyModel(
        applicant = JourneyModel.Applicant(
          name    = models.Name("first", "last"),
          nino    = applicantNino,
          utr     = Some(applicantUtr),
          address = models.Address("line 1", Some("line 2"), "town", Some("county"), "postcode")
        ),
        partner = JourneyModel.Partner(
          name = models.Name("partner first", "partner last"),
          nino = partnerNino,
          utr  = Some(partnerUtr)
        ),
        properties = NonEmptyList(
          head = models.Property(
            address        = models.Address("p1 line 1", Some("p1 line 2"), "p1 town", Some("p1 county"), "p1 postcode"),
            applicantShare = 1
          ),
          tail = List(models.Property(
            address        = models.Address("p2 line 1", Some("p2 line 2"), "p2 town", Some("p2 county"), "p2 postcode"),
            applicantShare = 2
          ))
        )
      )

      val expectedAuditEvent = DownloadAuditEvent(
        applicant = Applicant(
          name    = Name("first", "last"),
          nino    = applicantNino.value,
          utr     = Some(applicantUtr.value),
          address = Address("line 1", Some("line 2"), "town", Some("county"), "postcode")
        ),
        partner = Partner(
          name    = Name("partner first", "partner last"),
          nino    = partnerNino.value,
          utr     = Some(partnerUtr.value)
        ),
        properties = List(
          Property(
            address        = Address("p1 line 1", Some("p1 line 2"), "p1 town", Some("p1 county"), "p1 postcode"),
            applicantShare = 1
          ),
          Property(
            address        = Address("p2 line 1", Some("p2 line 2"), "p2 town", Some("p2 county"), "p2 postcode"),
            applicantShare = 2
          )
        )
      )

      val hc = HeaderCarrier()
      service.auditDownload(model)(hc)

      verify(mockAuditConnector, times(1)).sendExplicitAudit(eqTo("downloadAuditEvent"), eqTo(expectedAuditEvent))(eqTo(hc), any(), any())
    }
  }
}
