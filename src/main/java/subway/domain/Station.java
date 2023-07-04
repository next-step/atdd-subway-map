package subway.domain;

import subway.service.command.StationCreateCommand;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    protected Station() {
    }

    private Station(String name) {
        this.name = name;
    }

    public static Station create(StationCreateCommand command) {
        return new Station(command.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
