package com.lutful.medical.measurement.controller

import com.lutful.medical.measurement.model.dto.CreateMeasurementRequest
import com.lutful.medical.measurement.model.dto.MeasurementResponse
import com.lutful.medical.measurement.service.MeasurementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/v1/measurements")
class MeasurementController(
    private val measurementService: MeasurementService
) {

    private val logger = LoggerFactory.getLogger(MeasurementController::class.java)

    @Operation(
        summary = "Create a new medical measurement",
        description = "Stores systolic, diastolic, and heart rate values for a patient."
    )
    @ApiResponse(responseCode = "201", description = "Measurement created successfully")
    @PostMapping
    fun createMeasurement(
        @Valid @RequestBody request: CreateMeasurementRequest
    ): ResponseEntity<MeasurementResponse> {

        logger.info("Received request to create measurement")

        val created = measurementService.createMeasurement(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @Operation(
        summary = "Search medical measurements",
        description = """
            Supports multiple search combinations:
            - No filters → return all measurements
            - Only patientId → return all for that patient
            - patientId + date range → return filtered results
        """
    )
    @GetMapping
    fun getMeasurements(
        @RequestParam(required = false) patientId: String?,
        @RequestParam(required = false) from: Instant?,
        @RequestParam(required = false) to: Instant?
    ): ResponseEntity<List<MeasurementResponse>> {

        logger.info(
            "Searching measurements with patientId={}, from={}, to={}",
            patientId, from, to
        )

        val results = measurementService.searchMeasurements(patientId, from, to)
        return ResponseEntity.ok(results)
    }
}
