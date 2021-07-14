package br.com.zup.edu.extensions

import br.com.zup.edu.models.chavePix.ChavePixResponse
import br.com.zup.edu.proto.CriaChavePixResponse

fun CriaChavePixResponse.toChavePixResponse(): ChavePixResponse {
    return ChavePixResponse(pixId)
}