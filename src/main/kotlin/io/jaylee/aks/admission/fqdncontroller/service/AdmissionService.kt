package io.jaylee.aks.admission.fqdncontroller.service

import com.fasterxml.jackson.databind.node.ObjectNode
import io.jaylee.aks.admission.fqdncontroller.dto.AdmissionReviewData
import io.jaylee.aks.admission.fqdncontroller.dto.AdmissionReviewException
import io.jaylee.aks.admission.fqdncontroller.dto.AdmissionReviewResponse
import io.jaylee.aks.admission.fqdncontroller.dto.AdmissionStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class AdmissionService {
    private val log: Logger = LoggerFactory.getLogger(AdmissionService::class.java)

    companion object {
        const val HTTP_INTERNAL_SERVER_ERROR = 500
    }

    fun processAdmission(body: ObjectNode): AdmissionReviewResponse {

        log.debug("processAdmission: body={}", body.toPrettyString())

        return try {
            val admissionReviewData = addAnnotations(body)
            log.info("AdmissionReviewData= {}", admissionReviewData)

            createAdmissionReviewResponse(body, admissionReviewData)
        } catch (ex: AdmissionReviewException) {
            log.error("Error processing AdmissionRequest: code={}, message={}", ex.code, ex.message)

            val rejectedAdmissionReview = createRejectedAdmissionReview(body, ex.code, ex.message)
            createAdmissionReviewResponse(body, rejectedAdmissionReview)
        } catch (ex: Exception) {
            log.error("Unable to process AdmissionRequest: " + ex.message, ex)

            val rejectedAdmissionReview = createRejectedAdmissionReview(body, HTTP_INTERNAL_SERVER_ERROR, ex.message)
            createAdmissionReviewResponse(body, rejectedAdmissionReview)
        }
    }

    private fun createAdmissionReviewResponse(body: ObjectNode, data: AdmissionReviewData): AdmissionReviewResponse {
        return AdmissionReviewResponse(
            apiVersion = body.required("apiVersion").asText(),
            kind = body.required("kind").asText(),
            response = data
        )
    }

    private fun createRejectedAdmissionReview(body: ObjectNode, code: Int, message: String?): AdmissionReviewData {

        val requestId = body.path("request")
            .required("uid")
            .asText()

        val status = AdmissionStatus(
            code = code,
            message = message
        )

        return AdmissionReviewData(
            allowed = false,
            uid = requestId,
            status = status
        )
    }

    private fun addAnnotations(body: ObjectNode): AdmissionReviewData {
        // Create a PATCH object
        val patch: String = """
            [
                {
                    "op": "add",
                    "path": "/metadata/annotations",
                    "value": {"kubernetes.azure.com/set-kube-service-host-fqdn":"true"}
                }
            ]
        """
        return AdmissionReviewData(
            allowed = true,
            uid = body.path("request").required("uid").asText(),
            patch = Base64.getEncoder().encodeToString(patch.toByteArray()),
            patchType = "JSONPatch"
        )
    }
}