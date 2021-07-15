package br.com.zup.edu.models.chavePix

import br.com.zup.edu.external.KeyManagerGrpcClientFactory
import br.com.zup.edu.proto.*
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ChavePixControllerTest {

    @Inject
    lateinit var grpcClientMock: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve cadastrar uma chave com sucesso`() {
        val chavePixRequest = ChavePixRequest(
            idCliente = "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            tipoChave = TipoChavePix.CPF,
            valorChave = "02210597684",
            tipoConta = TipoConta.CONTA_POUPANCA
        )

        Mockito.`when`(grpcClientMock.registraChave(chavePixRequest.converte())).thenReturn(
            CriaChavePixResponse.newBuilder()
                .setPixId("20d4a159-d893-4890-91d0-bd139e7e174b")
                .build()
        )

        val request = HttpRequest.POST("/api/v1/key-manager/pix", chavePixRequest)

        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)

    }

    @Test
    internal fun `deleta uma chave pix com sucesso`() {

        val grpcRequest = DeletaChavePixRequest.newBuilder()
            .setPixId("20d4a159-d893-4890-91d0-bd139e7e174b")
            .build()

        Mockito.`when`(grpcClientMock.deletaChave(grpcRequest)).thenReturn(null)

        val request = HttpRequest.DELETE<Any>("/api/v1/key-manager/pix/20d4a159-d893-4890-91d0-bd139e7e174b")

        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.ACCEPTED, response.status)

    }

    @Test
    internal fun `deve obter os detalhes de uma chave pix com sucesso`() {

        val grpcRequest = DetalhaChavePixExternalRequest.newBuilder()
            .setPixId("be35e15e-86d1-4a35-9e47-fcbaa3938bbd")
            .setIdCliente("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .build()

        val grpcResponse = DetalhaChavePixExternalResponse.newBuilder()
            .setPixId("be35e15e-86d1-4a35-9e47-fcbaa3938bbd")
            .setIdCliente("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .setTipoChave(br.com.zup.edu.proto.TipoChavePix.RANDOM_KEY)
            .setValorChave("7719525a-6dc0-4664-a1fa-b31fc11286c1")
            .setCriadoEm("2021-07-13T19:08:59.567765")
            .build()

        Mockito.`when`(grpcClientMock.detalhaChaveExternal(grpcRequest)).thenReturn(grpcResponse)

        val request =
            HttpRequest.GET<ChavePixResponse>("/api/v1/key-manager/pix/0d1bb194-3c52-4e67-8c35-a93c0af9284f/be35e15e-86d1-4a35-9e47-fcbaa3938bbd")

        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    internal fun `deve obter uma lista de chaves pix com sucesso`() {
        val grpcRequest = ListaChavesRequest.newBuilder()
            .setIdCliente("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .build()

        val grpcResponse = ListaChavesResponse.newBuilder()
            .addAllChaves(
                mutableListOf(
                    geraChavePixDetailAleatorio(),
                    geraChavePixDetailAleatorio(),
                    geraChavePixDetailAleatorio(),
                )
            )
            .build()

        Mockito.`when`(grpcClientMock.listaChaves(grpcRequest)).thenReturn(grpcResponse)

        val request =
            HttpRequest.GET<List<ChavePixResponse>>("/api/v1/key-manager/pix/0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        val response = client.toBlocking().exchange(request, List::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(3, response.body().size)
    }

    // Não estou conseguindo testar esse caso porque a excessão não deixa esse cliente funcionar direito. Preciso perguntar como resolver isso
//    @Test
//    internal fun `deve falhar ao tentar deletar uma chave pix que nao existe`() {
//        val grpcRequest = DeletaChavePixRequest.newBuilder()
//            .setPixId("20d4a159-d893-4890-91d0-bd139e7e174b")
//            .build()
//
//        Mockito.`when`(grpcClientMock.deletaChave(grpcRequest)).thenThrow(Status.NOT_FOUND.asRuntimeException())
//
//        val request = HttpRequest.DELETE<Any>("/api/v1/key-manager/pix/20d4a159-d893-4890-91d0-bd139e7e174b")
//
//        val response = client.toBlocking().exchange(request, Any::class.java)
//
//        assertEquals(HttpStatus.NOT_FOUND, response.status)
//    }

    @Factory
    @Replaces(factory = KeyManagerGrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerServiceGrpc.KeyManagerServiceBlockingStub::class.java)
    }

    fun geraChavePixDetailAleatorio(): ListaChavesResponse.ListaChavesDetails {
        return ListaChavesResponse.ListaChavesDetails.newBuilder()
            .setPixId("be35e15e-86d1-4a35-9e47-fcbaa3938bbd")
            .setIdCliente("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .setTipoChave(br.com.zup.edu.proto.TipoChavePix.RANDOM_KEY)
            .setValorChave(UUID.randomUUID().toString())
            .setCriadoEm("2021-07-13T19:08:59.567765")
            .build()
    }
}