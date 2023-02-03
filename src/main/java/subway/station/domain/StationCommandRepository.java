package subway.station.domain;

public interface StationCommandRepository {

    Station save(Station station);

    void deleteById(Long stationId);
}
