package com.lutful.medical.measurement.service

import com.lutful.medical.measurement.model.dto.CreateMeasurementRequest
import com.lutful.medical.measurement.repository.MedicalMeasurementRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MeasurementServiceIntegrationTest @Autowired constructor(
    private val service: MeasurementService,
    private val repository: MedicalMeasurementRepository
) {

    @Test
    fun `createMeasurement saves to DB and returns response`() {
        // Arrange
        val fixedTime = Instant.parse("2025-11-20T10:00:00Z").truncatedTo(java.time.temporal.ChronoUnit.MILLIS)

        val request = CreateMeasurementRequest(
            patientId = "patient-123",
            systolic = 120,
            diastolic = 80,
            heartRate = 70,
            measuredAt = fixedTime // Pass the truncated time
        )

        // Act
        val response = service.createMeasurement(request)

        // Assert Response (Check the object returned by the service)
        assertNotNull(response.id, "ID should be generated")
        assertEquals("patient-123", response.patientId)
        assertEquals(120, response.systolic)
        assertEquals(80, response.diastolic)
        assertEquals(70, response.heartRate)

        // Assert Persistence (Check the object saved in the database)
        val fromDb = repository.findById(response.id!!).orElseThrow {
            AssertionError("Entity not found in database")
        }

        assertEquals("patient-123", fromDb.patientId)
        assertEquals(120, fromDb.systolic)
        assertEquals(80, fromDb.diastolic)
        assertEquals(70, fromDb.heartRate)
        assertEquals(fixedTime, fromDb.measuredAt)
    }
}