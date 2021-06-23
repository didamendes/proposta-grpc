package br.com.zup.biometria

import br.com.zup.cartao.model.Cartao
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
class Biometria(val biometria: ByteArray,
                @ManyToOne
                @JoinColumn(name = "cartao_id", nullable = false)
                val cartao: Cartao) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    val dataCriacao: OffsetDateTime? = null
}