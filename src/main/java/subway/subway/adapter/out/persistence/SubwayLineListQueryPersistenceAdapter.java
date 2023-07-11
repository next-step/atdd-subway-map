package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineJpaMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.SubwayLineListQueryPort;
import subway.subway.application.query.SubwayLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayLineListQueryPersistenceAdapter implements SubwayLineListQueryPort {

    private final SubwayLineRepository subwayLineRepository;
    private final SubwayLineJpaMapper subwayLineJpaMapper;

    public SubwayLineListQueryPersistenceAdapter(SubwayLineRepository subwayLineRepository, SubwayLineJpaMapper subwayLineJpaMapper) {
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineJpaMapper = subwayLineJpaMapper;
    }

    @Override
    public List<SubwayLineResponse> findAll() {
        List<SubwayLineJpa> subwayLineJpas = subwayLineRepository.findAllWithSections();
        return subwayLineJpas.stream().map(subwayLineJpaMapper::toSubwayLineResponse).collect(Collectors.toList());
    }
}
