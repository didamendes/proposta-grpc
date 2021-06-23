package br.com.zup.cartao.bloqueio

import br.com.zup.cartao.model.Cartao
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
class Bloqueio(@ManyToOne
               @JoinColumn(name = "cartao_id", nullable = false)
               val cartao: Cartao
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    val dataBloqueio: OffsetDateTime? = null

}