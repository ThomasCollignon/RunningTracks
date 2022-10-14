import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Point {

    private String label;

    private Map<Point, Integer> linkedPoints;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return label.equals(point.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    public Point(String label) {
        this.label = label;
        this.setLinkedPoints(new HashMap<>());
    }

    public String getLabel() {
        return label;
    }

    public Point setLabel(String label) {
        this.label = label;
        return this;
    }

    public void printListLinks(){
        this.getLinkedPoints().forEach((k, v)-> System.out.println(" " + k.getLabel() + " " + v));
    }

    public Map<Point, Integer> getLinkedPoints() {
        return linkedPoints;
    }

    public Point setLinkedPoints(Map<Point, Integer> linkedPoints) {
        this.linkedPoints = linkedPoints;
        return this;
    }
}
