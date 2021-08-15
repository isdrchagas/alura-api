package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourseAndUser(Course course, User username);

    @Query("SELECT new br.com.alura.school.enrollment.EnrollmentReport(u.email, COUNT(u.id) AS countEnroll) FROM Enrollment e INNER JOIN e.user u GROUP BY u.email ORDER BY countEnroll DESC")
    List<EnrollmentReport> countUserEnrollments();
}
