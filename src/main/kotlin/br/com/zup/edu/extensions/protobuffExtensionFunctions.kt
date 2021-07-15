package br.com.zup.edu.extensions

import br.com.zup.edu.models.chavePix.ChavePixResponse
import br.com.zup.edu.proto.DetalhaChavePixExternalResponse
import br.com.zup.edu.proto.ListaChavesResponse
import java.time.LocalDateTime
import java.util.*


fun DetalhaChavePixExternalResponse.toChavePixResponse(): ChavePixResponse {
    return ChavePixResponse(
        pixId = UUID.fromString(pixId),
        idCliente = UUID.fromString(idCliente),
        tipoChave = tipoChave,
        valorChave = valorChave,
        criadoEm = LocalDateTime.parse(criadoEm)
    )
}

fun ListaChavesResponse.ListaChavesDetails.toChavePixResponse(): ChavePixResponse {
    return ChavePixResponse(
        pixId = UUID.fromString(pixId),
        idCliente = UUID.fromString(idCliente),
        tipoChave = tipoChave,
        valorChave = valorChave,
        criadoEm = LocalDateTime.parse(criadoEm)
    )
}