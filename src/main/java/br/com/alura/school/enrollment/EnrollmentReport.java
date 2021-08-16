package br.com.alura.school.enrollment;

public class EnrollmentReport {

    private String email;
    private Long enrollmentsQuantity;

    public EnrollmentReport(String email, Long enrollmentsQuantity) {
        this.email = email;
        this.enrollmentsQuantity = enrollmentsQuantity;
    }

    public EnrollmentReportResponse toResponse() {
        return new EnrollmentReportResponse(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getEnrollmentsQuantity() {
        return enrollmentsQuantity;
    }

    public void setEnrollmentsQuantity(Long enrollmentsQuantity) {
        this.enrollmentsQuantity = enrollmentsQuantity;
    }
}
