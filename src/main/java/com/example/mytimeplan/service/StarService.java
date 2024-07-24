package com.example.mytimeplan.service;

import com.example.mytimeplan.model.Star;
import com.example.mytimeplan.repository.StarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for handling operations related to stars.
 * This class provides methods for manipulating and retrieving star data.
 */
@Service
public class StarService {

    private static final Logger logger = LoggerFactory.getLogger(StarService.class);
    private final StarRepository starRepository;

    /**
     * Constructs a new StarService with the given StarRepository.
     *
     * @param starRepository The repository for star data.
     */
    @Autowired
    public StarService(StarRepository starRepository) {
        this.starRepository = starRepository;
    }

    /**
     * Finds the closest stars to the sun.
     *
     * @param stars The list of stars to search.
     * @param size  The number of closest stars to return.
     * @return A list of the closest stars, sorted by distance.
     * @throws IllegalArgumentException if the star list is null or empty.
     */
    @Cacheable("closestStars")
    public List<Star> findClosestStars(List<Star> stars, int size) {
        if (stars == null || stars.isEmpty()) {
            throw new IllegalArgumentException("Star list cannot be null or empty");
        }
        logger.info("Finding the closest {} stars from a list of {} stars", size, stars.size());
        return stars.stream()
                .sorted(Comparator.comparingLong(Star::getDistance))
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * Gets the number of stars by their distances.
     *
     * @param stars The list of stars to analyze.
     * @return A map where the key is the distance and the value is the number of stars at that distance.
     * @throws IllegalArgumentException if the star list is null or empty.
     */
    @Cacheable("starsByDistances")
    public Map<Long, Integer> getNumberOfStarsByDistances(List<Star> stars) {
        if (stars == null || stars.isEmpty()) {
            throw new IllegalArgumentException("Star list cannot be null or empty");
        }
        logger.info("Calculating the number of stars by their distances");
        return stars.stream()
                .collect(Collectors.groupingBy(Star::getDistance, TreeMap::new, Collectors.reducing(0, e -> 1, Integer::sum)));
    }

    /**
     * Gets a collection of unique stars based on their names.
     *
     * @param stars The collection of stars to analyze.
     * @return A collection of unique stars.
     * @throws IllegalArgumentException if the star collection is null or empty.
     */
    @Cacheable("uniqueStars")
    public Collection<Star> getUniqueStars(Collection<Star> stars) {
        if (stars == null || stars.isEmpty()) {
            throw new IllegalArgumentException("Star collection cannot be null or empty");
        }
        logger.info("Getting unique stars from a collection of {} stars", stars.size());
        return new LinkedHashSet<>(stars);
    }

    /**
     * Retrieves a star by its ID.
     *
     * @param id The ID of the star to retrieve.
     * @return The star with the given ID.
     * @throws NoSuchElementException if no star is found with the given ID.
     */
    @Transactional(readOnly = true)
    public Star getStarById(Long id) {
        logger.info("Fetching star with id: {}", id);
        return starRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Star not found with id: {}", id);
                    return new NoSuchElementException("Star not found with id: " + id);
                });
    }

    /**
     * Adds a new star to the database.
     *
     * @param star The star to add.
     * @return The saved star with its generated ID.
     */
    @Transactional
    public Star addStar(Star star) {
        if (star.getName().length() < 3) {
            throw new IllegalArgumentException("Star name must be at least 3 characters long");
        }
        logger.info("Adding a new star with name: {}", star.getName());
        return starRepository.save(star);
    }

    /**
     * Updates an existing star in the database.
     *
     * @param id   The ID of the star to update.
     * @param star The updated star data.
     * @return The updated star.
     * @throws NoSuchElementException if no star is found with the given ID.
     */
    @Transactional
    public Star updateStar(Long id, Star star) {
        Star existingStar = getStarById(id);
        existingStar.setName(star.getName());
        existingStar.setDistance(star.getDistance());
        logger.info("Updating star with id: {}", id);
        return starRepository.save(existingStar);
    }

    /**
     * Deletes a star from the database.
     *
     * @param id The ID of the star to delete.
     */
    @Transactional
    public void deleteStar(Long id) {
        logger.info("Deleting star with id: {}", id);
        starRepository.deleteById(id);
    }
}