package com.astronomy;

public class AstronomicalConstants {

    // Earth Mass, Sun Mass
    public static final double EarthMass, SunMass;

    // Astronomical Unit of Distance (in Metres), Radii of Sun and the Earth
    public static final double AU, SunRadius, EarthRadius;
    // Space Size in our Simulations
    public static final double SpaceSize;
    // Universal Gravitational Constant
    public static final double G;
    // Mass Reduction,Mass Reduction Factor for simulations
    public static final double MassFactor, LengthFactor;

    static {
        EarthMass = 5.972 * 1e24; // kg
        SunMass = 1.989 * 1e30; // kg
        AU = 1.495978707 * 1e11; // m
        SpaceSize = 1e20; // m
        G = 6.6743 * 1e-11; // SI Units
        MassFactor = 1e24;
        LengthFactor = 1e11;

        SunRadius = 6.957*1e8; // m
        EarthRadius = 6.3781*1e6; // m
    }
}
