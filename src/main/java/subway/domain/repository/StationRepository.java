package subway.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.entity.station.Station;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;

public interface StationRepository extends JpaRepository<Station, Long> {
    @NonNull
    default Station findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new SubwayDomainException(SubwayDomainExceptionType.NOT_FOUND_STATION));
    }
}