package com.astronomy;

import PhysicsSim.Vector;

public class Planet extends Body {

    // Construct a planet of given mass
    public Planet(double mass, String name) {
        this.mass = mass / AstronomicalConstants.MassFactor;
        this.name = name;
    }

    // Constructor for a planet with given position and mass in terms of Earth-Masses using a boolean
    public Planet(double factor, double mass_of_factored_planet, String name
    ) {
        this.mass = factor * mass_of_factored_planet / AstronomicalConstants.MassFactor;
        this.name = name;
    }

    // Set the parameters for this planet
    public Planet setVelocity(Vector v) {
        this.velocity = v;
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
        return this;
    }

    public Planet setRadius(double factor, String planet) {
        if (planet.equals("Sun")) {
            this.radius = factor * AstronomicalConstants.SunRadius / AstronomicalConstants.LengthFactor;
        } else {
            this.radius = factor * AstronomicalConstants.EarthRadius / AstronomicalConstants.LengthFactor;
        }
        return this;
    }

    @Override
    public String toString() {
        return name + "\ns:" + position +
                "\nv:" + velocity +
                "\na:" + acceleration;
    }

}
