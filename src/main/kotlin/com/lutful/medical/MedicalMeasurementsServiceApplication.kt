package com.lutful.medical

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MedicalMeasurementsServiceApplication

fun main(args: Array<String>) {
	runApplication<MedicalMeasurementsServiceApplication>(*args)
}
