package br.com.zup.edu.annotations

import br.com.zup.edu.models.chavePix.ChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ValidPixKeyValidator::class])
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValidPixKey(
    val message: String = "Formato de chave pix inválido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidPixKeyValidator() : ConstraintValidator<ValidPixKey, ChavePixRequest> {

    override fun isValid(
        value: ChavePixRequest,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: io.micronaut.validation.validator.constraints.ConstraintValidatorContext
    ): Boolean {
        return value.tipoChave.valida(value.valorChave)
    }
}