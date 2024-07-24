package com.example.mytimeplan;

import com.example.mytimeplan.model.Star;
import com.example.mytimeplan.repository.StarRepository;
import com.example.mytimeplan.service.StarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the StarService class.
 * These tests cover the main functionalities of the StarService,
 * including finding closest stars, getting stars by distances,
 * and handling unique stars.
 */
@ExtendWith(MockitoExtension.class)
class StarServiceTest {

    @Mock
    private StarRepository starRepository;

    @InjectMocks
    private StarService starService;

    /**
     * Test for finding the closest stars.
     * This test verifies that the method returns the correct number of stars,
     * sorted by their distance from the sun.
     */
    @Test
    void findClosestStarsTest() {
        List<Star> stars = Arrays.asList(
                new Star("Alpha Centauri", 4),
                new Star("Barnard's Star", 6),
                new Star("Wolf 359", 8),
                new Star("Sirius", 9)
        );
        List<Star> closestStars = starService.findClosestStars(stars, 2);
        assertEquals(2, closestStars.size());
        assertEquals("Alpha Centauri", closestStars.get(0).getName());
        assertEquals("Barnard's Star", closestStars.get(1).getName());
    }

    /**
     * Test for getting the number of stars by distances.
     * This test checks if the method correctly groups stars by their distances
     * and returns the correct count for each distance.
     */
    @Test
    void getNumberOfStarsByDistancesTest() {
        List<Star> stars = Arrays.asList(
                new Star("Alpha Centauri", 4),
                new Star("Barnard's Star", 6),
                new Star("Wolf 359", 8),
                new Star("Sirius", 8)
        );
        Map<Long, Integer> starCountByDistance = starService.getNumberOfStarsByDistances(stars);
        assertEquals(1, starCountByDistance.get(4L));
        assertEquals(1, starCountByDistance.get(6L));
        assertEquals(2, starCountByDistance.get(8L));
    }

    /**
     * Test for getting unique stars.
     * This test verifies that the method correctly filters out duplicate stars
     * based on their names.
     */
    @Test
    void getUniqueStarsTest() {
        Collection<Star> stars = Arrays.asList(
                new Star("Alpha Centauri", 4),
                new Star("Barnard's Star", 6),
                new Star("Wolf 359", 8),
                new Star("Alpha Centauri", 9)
        );
        Collection<Star> uniqueStars = starService.getUniqueStars(stars);
        assertEquals(3, uniqueStars.size());
        assertTrue(uniqueStars.stream().anyMatch(star -> star.getName().equals("Alpha Centauri")));
        assertTrue(uniqueStars.stream().anyMatch(star -> star.getName().equals("Barnard's Star")));
        assertTrue(uniqueStars.stream().anyMatch(star -> star.getName().equals("Wolf 359")));
    }

    /**
     * Test for getting a star by ID.
     * This test verifies that the method correctly retrieves a star by its ID
     * and throws an exception when the star is not found.
     */
    @Test
    void getStarByIdTest() {
        Star expectedStar = new Star("Test Star", 10);
        when(starRepository.findById(1L)).thenReturn(Optional.of(expectedStar));
        when(starRepository.findById(2L)).thenReturn(Optional.empty());

        Star result = starService.getStarById(1L);
        assertEquals(expectedStar, result);

        assertThrows(NoSuchElementException.class, () -> starService.getStarById(2L));
    }

    /**
     * Test for adding a new star.
     * This test verifies that the method correctly adds a new star to the repository.
     */
    @Test
    void addStarTest() {
        Star newStar = new Star("New Star", 15);
        when(starRepository.save(any(Star.class))).thenReturn(newStar);

        Star result = starService.addStar(newStar);
        assertEquals(newStar, result);
        verify(starRepository, times(1)).save(newStar);
    }

    /**
     * Test for updating an existing star.
     * This test verifies that the method correctly updates an existing star in the repository.
     */
    @Test
    void updateStarTest() {
        Long starId = 1L;
        Star existingStar = new Star("Existing Star", 20);
        Star updatedStar = new Star("Updated Star", 25);

        when(starRepository.findById(starId)).thenReturn(Optional.of(existingStar));
        when(starRepository.save(any(Star.class))).thenReturn(updatedStar);

        Star result = starService.updateStar(starId, updatedStar);
        assertEquals(updatedStar.getName(), result.getName());
        assertEquals(updatedStar.getDistance(), result.getDistance());
        verify(starRepository, times(1)).findById(starId);
        verify(starRepository, times(1)).save(any(Star.class));
    }

    /**
     * Test for deleting a star.
     * This test verifies that the method correctly calls the delete method of the repository.
     */
    @Test
    void deleteStarTest() {
        Long starId = 1L;
        starService.deleteStar(starId);
        verify(starRepository, times(1)).deleteById(starId);
    }
}