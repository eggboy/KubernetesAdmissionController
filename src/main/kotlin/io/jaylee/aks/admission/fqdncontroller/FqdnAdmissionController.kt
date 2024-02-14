package io.jaylee.aks.admission.fqdncontroller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FqdnAdmissionController

fun main(args: Array<String>) {
	runApplication<FqdnAdmissionController>(*args)
}
