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

import base.SpecBase
import models.AncillaryEngineeringWhichTrade.{ApprenticeOrStorekeeper, LabourerSupervisorOrUnskilledWorker, PatternMaker}
import models.TypeOfEngineering._
import models._
import org.scalatest.prop.PropertyChecks
import pages._
import pages.engineering.{AncillaryEngineeringWhichTradePage, ConstructionalEngineeringApprenticePage, ConstructionalEngineeringList1Page, ConstructionalEngineeringList2Page}

class EngineeringNavigatorSpec extends SpecBase with PropertyChecks {

  val navigator = new EngineeringNavigator

  "Engineering Navigator" when {
    "in Normal mode" must {

      "from TypeOfEngineering" must {

        "go to ConstructionalEngineeringList1 when ConstructionalEngineering is selected" in {
          val answers = emptyUserAnswers.set(TypeOfEngineeringPage, ConstructionalEngineering).success.value

          navigator.nextPage(TypeOfEngineeringPage, NormalMode)(answers) mustBe
            controllers.engineering.routes.ConstructionalEngineeringList1Controller.onPageLoad(NormalMode)
        }

        "go to AncillaryEngineeringWhichTrade when AncillaryEngineering is selected " in {
          val answers = emptyUserAnswers.set(TypeOfEngineeringPage, TradesRelatingToEngineering).success.value

          navigator.nextPage(TypeOfEngineeringPage, NormalMode)(answers) mustBe
            controllers.engineering.routes.AncillaryEngineeringWhichTradeController.onPageLoad(NormalMode)
        }

        "go to FactoryEngineeringList1 when FactoryOrWorkshopEngineering is selected " in {
          val answers = emptyUserAnswers.set(TypeOfEngineeringPage, FactoryOrWorkshopEngineering).success.value

          navigator.nextPage(TypeOfEngineeringPage, NormalMode)(answers) mustBe
            controllers.engineering.routes.FactoryEngineeringList1Controller.onPageLoad(NormalMode)
        }

        "go to EmployerContribution when NoneOfTheAbove is selected " in {
          val answers = emptyUserAnswers.set(TypeOfEngineeringPage, TypeOfEngineering.NoneOfTheAbove).success.value

          navigator.nextPage(TypeOfEngineeringPage, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }
      }

      //Constructional Engineering

      "from ConstructionalEngineeringList1" must {

        "go to EmployerContribution when Yes is selected" in {
          val answers = emptyUserAnswers.set(ConstructionalEngineeringList1Page, true).success.value

          navigator.nextPage(ConstructionalEngineeringList1Page, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }

        "go to ConstructionalEngineeringList2 when No is selected" in {
          val answers = emptyUserAnswers.set(ConstructionalEngineeringList1Page, false).success.value

          navigator.nextPage(ConstructionalEngineeringList1Page, NormalMode)(answers) mustBe
            controllers.engineering.routes.ConstructionalEngineeringList2Controller.onPageLoad(NormalMode)
        }
      }

      "from ConstructionalEngineeringList2" must {

        "go to EmployerContribution when Yes is selected" in {
          val answers = emptyUserAnswers.set(ConstructionalEngineeringList2Page, true).success.value

          navigator.nextPage(ConstructionalEngineeringList2Page, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }

        "go to ConstructionalEngineeringApprentice when No is selected" in {
          val answers = emptyUserAnswers.set(ConstructionalEngineeringList2Page, false).success.value

          navigator.nextPage(ConstructionalEngineeringList2Page, NormalMode)(answers) mustBe
            controllers.engineering.routes.ConstructionalEngineeringApprenticeController.onPageLoad(NormalMode)
        }
      }

      "from ConstructionalEngineeringApprentice" must {

        "go to EmployerContribution when Yes is selected" in {
          val answers = emptyUserAnswers.set(ConstructionalEngineeringApprenticePage, true).success.value

          navigator.nextPage(ConstructionalEngineeringApprenticePage, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }

        "go to EmployerContribution when No is selected" in {
          val answers = emptyUserAnswers.set(ConstructionalEngineeringApprenticePage, false).success.value

          navigator.nextPage(ConstructionalEngineeringApprenticePage, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }
      }


      //Ancillary Engineering

      "from AncillaryEngineeringWhichTrade" must {

        "go to EmployerContribution when PatternMaker is selected" in {
          val answers = emptyUserAnswers.set(AncillaryEngineeringWhichTradePage, PatternMaker).success.value

          navigator.nextPage(AncillaryEngineeringWhichTradePage, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }

        "go to EmployerContribution when LabourerSupervisorOrUnskilledWorker is selected" in {
          val answers = emptyUserAnswers.set(AncillaryEngineeringWhichTradePage, LabourerSupervisorOrUnskilledWorker).success.value

          navigator.nextPage(AncillaryEngineeringWhichTradePage, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }

        "go to EmployerContribution when ApprenticeOrStorekeeper is selected" in {
          val answers = emptyUserAnswers.set(AncillaryEngineeringWhichTradePage, ApprenticeOrStorekeeper).success.value

          navigator.nextPage(AncillaryEngineeringWhichTradePage, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }

        "go to EmployerContribution when NoneOfTheAbove is selected" in {
          val answers = emptyUserAnswers.set(AncillaryEngineeringWhichTradePage, AncillaryEngineeringWhichTrade.NoneOfTheAbove).success.value

          navigator.nextPage(AncillaryEngineeringWhichTradePage, NormalMode)(answers) mustBe
            controllers.routes.EmployerContributionController.onPageLoad(NormalMode)
        }
      }



        "in Check mode" must {

        }


    }
  }

}
