package io.jaylee.aks.admission.fqdncontroller.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import io.jaylee.aks.admission.fqdncontroller.dto.AdmissionReviewResponse
import io.jaylee.aks.admission.fqdncontroller.service.AdmissionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AdmissionReviewController(private val admissionService: AdmissionService) {

    @PostMapping(path = ["/mutate"])
    fun processAdmissionReviewRequest(@RequestBody request: ObjectNode): AdmissionReviewResponse =
        admissionService.processAdmission(request)

}