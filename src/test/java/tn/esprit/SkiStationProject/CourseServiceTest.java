package tn.esprit.SkiStationProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.repositories.CourseRepository;
import tn.esprit.SkiStationProject.services.CourseServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    @BeforeEach
    public void setUp() {
        courseServices = new CourseServicesImpl(courseRepository);
    }

    @Test
    void testAddCourse() {
        // Given
        Course newCourse = new Course();
        when(courseRepository.save(newCourse)).thenReturn(newCourse);

        // When
        Course addedCourse = courseServices.addCourse(newCourse);

        // Then
        assertNotNull(addedCourse);
        assertEquals(newCourse, addedCourse);

        // Verify the method in courseRepository is called
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testRetrieveCourse() {
        // Given
        Long courseId = 1L;
        Course retrievedCourse = new Course();
        when(courseRepository.findById(courseId)).thenReturn(java.util.Optional.of(retrievedCourse));

        // When
        Course foundCourse = courseServices.retrieveCourse(courseId);

        // Then
        assertNotNull(foundCourse);
        assertEquals(retrievedCourse, foundCourse);

        // Verify the method in courseRepository is called
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testRetrieveCourseCourseNotFound() {
        // Given
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> {
            courseServices.retrieveCourse(courseId);
        });

        // Verify the method in courseRepository is called
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testRetrieveAllCourses() {
        // Given
        List<Course> courseList = new ArrayList<>();
        when(courseRepository.findAll()).thenReturn(courseList);

        // When
        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        // Then
        assertNotNull(retrievedCourses);
        assertEquals(courseList, retrievedCourses);

        // Verify the method in courseRepository is called
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourse() {
        // Given
        Course course = new Course();
        when(courseRepository.save(course)).thenReturn(course);

        // When
        Course updatedCourse = courseServices.updateCourse(course);

        // Then
        assertNotNull(updatedCourse);
        assertEquals(course, updatedCourse);

        // Verify the method in courseRepository is called
        verify(courseRepository, times(1)).save(any(Course.class));
    }
}
