package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrollmentReportResponse {

    @JsonProperty("email")
    private String email;
    @JsonProperty("quantidade_matriculas")
    private Long enrollmentsQuantity;

    public EnrollmentReportResponse(String email, Long enrollmentsQuantity) {
        this.email = email;
        this.enrollmentsQuantity = enrollmentsQuantity;
    }
}
