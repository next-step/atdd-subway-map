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

//    @ManyToOne
//    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    private Line line;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public boolean equalsId(final Station other) {
        return this.id.equals(other.getId());
    }


    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
