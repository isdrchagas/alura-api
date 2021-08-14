package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewEnrollmentRequest {

    @NotBlank
    @Size(max=20)
    @JsonProperty
    private String username;

    protected NewEnrollmentRequest() { }

    public NewEnrollmentRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
