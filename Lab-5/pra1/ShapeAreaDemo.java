import java.util.Locale;

public class ShapeAreaDemo {
    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(2),
            new Rectangle(3, 4),
            new Triangle(6, 5)
        };

        double runningTotal = 0;
        double largestArea = -1;
        String largestShape = "";

        for (Shape shape : shapes) {
            double currentArea = shape.area();
            runningTotal += currentArea;

            if (currentArea > largestArea) {
                largestArea = currentArea;
                largestShape = shape.getClass().getSimpleName();
            }

            System.out.printf(Locale.ROOT, "%s area: %.2f, running total: %.2f%n",
                    shape.getClass().getSimpleName(), currentArea, runningTotal);
        }

        System.out.printf(Locale.ROOT, "Largest: %s (%.2f)%n", largestShape, largestArea);
    }
}
