package subway.line;

import javax.persistence.*;

@Entity
@Table(name = "line")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Color color;

    protected Line() {}

    public Line(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void update(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
