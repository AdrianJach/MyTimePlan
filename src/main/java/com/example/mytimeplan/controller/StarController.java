package com.example.mytimeplan.controller;

import com.example.mytimeplan.model.Star;
import com.example.mytimeplan.service.StarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing star-related operations.
 * This controller provides endpoints for CRUD operations on stars and other star-related functionalities.
 */
@RestController
@RequestMapping("/api/stars")
public class StarController {

    private final StarService starService;

    /**
     * Constructs a new StarController with the given StarService.
     *
     * @param starService The service for star-related operations.
     */
    @Autowired
    public StarController(StarService starService) {
        this.starService = starService;
    }

    /**
     * Retrieves a star by its ID.
     *
     * @param id The ID of the star to retrieve.
     * @return ResponseEntity containing the star if found, or a not found status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Star> getStarById(@PathVariable Long id) {
        return ResponseEntity.ok(starService.getStarById(id));
    }

    /**
     * Adds a new star.
     *
     * @param star The star to add.
     * @return ResponseEntity containing the added star and a created status.
     */
    @PostMapping
    public ResponseEntity<Star> addStar(@Valid @RequestBody Star star) {
        return new ResponseEntity<>(starService.addStar(star), HttpStatus.CREATED);
    }

    /**
     * Updates an existing star.
     *
     * @param id   The ID of the star to update.
     * @param star The updated star data.
     * @return ResponseEntity containing the updated star.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Star> updateStar(@PathVariable Long id, @Valid @RequestBody Star star) {
        return ResponseEntity.ok(starService.updateStar(id, star));
    }

    /**
     * Deletes a star.
     *
     * @param id The ID of the star to delete.
     * @return ResponseEntity with no content status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStar(@PathVariable Long id) {
        starService.deleteStar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Finds the closest stars from a given list.
     *
     * @param stars The list of stars to search.
     * @param size  The number of closest stars to return.
     * @return ResponseEntity containing the list of closest stars.
     */
    @PostMapping("/closest")
    public ResponseEntity<List<Star>> findClosestStars(@RequestBody List<Star> stars, @RequestParam int size) {
        return ResponseEntity.ok(starService.findClosestStars(stars, size));
    }

    /**
     * Gets the number of stars by their distances.
     *
     * @param stars The list of stars to analyze.
     * @return ResponseEntity containing a map of distances to star counts.
     */
    @PostMapping("/distances")
    public ResponseEntity<Map<Long, Integer>> getNumberOfStarsByDistances(@RequestBody List<Star> stars) {
        return ResponseEntity.ok(starService.getNumberOfStarsByDistances(stars));
    }

    /**
     * Gets a collection of unique stars.
     *
     * @param stars The collection of stars to analyze.
     * @return ResponseEntity containing a collection of unique stars.
     */
    @PostMapping("/unique")
    public ResponseEntity<Collection<Star>> getUniqueStars(@RequestBody Collection<Star> stars) {
        return ResponseEntity.ok(starService.getUniqueStars(stars));
    }
}