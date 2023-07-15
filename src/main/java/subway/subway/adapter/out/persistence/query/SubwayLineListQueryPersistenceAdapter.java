package subway.subway.adapter.out.persistence.query;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineResponseMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineJpaRepository;
import subway.subway.application.out.query.SubwayLineListQueryPort;
import subway.subway.application.query.SubwayLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayLineListQueryPersistenceAdapter implements SubwayLineListQueryPort {

    private final SubwayLineJpaRepository subwayLineJpaRepository;
    private final SubwayLineResponseMapper subwayLineResponseMapper;

    public SubwayLineListQueryPersistenceAdapter(SubwayLineJpaRepository subwayLineJpaRepository, SubwayLineResponseMapper subwayLineResponseMapper) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
        this.subwayLineResponseMapper = subwayLineResponseMapper;
    }

    @Override
    public List<SubwayLineResponse> findAll() {
        List<SubwayLineJpa> subwayLineJpas = subwayLineJpaRepository.findAllWithSections();
        return subwayLineJpas.stream().map(subwayLineResponseMapper::from).collect(Collectors.toList());
    }
}
