package subway.entity;

import javax.persistence.*;
import java.util.List;


@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
<<<<<<< HEAD
=======

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return Objects.equals(getId(), line.getId()) && Objects.equals(getName(), line.getName()) && Objects.equals(color, line.color) && Objects.equals(upStationId, line.upStationId) && Objects.equals(downStationId, line.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), color, upStationId, downStationId);
    }

    public static Line EMPTY = new Line();
>>>>>>> kakao-jamesjin
}
