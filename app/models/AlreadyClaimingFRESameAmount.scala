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

package models

import viewmodels.RadioCheckboxOption

sealed trait AlreadyClaimingFRESameAmount

object AlreadyClaimingFRESameAmount extends Enumerable.Implicits {

  case object NoChange extends WithName("noChange") with AlreadyClaimingFRESameAmount
  case object Remove extends WithName("remove") with AlreadyClaimingFRESameAmount

  val values: Seq[AlreadyClaimingFRESameAmount] = Seq(
    NoChange, Remove
  )

  val options: Seq[RadioCheckboxOption] = values.map {
    value =>
      RadioCheckboxOption("alreadyClaimingFRESameAmount", value.toString)
  }

  implicit val enumerable: Enumerable[AlreadyClaimingFRESameAmount] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
