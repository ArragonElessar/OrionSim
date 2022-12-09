package com.astronomy;

import PhysicsSim.Vector;
import java.util.ArrayList;

public class Field {

    // An arraylist will hold info about all the astronomical bodies in the
    // simulation
    ArrayList<Body> objects;

    // time -> current time in the simulation universe
    long time; // ms
    // after how many ms should the values of parameters be calculated
    // note that this makes our simulation occur in discrete time steps instead of
    // continuous flow of time like real-life.
    // Decrease the time step to get ever more accurate results
    int time_step; // ms

    // Number of ms after which updates should be printed on the console.
    int update_frequency; // ms
    
    Vector com; //position of vector of mass
    long total_mass;
    long total_ke;
    long interactive_pe; //potential energy due to interaction between objects
    long existence_pe; //potential energy of an object solely due to the virtue of its existence
    long total_pe;

    // Constructor initializes all variables
    public Field(int time_step, int update_frequency) {
        objects = new ArrayList<>();
        time = 0; // s
        this.time_step = time_step; // ms
        this.update_frequency = update_frequency; // experimental
    }
    //function to calculate the position of COM during simulation
    private void update_com() {
        Vector temp = new Vector(0d, 0d, 0d);
        ;
        for (Body b : this.objects) {
            temp = Vector.add(temp, Vector.constant_multiply(b.mass, b.position));
        }
        com = Vector.constant_multiply(1 / total_mass, temp);
    }

    private void update_tke() {
        long temp = 0;
        for (Body b : this.objects) {
            temp += b.kinetic_energy;
        }
        total_ke = temp;
    }
    //
    private void update_existencepe() {
        long temp = 0;
        for (Body b : this.objects) {
            temp += b.potential_energy;
        }
        existence_pe = temp;
        update_totalpe();
    }
    //interactive potential energy calculated pair-wise.
    private void update_interactivepe() {
        long temp = 0;
        int n = objects.size();

        for (int i = 0; i < n; i++) {
            Body a = objects.get(i);
            for (int j = i + 1; j < n; j++) {
                Body b = objects.get(j);
                temp += -AstronomicalConstants.G * a.mass * b.mass / findDistance(a, b);
            }
        }
        interactive_pe = temp;
        update_totalpe();
    }
    //self-explanatory
    private void update_totalpe() {
        total_pe = interactive_pe + existence_pe;
    }

    // self-explanatory
    public void add_body(Body b) {
        objects.add(b);
        total_mass += b.mass;
        update_existencepe();
    }

    public void remove_body(Body b) {
        objects.remove(b);
        total_mass -= b.mass;
        update_existencepe();
    }

    // Actual Simulation Function
    // Takes in duration parameter
    public void simulate(int duration) throws InterruptedException {
        // duration in ms, calculate steps
        int steps = duration / time_step + 1;

        // iterate through all steps
        for (int step = 0; step <= steps; step++) {
            // check if it is time to provide an update
            provide_status_updates();

            // update a, v, s, F, of all objects in our simulation
            if (!move_all_objects()) {
                System.out.println("Simulation ended prematurely due to collision");
                break;
            }

            // increment time
            time += time_step;

            // wait for time step, this is our connection to real life time.
            Thread.sleep(time_step);
        }
    }

    // Function to print status of all Bodies in simulation
    private void provide_status_updates() {
        // check if it is the right time to print an update
        if (time % update_frequency == 0) {
            // Print Current time
            System.out.println("\nCurrent Time: " + time + " ms");
            // Loop through all bodies and print status, as per their toString() Method
            for (Body b : this.objects) {
                System.out.println(b);
            }
        }
    }

    // Function to update a, v, s, F, of all objects in our simulation
    private boolean move_all_objects() {
        // this function will update the gravitational accelerations of all objects in
        // our simulations
        assign_gravitational_accelerations();

        // for given acceleration of each body, calculate their new velocity and
        // corresponding position vectors.
        for (Body b : this.objects) {
            b.velocity = Vector.add(b.velocity, Vector.constant_multiply(this.time_step * 1e-3, b.acceleration));
            b.position = Vector.add(b.position, Vector.constant_multiply(this.time_step * 1e-3, b.velocity));
        }

        update_com();
        update_tke();
        update_interactivepe();

        // run the collsion detection too
        for (Body b : this.objects) {
            for (Body c : this.objects) {
                if (b.equals(c)) {
                    continue;
                } else {
                    if (haveCollided(b, c, findDistance(b, c))) {
                        System.out.println("Collision Detected between " + b.name + " and " + c.name);
                        return false;
                    }
                }
            }
        }

        return true; // code exec without any problems
    }

    // For two given bodies, find the magnitude of the distance between their Center
    // of Masses. (COM)
    private Double findDistance(Body a, Body b) {

        // this is the magnitude of the displacement vector i.e mod(A - B), factored for
        // simulation purpose,
        return Vector.findMagnitude(Vector.subtract(a.position, b.position)) * AstronomicalConstants.AU
                / AstronomicalConstants.LengthFactor;
    }

    // This function checks for collisions between two bodies.
    // In an update in the future, try to have a Collision as an Exception
    private boolean haveCollided(Body a, Body b, double distance) {

        // Principle -> if R1 + R2 <= distance between the centers of bodies 1 & 2, that
        // means there is an overlap, i.e Collision
        if (distance <= a.radius + b.radius) {
            a.acceleration = Vector.ZeroVector;
            b.acceleration = Vector.ZeroVector;

            System.out.println("Alert! Collision between " + a.name + " and " + b.name + ".");

            // Return true if there has been a collision
            return true;
        }
        // else return false
        return false;
    }

    // Does as its name suggests
    private double findGravitationalForce(Body a, Body b, double distance) {

        // from the famous Newtonian Formula -> F = G * m1 * m2 / r^2;
        return AstronomicalConstants.G * a.mass * b.mass / (distance * distance);
    }

    // Calculate and Assign Gravitational Accelerations
    private boolean assign_gravitational_accelerations() {
        // Number of Objects in Sim
        int n = objects.size();

        // Every object exerts a gravitational force on every other object
        // A nested for loop goes through each pair of bodies
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // assign to temporary variables for readable program
                Body a = objects.get(i);
                Body b = objects.get(j);

                // Calculate the distance between a and b
                double distance = findDistance(a, b);

                // check if there is a collision between the two objects
                if (haveCollided(a, b, distance)) {
                    return false;
                }

                // Calculate the magnitude of Gravitational Force between A and B
                double force = findGravitationalForce(a, b, distance);

                // Set Accelerations of both items according to Newtons Laws.
                // Add a Detailed Explanation here, for calculation of acceleration.
                a.acceleration = Vector.add(
                        a.acceleration,
                        Vector.constant_multiply(force / a.mass,
                                Vector.unit_vector(
                                        Vector.subtract(b.position, a.position))));
                b.acceleration = Vector.add(
                        b.acceleration,
                        Vector.constant_multiply(
                                force / b.mass,
                                Vector.unit_vector(
                                        Vector.subtract(a.position, b.position))));
            }
        }
        // Just a status flag, also indicates that there have been no collisions
        return true;
    }

}
