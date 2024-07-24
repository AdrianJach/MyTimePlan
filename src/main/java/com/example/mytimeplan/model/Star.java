package com.example.mytimeplan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Objects;

/**
 * Represents a star with its properties.
 * This entity is used to store information about stars in the database.
 */
@Entity
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @PositiveOrZero(message = "Distance must be non-negative")
    private long distance;

    /**
     * Default constructor for JPA.
     */
    public Star() {
    }

    /**
     * Constructor with name and distance.
     *
     * @param name     The name of the star.
     * @param distance The distance of the star from the sun in light-years.
     */
    public Star(String name, long distance) {
        this.name = name;
        this.distance = distance;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Star star)) return false;
        return Objects.equals(getName(), star.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}