package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Enrollment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private User user;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime enrollDate = LocalDateTime.now();

    /**
     * hibernate only
     */
    @Deprecated
    protected Enrollment() {
    }

    public Enrollment(Course course, User user) {
        this.course = course;
        this.user = user;
    }
}
