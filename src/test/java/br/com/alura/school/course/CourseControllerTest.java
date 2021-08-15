package br.com.alura.school.course;

import br.com.alura.school.enrollment.Enrollment;
import br.com.alura.school.enrollment.EnrollmentRepository;
import br.com.alura.school.enrollment.NewEnrollmentRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void should_retrieve_course_by_code() throws Exception {
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));

        mockMvc.perform(get("/courses/java-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("java-1")))
                .andExpect(jsonPath("$.name", is("Java OO")))
                .andExpect(jsonPath("$.shortDescription", is("Java and O...")));
    }

    @Test
    void should_retrieve_all_courses() throws Exception {
        courseRepository.save(new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC."));
        courseRepository.save(new Course("spring-2", "Spring Boot", "Spring Boot"));

        mockMvc.perform(get("/courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].code", is("spring-1")))
                .andExpect(jsonPath("$[0].name", is("Spring Basics")))
                .andExpect(jsonPath("$[0].shortDescription", is("Spring Cor...")))
                .andExpect(jsonPath("$[1].code", is("spring-2")))
                .andExpect(jsonPath("$[1].name", is("Spring Boot")))
                .andExpect(jsonPath("$[1].shortDescription", is("Spring Boot")));
    }

    @Test
    void should_add_new_course() throws Exception {
        NewCourseRequest newCourseRequest = new NewCourseRequest("java-2", "Java Collections", "Java Collections: Lists, Sets, Maps and more.");

        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newCourseRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-2"));
    }

    @Test
    void not_found_when_user_does_not_exist_to_enroll() throws Exception {
        String courseCode = "java-3";
        NewEnrollmentRequest newEnrollment = new NewEnrollmentRequest("isa");

        mockMvc.perform(post(format("/courses/%s/enroll", courseCode))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollment)))
                .andExpect(status().isNotFound());
    }

    @Test
    void not_found_when_course_does_not_exist_to_enroll() throws Exception {
        userRepository.save(new User("isa", "isa@email.com"));

        String courseCode = "java-3";
        NewEnrollmentRequest newEnrollment = new NewEnrollmentRequest("isa");

        mockMvc.perform(post(format("/courses/%s/enroll", courseCode))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollment)))
                .andExpect(status().isNotFound());
    }

    @Test
    void bad_request_when_enrollment_course_already_exists_to_user() throws Exception {
        String username = "isa";
        String courseCode = "spring-2";

        User user = userRepository.save(new User(username, "isa@email.com"));
        Course course = courseRepository.save(new Course(courseCode, "Spring Boot", "Spring Boot"));
        enrollmentRepository.save(new Enrollment(course, user));

        NewEnrollmentRequest newEnrollment = new NewEnrollmentRequest(username);

        mockMvc.perform(post(format("/courses/%s/enroll", courseCode))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollment)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_new_enrollment() throws Exception {
        String username = "isa";
        String courseCode = "spring-2";

        User user = userRepository.save(new User(username, "isa@email.com"));
        Course course = courseRepository.save(new Course(courseCode, "Spring Boot", "Spring Boot"));

        NewEnrollmentRequest newEnrollment = new NewEnrollmentRequest(username);

        mockMvc.perform(post(format("/courses/%s/enroll", courseCode))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollment)))
                .andExpect(status().isCreated());
    }

    @Test
    void no_content_when_users_do_not_have_enrollments() throws Exception {

        mockMvc.perform(get("/courses/enroll/report")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_retrieve_all_enrollments_grouped_by_email() throws Exception {
        User user = userRepository.save(new User("isa", "isa@email.com"));
        Course course = courseRepository.save(new Course("spring-2", "Spring Boot", "Spring Boot"));
        enrollmentRepository.save(new Enrollment(course, user));

        mockMvc.perform(get("/courses/enroll/report")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].email", is("isa@email.com")))
                .andExpect(jsonPath("$[0].quantidade_matriculas", is(1)));
    }

    @Test
    void should_retrieve_all_enrollments_grouped_by_email_and_order_by_desc() throws Exception {
        User userIsa = userRepository.save(new User("isa", "isa@email.com"));
        User userAna = userRepository.save(new User("ana", "ana@email.com"));
        User userCaio = userRepository.save(new User("caio", "caio@email.com"));
        User userNico = userRepository.save(new User("nico", "nico@email.com"));
        User userRodrigo = userRepository.save(new User("rodrigo", "rodrigo@email.com"));

        Course course1 = courseRepository.save(new Course("spring-1", "Spring Boot 1", "Spring Boot 1"));
        Course course2 = courseRepository.save(new Course("spring-2", "Spring Boot 2", "Spring Boot 2"));
        Course course3 = courseRepository.save(new Course("spring-3", "Spring Boot 3", "Spring Boot 3"));

        enrollmentRepository.save(new Enrollment(course1, userIsa));
        enrollmentRepository.save(new Enrollment(course2, userIsa));
        enrollmentRepository.save(new Enrollment(course3, userCaio));
        enrollmentRepository.save(new Enrollment(course1, userCaio));
        enrollmentRepository.save(new Enrollment(course2, userCaio));
        enrollmentRepository.save(new Enrollment(course1, userNico));

        mockMvc.perform(get("/courses/enroll/report")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].email", is("caio@email.com")))
                .andExpect(jsonPath("$[0].quantidade_matriculas", is(3)))
                .andExpect(jsonPath("$[1].email", is("isa@email.com")))
                .andExpect(jsonPath("$[1].quantidade_matriculas", is(2)))
                .andExpect(jsonPath("$[2].email", is("nico@email.com")))
                .andExpect(jsonPath("$[2].quantidade_matriculas", is(1)));
    }
}