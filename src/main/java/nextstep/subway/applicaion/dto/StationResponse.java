package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.*;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public StationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public StationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
        this.createdDate = station.getCreatedDate();
        this.modifiedDate = station.getModifiedDate();
    }


    public static List<StationResponse> toDtos(Set<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(toList());
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
