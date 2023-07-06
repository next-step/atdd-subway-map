package subway.subway.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.StationJpa;
import subway.subway.application.query.StationResponse;
import subway.subway.domain.Station;

@Component
public class StationJpaMapper {

    public StationJpa mapFrom(Station station) {
        return new StationJpa(station.getName());
    }

    public StationResponse mapStationResponseFrom(StationJpa station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
    public Station mapStationFrom(StationJpa station) {
        return Station.of(
                new Station.Id(station.getId()),
                station.getName()
        );
    }
}
