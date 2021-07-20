package br.com.zupacademy.sergio.proposta.model.external

import br.com.zupacademy.sergio.proposta.model.Proposal
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.time.LocalDateTime

class CreditCardRequest(proposal: Proposal) {
  val documento: String = proposal.nationalRegistryId
  val nome: String = proposal.name
  val idProposta: String? = proposal.id
}

class CreditCardResponse(

  /* the validation annotation for the id should be @field:CreditCardNumber, but
   * the external api is returning invalid credit card numbers that must be
   * accepted on the scope of this project */
  @field:Length(min = 19, max = 19)
  val id: String,

  private val emitidoEm: String,
  private val titular: String,
  private val bloqueios: Array<Block>,
  private val avisos: Array<Notice>,
  private val carteiras: Array<Wallet>,
  private val parcelas: Array<Installment>,
  private val limite: BigDecimal,
  private val renegociacao: Renegotiation?,
  private val vencimento: Expiration,
  private val idProposta: String
) {
  class Block(
    private val id: String,
    private val bloqueadoEm: String,
    private val sistemaResponsavel: String,
    private val ativo: Boolean
  )

  class Notice(
    private val validoAte: LocalDateTime,
    private val destino: String
  )

  class Wallet(
    private val id: String,
    private val email: String,
    private val associadaEm: LocalDateTime,
    private val emissor: String
  )

  class Installment(
    private val id: String,
    private val quantidade: Int,
    private val valor: BigDecimal
  )

  class Renegotiation(
    private val id: String,
    private val quantidade: Int,
    private val valor: BigDecimal,
    private val dataDeCriacao: LocalDateTime
  )

  class Expiration(
    private val id: String,
    private val dia: Int,
    private val dataDeCriacao: LocalDateTime
  )
}
