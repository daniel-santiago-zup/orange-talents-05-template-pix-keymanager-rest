package br.com.zup.edu.models.chavePix

import br.com.zup.edu.proto.DeletaChavePixRequest
import br.com.zup.edu.proto.KeyManagerServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/key-manager/pix")
class ChavePixController(
    @Inject val keyManagerGrpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {

    @Post
    fun registraChavePix(@Valid @Body request: ChavePixRequest): HttpResponse<ChavePixResponse> {
        val grpcResponse = keyManagerGrpcClient.registraChave(request.converte())

        val uri = UriBuilder.of("/api/v1/key-manager/pix/{pixId}").expand(mutableMapOf("id" to grpcResponse.pixId))

        return HttpResponse.created(uri)
    }

    @Delete("/{pixId}")
    fun deletaChavePix(@PathVariable pixId: String): HttpResponse<Any> {

        val grpcRequest = DeletaChavePixRequest.newBuilder()
            .setPixId(pixId)
            .build()

        keyManagerGrpcClient.deletaChave(grpcRequest)

        return HttpResponse.accepted()

    }

}