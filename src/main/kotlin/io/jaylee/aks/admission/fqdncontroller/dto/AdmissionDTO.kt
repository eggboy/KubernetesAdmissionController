package io.jaylee.aks.admission.fqdncontroller.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class AdmissionStatus(
    var code: Int = 0,
    var message: String? = null
)

data class AdmissionReviewData(
    val uid: String? = null,
    val allowed: Boolean = false,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val patchType: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val patch: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val status: AdmissionStatus? = null
)

data class AdmissionReviewResponse(
    val apiVersion: String = "admission.k8s.io/v1",

    val kind: String = "AdmissionReview",

    val response: AdmissionReviewData? = null
)

data class AdmissionReviewException(val code: Int = 0, override val message: String?) : RuntimeException(message) {
    companion object {
        private const val serialVersionUID = 1L
    }
}