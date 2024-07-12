package subway.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.entity.line.Line;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;

public interface LineRepository extends JpaRepository<Line, Long> {
    @NonNull
    default Line findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new SubwayDomainException(SubwayDomainExceptionType.NOT_FOUND_LINE));
    }
}
