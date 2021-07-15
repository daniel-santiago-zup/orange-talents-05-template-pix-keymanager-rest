package br.com.zup.edu.models.chavePix

import br.com.zup.edu.proto.TipoChavePix
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ChavePixResponse(
    @field:NotBlank val pixId: UUID,
    @field:NotBlank val idCliente: UUID,
    @field:NotNull val tipoChave: TipoChavePix,
    @field:NotBlank val valorChave: String,
    @field:NotNull @field:JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") val criadoEm: LocalDateTime
)
