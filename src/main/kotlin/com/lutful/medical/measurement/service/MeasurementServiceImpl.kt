package com.lutful.medical.measurement.service

import com.lutful.medical.measurement.model.dto.CreateMeasurementRequest
import com.lutful.medical.measurement.model.dto.MeasurementResponse
import com.lutful.medical.measurement.model.entity.MedicalMeasurement
import com.lutful.medical.measurement.repository.MedicalMeasurementRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import org.slf4j.LoggerFactory

@Service
class MeasurementServiceImpl(
    private val repository: MedicalMeasurementRepository
) : MeasurementService {

    private val logger = LoggerFactory.getLogger(MeasurementServiceImpl::class.java)

    override fun createMeasurement(request: CreateMeasurementRequest): MeasurementResponse {
        logger.info("Saving measurement for patient {}", request.patientId)

        val entity = MedicalMeasurement(
            patientId = request.patientId,
            systolic = request.systolic,
            diastolic = request.diastolic,
            heartRate = request.heartRate,
            measuredAt = request.measuredAt,
            receivedAt = Instant.now()
        )

        val saved = repository.save(entity)
        return saved.toResponse()
    }

    override fun searchMeasurements(
        patientId: String?,
        from: Instant?,
        to: Instant?
    ): List<MeasurementResponse> {

        logger.info(
            "Searching measurements with patientId={}, from={}, to={}",
            patientId, from, to
        )

        // Validate range if both provided
        if (from != null && to != null && from.isAfter(to)) {
            throw IllegalArgumentException("Invalid time range: from must be before to")
        }

        val results = when {
            // Case 1: no filters → return everything (sorted)
            patientId == null && from == null && to == null ->
                repository.findAllByOrderByMeasuredAtDesc()

            // Case 2: only patientId
            patientId != null && from == null && to == null ->
                repository.findByPatientIdOrderByMeasuredAtDesc(patientId)

            // Case 3: only date range
            patientId == null && from != null && to != null ->
                repository.findByMeasuredAtBetweenOrderByMeasuredAtDesc(from, to)

            // Case 4: patientId + date range
            patientId != null && from != null && to != null ->
                repository.findByPatientIdAndMeasuredAtBetweenOrderByMeasuredAtDesc(
                    patientId, from, to
                )

            // Case 5: invalid partial state
            else ->
                throw IllegalArgumentException(
                    "Both 'from' and 'to' must be provided for date range filtering"
                )
        }

        return results.map { it.toResponse() }
    }
}

private fun MedicalMeasurement.toResponse() = MeasurementResponse(
    id = id,
    patientId = patientId,
    systolic = systolic,
    diastolic = diastolic,
    heartRate = heartRate,
    measuredAt = measuredAt,
    receivedAt = receivedAt
)
