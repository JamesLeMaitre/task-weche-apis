package dev.perogroupe.wecheapis.utils.validations.validators

import dev.perogroupe.wecheapis.utils.validations.constraints.BirthDateConstraint
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [BirthDateConstraint::class])
annotation class BirthDate(
    val message: String = "l'âge doit être supérieur à 18 ans!",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)
