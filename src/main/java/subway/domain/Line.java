package subway.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    private Station downStation;

    private Integer distance;

    protected Line() {
    }

    public Line(final Long id, final String name, final String color, final Station upStation, final Station downStation, final Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Line(final String name, final String color, final Station upStation, final Station downStation, final Integer distance) {
        this(null, name, color, upStation, downStation, distance);
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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void updateLine(final String changeName, final String changeColor) {
        this.name = changeName;
        this.color = changeColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return id.equals(line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
