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

package views.manufacturing

import controllers.manufacturing.routes
import forms.manufacturing.AluminiumOccupationList1FormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.manufacturing.AluminiumOccupationList1View

class AluminiumOccupationList1ViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "aluminiumOccupationList1"

  val form = new AluminiumOccupationList1FormProvider()()

  val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

  "AluminiumOccupationList1 view" must {

    val view = application.injector.instanceOf[AluminiumOccupationList1View]

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages, hc)

    def applyViewWithAuth(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages, hcWithAuth)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like normalPageWithAccountMenu(applyViewWithAuth(form))

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, routes.AluminiumOccupationList1Controller.onSubmit(NormalMode).url)

    behave like pageWithList(applyView(form), messageKeyPrefix,
      Seq(
        "occupation1",
        "occupation2",
        "occupation3",
        "occupation4",
        "occupation5",
        "occupation6",
        "occupation7"
      )
    )

    behave like pageWithSecondaryHeader(applyView(form), messages(s"$messageKeyPrefix.secondaryHeading"))
  }

  application.stop()
}
