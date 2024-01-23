package subway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long lineId;
    @Column(length = 20, nullable = false)
    private String name;


    public Station() {
    }

    public Station(String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name, Long lineId) {
        this.id = id;
        this.name = name;
        this.lineId = lineId;
    }

    public void updateLineId(Long lineId) {
        this.lineId = lineId;
    }
}
