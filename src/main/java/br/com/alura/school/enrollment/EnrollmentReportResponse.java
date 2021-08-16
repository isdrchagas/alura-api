package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrollmentReportResponse {

    @JsonProperty("email")
    private String email;
    @JsonProperty("quantidade_matriculas")
    private Long enrollmentsQuantity;

    public EnrollmentReportResponse(EnrollmentReport enrollmentReport) {
        this.email = enrollmentReport.getEmail();
        this.enrollmentsQuantity = enrollmentReport.getEnrollmentsQuantity();
    }
}
