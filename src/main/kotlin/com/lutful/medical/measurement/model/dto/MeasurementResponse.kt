package com.lutful.medical.measurement.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

data class MeasurementResponse(
    val id: Long?,

    @field:Schema(description = "Patient Identifier", example = "patient-1")
    val patientId: String,

    @field:Schema(description = "Systolic Blood Pressure (mmHg)", example = "120")
    val systolic: Int,

    @field:Schema(description = "Diastolic Blood Pressure (mmHg)", example = "80")
    val diastolic: Int,

    @field:Schema(description = "Heart Rate (Beats per minute)", example = "72")
    val heartRate: Int,

    val measuredAt: Instant,

    val receivedAt: Instant
)