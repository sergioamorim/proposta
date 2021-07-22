package br.com.zupacademy.sergio.proposal.model

import org.hibernate.annotations.GenericGenerator
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class Block(
  @OneToOne(optional = false)
  private val creditCard: CreditCard,

  @Column(nullable = false)
  val requestIp: String,

  @Column(nullable = false)
  val requestUserAgent: String
) {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private var id: String? = null

  @Column(nullable = false)
  val creation: ZonedDateTime = ZonedDateTime.now()

  override fun toString(): String =
    "Block(" +
    "requestIp='$requestIp', " +
    "requestUserAgent='$requestUserAgent', " +
    "creation=$creation" +
    ")"
}

class BlockDetail(block: Block) {
  val requestIp: String = block.requestIp
  val requestUserAgent: String = block.requestUserAgent
  val creation: ZonedDateTime = block.creation
}
