package nextstep.subway.applicaion.dto;

import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private final Long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public StationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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

    public static List<StationResponse> toList(List<Station> stations) {
        return stations.stream()
                       .map(StationService::createStationResponse)
                       .collect(Collectors.toList());
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
