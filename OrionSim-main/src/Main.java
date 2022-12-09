import PhysicsSim.Vector;
import com.astronomy.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Planet earth = new Planet(1, AstronomicalConstants.EarthMass, "Earth")
                .setPosition(new Vector(1d, 0d, 0d))
                .setVelocity(new Vector(0d, 0d, 0d))
                .setAcceleration(new Vector(-1d, 0d, 0d))
                .setRadius(1, "Earth");

        Planet sun = new Planet(1, AstronomicalConstants.SunMass, "Sun")
                .setPosition(Vector.ZeroVector)
                .setVelocity(Vector.ZeroVector)
                .setAcceleration(Vector.ZeroVector)
                .setRadius(1, "Sun");

        Field space = new Field(10, 1000);

        space.add_body(earth);
        space.add_body(sun);

        space.simulate(15000);
    }
}
