package subway.line;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;

    protected Line() {
    }

    public static Line of(Long id, String name, String color) {
        Line line = Line.of(name, color);
        line.id = id;
        return line;
    }

    public static Line of(String name, String color) {
        Line line = new Line();
        line.name = name;
        line.color = color;
        return line;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
