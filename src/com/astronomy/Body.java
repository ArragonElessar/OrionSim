package com.astronomy;

import PhysicsSim.Constants;
import PhysicsSim.Vector;

public class Body {
    // in future, can add parameters like radius
    public Double radius;
    // All these parameters are define with respect to COM (center of mass) of the body
    public String name;
    public double mass;
    public Vector position; // (x,y,z)
    public Vector velocity; // m/s
    public Vector acceleration; // m/(s^2)



}
