/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers.actions

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.routes._
import models.requests.IdentifierRequest
import play.api.Logging
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve._
import uk.gov.hmrc.http.{HeaderCarrier, UnauthorizedException}
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedIdentifierActionImpl @Inject()(
                                                   override val authConnector: AuthConnector,
                                                   config: FrontendAppConfig,
                                                   val parser: BodyParsers.Default
                                                 )(implicit val executionContext: ExecutionContext) extends AuthenticatedIdentifierAction with AuthorisedFunctions with Logging {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    authorised(AuthProviders(AuthProvider.Verify) or (AffinityGroup.Individual and ConfidenceLevel.L200))
      .retrieve(v2.Retrievals.nino and v2.Retrievals.internalId) {
        case Some(nino) ~ Some(internalId) =>
          block(IdentifierRequest(request, Authed(internalId), Some(nino)))
        case _ =>
          throw new UnauthorizedException("Unauthorized exception: missing nino or internal id")
      } recover {
      case _: UnauthorizedException | _: NoActiveSession =>
        unauthorised(hc.sessionId.map(_.value))
      case _: InsufficientConfidenceLevel =>
        hc.sessionId.map(_.value) match {
          case Some(id) => insufficientConfidence(request.getQueryString("key").getOrElse(id))
          case _ => Redirect(SessionExpiredController.onPageLoad())
        }
      case _: AuthorisationException =>
        Redirect(UnauthorisedController.onPageLoad())
      case e =>
        logger.error(s"[AuthenticatedIdentifierAction][authorised] failed $e", e)
        Redirect(TechnicalDifficultiesController.onPageLoad())
    }
  }

  def unauthorised(sessionId: Option[String]): Result = {
    sessionId match {
      case Some(id) =>
        Redirect(config.loginUrl, Map("continue" -> Seq(s"${config.loginContinueUrl + id}")))
      case _ =>
        Redirect(SessionExpiredController.onPageLoad())
    }
  }

  def insufficientConfidence(queryString: String): Result = {
    Redirect(s"${config.ivUpliftUrl}?origin=EE&confidenceLevel=200" +
      s"&completionURL=${config.authorisedCallback + queryString}" +
      s"&failureURL=${config.unauthorisedCallback}")
  }

}

trait AuthenticatedIdentifierAction extends ActionBuilder[IdentifierRequest, AnyContent] with ActionFunction[Request, IdentifierRequest]
