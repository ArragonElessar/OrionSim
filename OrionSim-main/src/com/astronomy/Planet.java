package com.astronomy;

import PhysicsSim.Vector;
import java.lang.Math;

public class Planet extends Body {

    // Construct a planet of given mass
    public Planet(double mass, String name) {
        this.mass = mass / AstronomicalConstants.MassFactor;
        this.name = name;
        update_pe();
    }

    // Constructor for a planet with given position and mass in terms of Earth-Masses using a boolean
    public Planet(double factor, double mass_of_factored_planet, String name) {
        this.mass = factor * mass_of_factored_planet / AstronomicalConstants.MassFactor;
        this.name = name;
    }

    // Set the parameters for this planet
    public Planet setVelocity(Vector v) {
        this.velocity = v;
        update_ke();
        return this;
    }

    public Planet setPosition(Vector v) {
        this.position = v;
        return this;
    }

    public Planet setAcceleration(Vector v) {
        this.acceleration = v;
        return this;
    }

    public Planet setRadius(double radius) {
        this.radius = radius / AstronomicalConstants.LengthFactor;
        update_pe();
        update_escapev();
        return this;
    }

    public Planet setRadius(double factor, String planet) {
        if (planet.equals("Sun")) {
            this.radius = factor * AstronomicalConstants.SunRadius / AstronomicalConstants.LengthFactor;
        } else {
            this.radius = factor * AstronomicalConstants.EarthRadius / AstronomicalConstants.LengthFactor;
        }
        update_pe();
        return this;
    }

    private void update_ke() {
        kinetic_energy = 0.5 * mass * Vector.findMagnitude(velocity) * Vector.findMagnitude(velocity);
    }

    private void update_pe() {
        if(radius != 0) {
            potential_energy = -0.6 * AstronomicalConstants.G * mass * mass / radius;
        }
    }

    private void update_escapev() {
        if(radius != 0) {
            escape_velocity = Math.sqrt(2 * AstronomicalConstants.G * mass/radius);
        }
    }

    @Override
    public String toString() {
        return name + "\ns:" + position +
                "\nv:" + velocity +
                "\na:" + acceleration;
    }

}
