package br.com.zupacademy.sergio.proposal.model.external

import br.com.zupacademy.sergio.proposal.model.TravelNotice
import java.time.LocalDate

class TravelNoticeNotificationRequest(travelNotice: TravelNotice) {
  val destino: String = travelNotice.destination
  val validoAte: LocalDate = travelNotice.endDate
}
