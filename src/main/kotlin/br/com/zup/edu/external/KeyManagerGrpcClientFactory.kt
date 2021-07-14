package br.com.zup.edu.external

import br.com.zup.edu.proto.KeyManagerServiceGrpc
import io.grpc.Channel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcClientFactory {

    @Singleton
    fun geraKeyManagerGrpcClient(@GrpcChannel("key-manager") channel: Channel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub {
        return KeyManagerServiceGrpc.newBlockingStub(channel)
    }

}