package PhysicsSim;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Vector {

    public Double[] components;
    public Double[] angles;
    private Double magnitude;
    public static Vector ZeroVector;

    // Vectors for Standard Usage
    static {
        ZeroVector = new Vector(0d,0d,0d);
    }

    // If a vector is defined in components form, i.e {x, y, z, .... }
    public Vector(Double... components) {

        // if components > Constants.DIMENSIONS, give a warning
        if (components.length > Constants.DIMENSIONS) {
            System.out.println("Excess Components Passed, only " + Constants.DIMENSIONS + " components are considered.");
        }

        // know how many components are provided, assign, else keep the value of extra components to zero
        this.components = new Double[Constants.DIMENSIONS];
        int l = components.length;
        for (int i = 0; i < Constants.DIMENSIONS; i++) {
            if (i < l) {
                this.components[i] = components[i];
            } else {
                this.components[i] = 0d;
            }
        }

        // also find the angles made by adjacent components (in radians)
        calculate_angles();
        // calculate magnitude
        calculate_magnitude();


    }

    // If a vector is defined in polar form i.e, r, theta
    // Currently Supported for 2D vectors only
    public Vector(boolean polar, double magnitude, double theta) {
        this(find_components_from_polar(magnitude, theta));
        this.calculate_angles();
    }

    // function to find components of a vector described as r, theta;
    private static Double[] find_components_from_polar(double magnitude, double theta) {
        return new Double[]{magnitude * Math.cos(theta), magnitude * Math.sin(theta)};
    }

    // function to calculate angles made adjacent components
    private void calculate_angles() {
        // make the angle vector -> angle[i] = arctan(angle[i+1]/angle[i]
        this.angles = new Double[Constants.DIMENSIONS - 1];
        for (int i = 0; i < Constants.DIMENSIONS - 1; i++) {
            this.angles[i] = Math.atan(this.components[i + 1] / this.components[i]);
        }
    }


    // calculate magnitude (Sqrt of Sum of Squares of all Components)
    private double calculate_magnitude() {
        double sum_of_magnitudes_squared = 0.00;
        for (int i = 0; i < Constants.DIMENSIONS; i++) {
            sum_of_magnitudes_squared += this.components[i] * this.components[i];
        }
        this.magnitude = Math.sqrt(sum_of_magnitudes_squared);
        return this.magnitude;
    }

    // These functions do what they say
    public Double x() {
        return this.components[0];
    }

    public Double y() {
        return this.components[1];
    }

    public Double getMagnitude() {
        return this.magnitude;
    }

    public static Double findMagnitude(Vector a) {
        return a.calculate_magnitude();
    }

    // component wise addition
    public static Vector add(Vector a, Vector b) {
        Vector sum = new Vector();
        for (int i = 0; i < Constants.DIMENSIONS; i++) {
            sum.components[i] = a.components[i] + b.components[i];
        }
        sum.calculate_angles();
        sum.calculate_magnitude();
        return sum;
    }

    // component wise difference a - b
    public static Vector subtract(Vector a, Vector b) {
        Vector diff = new Vector();
        for (int i = 0; i < Constants.DIMENSIONS; i++) {
            diff.components[i] = a.components[i] - b.components[i];
        }
        return diff;
    }

    // Scalar Multiplication, when two vectors are provided, also called Dot Product
    public static double scalar_multiply(Vector a, Vector b) {
        double product = 0d;
        for (int i = 0; i < Constants.DIMENSIONS; i++) {
            product += a.components[i] * b.components[i];
        }
        return product;
    }

    // Scalar Multiplication when a double and a vector is provided
    public static Vector constant_multiply(double d, Vector b) {
        Vector product = new Vector(0d, 0d, 0d);
        for (int i = 0; i < Constants.DIMENSIONS; i++) {
            product.components[i] = d * b.components[i];
        }
        return product;
    }

    // Cross-Product of a, b, currently supported for upto 3 Dimensions
    public static Vector cross_product(Vector a, Vector b) {
        // https://en.wikipedia.org/wiki/Cross_product
        // From Matrix Notation Method
        double ans_x = a.components[1] * b.components[2] - a.components[2] * b.components[1];
        double ans_y = -(a.components[0] * b.components[2] - a.components[2] * b.components[0]);
        double ans_z = a.components[0] * b.components[1] - a.components[1] * b.components[0];

        return new Vector(ans_x, ans_y, ans_z);
    }

    // this functions the unit vector pointing in the direction of a
    public static Vector unit_vector(Vector a){
        double mod = findMagnitude(a);
        return constant_multiply(1/mod, a);
    }

    @Override
    public String toString() {
        // Set up Decimal Formatter
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);

        // Build the output string
        StringBuilder out = new StringBuilder("(");
        for (double component : components) {
            out.append(df.format(component)).append(", ");
        }
        // remove excess commas and space
        out.delete(out.length()-2, out.length());

        // append magnitude of the vector
        out.append("), mod: ").append(df.format(magnitude));

        return out.toString();
    }
}
