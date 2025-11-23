package com.lutful.medical.measurement.service

import com.lutful.medical.measurement.model.dto.CreateMeasurementRequest
import com.lutful.medical.measurement.model.dto.MeasurementResponse
import java.time.Instant

interface MeasurementService {
    /**
     * Stores a new medical measurement into the system.
     *
     * @param request The incoming measurement payload.
     * @return The stored measurement including database ID and timestamps.
     */
    fun createMeasurement(request: CreateMeasurementRequest): MeasurementResponse

    /**
     * Searches stored measurements based on optional filters.
     *
     * @param patientId Filter by patient identifier (optional).
     * @param from      Starting timestamp (optional).
     * @param to        Ending timestamp (optional).
     * @return List of matching measurements sorted by measured or received time.
     */
    fun searchMeasurements(
        patientId: String?,
        from: Instant?,
        to: Instant?
    ): List<MeasurementResponse>

}
