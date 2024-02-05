package subway.domain;

import java.util.Objects;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;


    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }


    public boolean equalsId(Long id) {
        return this.id.equals(id);
    }

}
