/*
 * Copyright 2019 HM Revenue & Customs
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

package navigation

import controllers.routes
import javax.inject.Inject
import models.AncillaryEngineeringWhichTrade.{ApprenticeOrStorekeeper, LabourerSupervisorOrUnskilledWorker, PatternMaker}
import models.TypeOfEngineering._
import models.{AncillaryEngineeringWhichTrade, CheckMode, Mode, NormalMode, UserAnswers}
import pages.engineering._
import pages.{Page, TypeOfEngineeringPage}
import play.api.mvc.Call

class EngineeringNavigator @Inject()() extends Navigator {

  protected val routeMap: PartialFunction[Page, UserAnswers => Call] = {
    case TypeOfEngineeringPage => userAnswers => typeOfEngineeringOptions(NormalMode)(userAnswers)
    case ConstructionalEngineeringList1Page => userAnswers => constructionalEngineeringList1(NormalMode)(userAnswers)
    case ConstructionalEngineeringList2Page => userAnswers => constructionalEngineeringList2(NormalMode)(userAnswers)
    case ConstructionalEngineeringApprenticePage => userAnswers => constructionalEngineeringApprentice(NormalMode)(userAnswers)
    case AncillaryEngineeringWhichTradePage => userAnswers => ancillaryEngineeringWhichTrade(NormalMode)(userAnswers)
    case FactoryEngineeringList1Page => userAnswers => factoryEngineeringList1(NormalMode)(userAnswers)
    case FactoryEngineeringList2Page => userAnswers => factoryEngineeringList2(NormalMode)(userAnswers)
    case FactoryEngineeringApprenticePage => userAnswers => factoryEngineeringApprentice(NormalMode)(userAnswers)
    case _ => _ => routes.SessionExpiredController.onPageLoad()
  }

  protected val checkRouteMap: PartialFunction[Page, UserAnswers => Call] = {
    case TypeOfEngineeringPage => userAnswers => typeOfEngineeringOptions(CheckMode)(userAnswers)
    case _ => _ => routes.SessionExpiredController.onPageLoad()
  }

  private def typeOfEngineeringOptions(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(TypeOfEngineeringPage) match {
      case Some(ConstructionalEngineering) =>
        controllers.engineering.routes.ConstructionalEngineeringList1Controller.onPageLoad(mode)
      case Some(TradesRelatingToEngineering) =>
        controllers.engineering.routes.AncillaryEngineeringWhichTradeController.onPageLoad(mode)
      case Some(FactoryOrWorkshopEngineering) =>
        controllers.engineering.routes.FactoryEngineeringList1Controller.onPageLoad(mode)
      case Some(NoneOfTheAbove) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }

  private def constructionalEngineeringList1(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(ConstructionalEngineeringList1Page) match {
      case Some(true) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case Some(false) =>
        controllers.engineering.routes.ConstructionalEngineeringList2Controller.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }

  private def constructionalEngineeringList2(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(ConstructionalEngineeringList2Page) match {
      case Some(true) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case Some(false) =>
        controllers.engineering.routes.ConstructionalEngineeringApprenticeController.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }

  private def constructionalEngineeringApprentice(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(ConstructionalEngineeringApprenticePage) match {
      case Some(true) | Some(false) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }

  private def ancillaryEngineeringWhichTrade(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(AncillaryEngineeringWhichTradePage) match {
      case Some(_) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }

  private def factoryEngineeringList1(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(FactoryEngineeringList1Page) match {
      case Some(true) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case Some(false) =>
        controllers.engineering.routes.FactoryEngineeringList2Controller.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }

  private def factoryEngineeringList2(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(FactoryEngineeringList2Page) match {
      case Some(true) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case Some(false) =>
        controllers.engineering.routes.FactoryEngineeringApprenticeController.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }

  private def factoryEngineeringApprentice(mode: Mode)(userAnswers: UserAnswers): Call = {
    userAnswers.get(FactoryEngineeringApprenticePage) match {
      case Some(true) | Some(false) =>
        controllers.routes.EmployerContributionController.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }
}
