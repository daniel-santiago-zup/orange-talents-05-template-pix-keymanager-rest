package br.com.zup.edu.models.chavePix

import br.com.zup.edu.annotations.ValidPixKey
import br.com.zup.edu.annotations.ValidUUID
import br.com.zup.edu.proto.CriaChavePixRequest
import br.com.zup.edu.proto.TipoConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
@ValidPixKey
data class ChavePixRequest(
    @field:NotBlank @field:ValidUUID val idCliente: String,
    @field:NotNull val tipoChave: TipoChavePix,
    val valorChave: String?,
    @field:NotNull val tipoConta: TipoConta
) {
    fun converte(): CriaChavePixRequest {
        return CriaChavePixRequest.newBuilder()
            .setIdCliente(idCliente)
            .setTipoChave(tipoChave.toTipoChavePixProtobuff())
            .setValorChave(valorChave ?: "")
            .setTipoConta(tipoConta)
            .build()
    }
}
