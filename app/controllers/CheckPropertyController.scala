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

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.{Index, NormalMode}
import navigation.Navigator
import pages.CheckPropertyPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.{PropertyAddressSummary, ShareOfPropertySummary}
import viewmodels.govuk.summarylist._
import views.html.CheckPropertyView

class CheckPropertyController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            navigator: Navigator,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: CheckPropertyView
                                          ) extends FrontendBaseController with I18nSupport {

  def onPageLoad(index: Index): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val list = SummaryListViewModel(
        rows = Seq(
          PropertyAddressSummary.row(request.userAnswers, index),
          ShareOfPropertySummary.row(request.userAnswers, index),
          ShareOfPropertySummary.partnerRow(request.userAnswers, index)
        ).flatten
      )

      Ok(view(list, index))
  }

  def onSubmit(index: Index): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      Redirect(navigator.nextPage(CheckPropertyPage(index), NormalMode, request.userAnswers))
  }
}
