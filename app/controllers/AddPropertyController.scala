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

import controllers.actions._
import forms.AddPropertyFormProvider

import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.AddPropertyPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.AddPropertySummary
import views.html.AddPropertyView

import scala.concurrent.{ExecutionContext, Future}

class AddPropertyController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: AddPropertyFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: AddPropertyView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val propertySummaries = AddPropertySummary.rows(request.userAnswers)

      Ok(view(form, mode, propertySummaries))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {
          val propertySummaries = AddPropertySummary.rows(request.userAnswers)

          Future.successful(BadRequest(view(formWithErrors, mode, propertySummaries)))
        },

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(AddPropertyPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(AddPropertyPage, mode, updatedAnswers))
      )
  }
}
