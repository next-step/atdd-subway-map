package subway;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;


    @ManyToOne()
    @JoinColumn(name="subwayLineId")
    private SubwayLine subwayLine;

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, SubwayLine subwayLine) {
        this.name = name;
        this.subwayLine = subwayLine;
    }
    public Station() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubwayLine(SubwayLine subwayLine) {
        this.subwayLine = subwayLine;
    }
}
