package br.com.zup.edu.models.chavePix

import javax.validation.constraints.NotBlank

data class ChavePixResponse(
    @field:NotBlank val pixId: String
)
