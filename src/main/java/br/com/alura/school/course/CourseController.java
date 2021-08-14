package br.com.alura.school.course;

import br.com.alura.school.enrollment.Enrollment;
import br.com.alura.school.enrollment.EnrollmentRepository;
import br.com.alura.school.enrollment.NewEnrollmentRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    CourseController(CourseRepository courseRepository, UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> listCourseResponse = new ArrayList<>();

        courses.forEach(course -> listCourseResponse.add(new CourseResponse(course)));

        return ResponseEntity.ok(listCourseResponse);
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/courses/{courseCode}/enroll")
    ResponseEntity<Void> newCourseEnroll(@PathVariable String courseCode, @RequestBody @Valid NewEnrollmentRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with username %s not found", request.getUsername())));
        Course course = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", courseCode)));

        Optional<Enrollment> hasEnrollment = enrollmentRepository.findByCourseAndUser(course, user);
        if (hasEnrollment.isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, format("Enrollment with code %s and user %s already exists", courseCode, request.getUsername()));
        }

        enrollmentRepository.save(new Enrollment(course, user));
        //location null just because its not necessary a location for while
        return ResponseEntity.created(null).build();
    }
}
