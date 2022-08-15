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

package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, Index, NormalMode, UserAnswers}
import pages.{AddPropertyPage, CheckPropertyPage}
import play.api.i18n.Messages
import play.twirl.api.{Html, HtmlFormat}
import queries.AllProperties
import uk.gov.hmrc.govukfrontend.views.Aliases.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddPropertySummary  {

  def rows(answers: UserAnswers): Seq[ListItem] =
    answers.get(AllProperties).getOrElse(Nil).zipWithIndex.map {
      case (property, index) =>

        val name = property.address.lines.map(HtmlFormat.escape).mkString(", ")

        ListItem(
          name      = name,
          changeUrl = routes.CheckPropertyController.onPageLoad(Index(index)).url,
          removeUrl = routes.RemovePropertyController.onPageLoad(NormalMode, Index(index)).url
        )
    }

  def checkAnswersRow(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllProperties).map {
      properties =>

        val propertyAddresses = properties.map {
          property =>
            property.address.lines.map(HtmlFormat.escape).mkString("<br/>")
        }

        val value = propertyAddresses.mkString("<br/><br/>")

        SummaryListRowViewModel(
          key     = "addProperty.checkYourAnswersLabel",
          value   = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel("site.change", routes.AddPropertyController.onPageLoad(CheckMode).url)
              .withVisuallyHiddenText(messages("addProperty.change.hidden"))
          )
        )
    }
}
