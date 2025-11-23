package com.lutful.medical.measurement.model.dto

import jakarta.validation.constraints.*
import java.time.Instant

data class CreateMeasurementRequest(
    @field:NotBlank
    val patientId: String,

    @field:Min(50) @field:Max(250)
    val systolic: Int,

    @field:Min(30) @field:Max(150)
    val diastolic: Int,

    @field:Min(30) @field:Max(220)
    val heartRate: Int,

    val measuredAt: Instant = Instant.now()
)
