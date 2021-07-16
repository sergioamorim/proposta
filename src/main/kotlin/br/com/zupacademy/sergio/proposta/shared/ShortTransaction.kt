package br.com.zupacademy.sergio.proposta.shared

import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Component
class ShortTransaction<T> {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  @Transactional
  fun update(any: T): T {
    this.entityManager.merge(any)
    return any
  }

  @Transactional
  fun insert(any: T): T {
    this.entityManager.persist(any)
    return any
  }
}
