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

package controllers

import audit.AuditService
import base.SpecBase
import com.dmanchester.playfop.sapi.PlayFop
import generators.ModelGenerators
import models.{Address, Index, Name, UserAnswers, Utr}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{never, times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import pages._
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino
import views.html.PrintView

import java.nio.charset.Charset

class PrintControllerSpec extends SpecBase with ModelGenerators with MockitoSugar {

  private val applicantName    = arbitrary[Name].sample.value
  private val applicantNino    = arbitrary[Nino].sample.value
  private val applicantUtr     = arbitrary[Utr].sample.value
  private val partnerName      = arbitrary[Name].sample.value
  private val partnerNino      = arbitrary[Nino].sample.value
  private val partnerUtr       = arbitrary[Utr].sample.value
  private val currentAddressUk   = arbitrary[Address].sample.value
  private val propertyAddress1 = arbitrary[Address].sample.value
  private val propertyAddress2 = arbitrary[Address].sample.value
  private val shareOfProperty1 = Gen.choose(1, 99).sample.value
  private val shareOfProperty2 = Gen.choose(1, 99).sample.value

  private val completeAnswers = UserAnswers("id")
    .set(ApplicantNamePage, applicantName).success.value
    .set(ApplicantNinoPage, applicantNino).success.value
    .set(ApplicantHasUtrPage, true).success.value
    .set(ApplicantUtrPage, applicantUtr).success.value
    .set(PartnerNamePage, partnerName).success.value
    .set(PartnerNinoPage, partnerNino).success.value
    .set(PartnerHasUtrPage, true).success.value
    .set(PartnerUtrPage, partnerUtr).success.value
    .set(CurrentAddressUkPage, currentAddressUk).success.value
    .set(PropertyAddressPage(Index(0)), propertyAddress1).success.value
    .set(ShareOfPropertyPage(Index(0)), shareOfProperty1).success.value
    .set(PropertyAddressPage(Index(1)), propertyAddress2).success.value
    .set(ShareOfPropertyPage(Index(1)), shareOfProperty2).success.value

  "Print Controller" - {

    "must return OK and the correct view when user answers are complete" in {

      val application = applicationBuilder(userAnswers = Some(completeAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onPageLoad.url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[PrintView]

        status(result) mustEqual OK

        contentAsString(result) mustEqual view()(request, messages(application)).toString
      }
    }

    "must redirect to journey recovery for onPageLoad when user answers are not complete" in {

      val incompleteAnswers = completeAnswers.remove(ApplicantNamePage).success.value

      val application = applicationBuilder(userAnswers = Some(incompleteAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onPageLoad.url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must return OK and audit the download for onDownload when user answers are complete" in {

      val mockAuditService = mock[AuditService]
      val mockFop = mock[PlayFop]
      when(mockFop.processTwirlXml(any(), any(), any(), any())) thenReturn "hello".getBytes

      val application =
        applicationBuilder(userAnswers = Some(completeAnswers))
          .overrides(
            bind[PlayFop].toInstance(mockFop),
            bind[AuditService].toInstance(mockAuditService)
          )
          .build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onDownload.url)

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsBytes(result).decodeString(Charset.defaultCharset()) mustEqual "hello"
        verify(mockAuditService, times(1)).auditDownload(any())(any())
      }
    }

    "must redirect to journey recovery and not audit the download for onDownload when user answers are not complete" in {

      val incompleteAnswers = completeAnswers.remove(ApplicantNamePage).success.value

      val mockAuditService = mock[AuditService]
      val mockFop = mock[PlayFop]
      when(mockFop.processTwirlXml(any(), any(), any(), any())) thenReturn "hello".getBytes

      val application =
        applicationBuilder(userAnswers = Some(incompleteAnswers))
          .overrides(
            bind[PlayFop].toInstance(mockFop),
            bind[AuditService].toInstance(mockAuditService)
          )
          .build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onDownload.url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
        verify(mockAuditService, never()).auditDownload(any())(any())
      }
    }
  }
}
