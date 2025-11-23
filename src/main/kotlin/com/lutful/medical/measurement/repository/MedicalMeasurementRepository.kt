package com.lutful.medical.measurement.repository

import com.lutful.medical.measurement.model.entity.MedicalMeasurement
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface MedicalMeasurementRepository : JpaRepository<MedicalMeasurement, Long> {

    fun findByPatientIdOrderByMeasuredAtDesc(
        patientId: String
    ): List<MedicalMeasurement>

    fun findByPatientIdAndMeasuredAtBetweenOrderByMeasuredAtDesc(
        patientId: String,
        from: Instant,
        to: Instant
    ): List<MedicalMeasurement>

    fun findByMeasuredAtBetweenOrderByMeasuredAtDesc(
        from: Instant,
        to: Instant
    ): List<MedicalMeasurement>

    fun findAllByOrderByMeasuredAtDesc(): List<MedicalMeasurement>
}
