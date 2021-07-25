package br.com.zupacademy.sergio.proposal.model

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.persistence.*
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank

@Entity
class TravelNotice(
  @ManyToOne(optional = false)
  private val creditCard: CreditCard,

  @Column(nullable = false)
  val destination: String,

  @Column(nullable = false)
  val endDate: LocalDate,

  @Column(nullable = false)
  val requestUserAgent: String,

  @Column(nullable = false)
  val requestIp: String
) {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private var id: String? = null

  @Column(nullable = false)
  val creation: ZonedDateTime = ZonedDateTime.now()

  fun getCreditCardNumber(): String = this.creditCard.number

  override fun toString(): String =
    "TravelNotice(" +
    "destination='$destination', " +
    "endDate=$endDate, " +
    "requestUserAgent='$requestUserAgent', " +
    "requestIp='$requestIp', " +
    "creation='$creation'" +
    ")"
}

class TravelNoticeRequest(
  @field:NotBlank
  val destination: String,

  @field:Future
  val endDate: LocalDate
) {

  fun toTravelNotice(
    creditCard: CreditCard, requestUserAgent: String, requestIp: String
  ) =
    TravelNotice(
      creditCard = creditCard,
      destination = this.destination,
      endDate = this.endDate,
      requestUserAgent = requestUserAgent,
      requestIp = requestIp
    )

}

class TravelNoticeDetail(travelNotice: TravelNotice) {
  val destination: String = travelNotice.destination
  val endDate: LocalDate = travelNotice.endDate
  val requestUserAgent: String = travelNotice.requestUserAgent
  val requestIp: String = travelNotice.requestIp
  val creation: ZonedDateTime = travelNotice.creation
}
