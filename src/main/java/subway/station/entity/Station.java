package subway.station.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.line.entity.Line;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Line line;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id, String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name) && Objects.equals(line, station.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, line);
    }
}
