package br.com.zup.edu.models.chavePix

import br.com.zup.edu.extensions.toChavePixResponse
import br.com.zup.edu.proto.DeletaChavePixRequest
import br.com.zup.edu.proto.DetalhaChavePixExternalRequest
import br.com.zup.edu.proto.KeyManagerServiceGrpc
import br.com.zup.edu.proto.ListaChavesRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/key-manager/pix")
class ChavePixController(
    @Inject val keyManagerGrpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {

    @Get("/{idCliente}")
    fun listaChavesPix(@PathVariable("idCliente") idCliente: UUID): HttpResponse<List<ChavePixResponse>> {

        val grpcRequest = ListaChavesRequest.newBuilder()
            .setIdCliente(idCliente.toString())
            .build()

        val grpcResponse = keyManagerGrpcClient.listaChaves(grpcRequest)

        val listaChavesPix = grpcResponse.chavesList.map { it.toChavePixResponse() }

        return HttpResponse.ok(listaChavesPix)

    }

    @Get("/{idCliente}/{pixId}")
    fun detalhaChavePix(
        @PathVariable("idCliente") idCliente: UUID,
        @PathVariable("pixId") pixId: UUID
    ): HttpResponse<ChavePixResponse> {

        val grpcRequest = DetalhaChavePixExternalRequest.newBuilder()
            .setIdCliente(idCliente.toString())
            .setPixId(pixId.toString())
            .build()

        val grpcResponse = keyManagerGrpcClient.detalhaChaveExternal(grpcRequest)

        return HttpResponse.ok(grpcResponse.toChavePixResponse())
    }

    @Post
    fun registraChavePix(@Valid @Body request: ChavePixRequest): HttpResponse<ChavePixResponse> {
        val grpcResponse = keyManagerGrpcClient.registraChave(request.converte())

        val uri = UriBuilder.of("/api/v1/key-manager/pix/{pixId}").expand(mutableMapOf("id" to grpcResponse.pixId))

        return HttpResponse.created(uri)
    }

    @Delete("/{pixId}")
    fun deletaChavePix(@PathVariable pixId: UUID): HttpResponse<Any> {

        val grpcRequest = DeletaChavePixRequest.newBuilder()
            .setPixId(pixId.toString())
            .build()

        keyManagerGrpcClient.deletaChave(grpcRequest)

        return HttpResponse.accepted()

    }

}