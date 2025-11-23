package com.lutful.medical.measurement.service

import com.lutful.medical.measurement.model.dto.CreateMeasurementRequest
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.random.Random

@Component
@Profile("!test") // Don't run this during tests!
class MeasurementSimulator(private val measurementService: MeasurementService) {

    private val logger = LoggerFactory.getLogger(MeasurementSimulator::class.java)
    private val patientIds = listOf("patient-1", "patient-2", "patient-3", "patient-4")

    @Scheduled(fixedRateString = "\${app.simulation.rate:5000}")
    fun simulateIncomingSensorData() {
        val patientId = patientIds.random()

        val request = CreateMeasurementRequest(
            patientId = patientId,
            systolic = Random.nextInt(110, 160),
            diastolic = Random.nextInt(70, 100),
            heartRate = Random.nextInt(60, 120),
            measuredAt = Instant.now()
        )

        try {
            measurementService.createMeasurement(request)
            logger.info("DEVICE_PUSH: Patient [$patientId] telemetry received | HR: ${request.heartRate} bpm")
        } catch (e: Exception) {
            logger.error("SIMULATOR: Failed to process data", e)
        }
    }
}