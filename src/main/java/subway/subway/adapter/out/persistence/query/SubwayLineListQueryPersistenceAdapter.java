package subway.subway.adapter.out.persistence.query;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineResponseMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.query.SubwayLineListQueryPort;
import subway.subway.application.query.SubwayLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayLineListQueryPersistenceAdapter implements SubwayLineListQueryPort {

    private final SubwayLineRepository subwayLineRepository;
    private final SubwayLineResponseMapper subwayLineResponseMapper;

    public SubwayLineListQueryPersistenceAdapter(SubwayLineRepository subwayLineRepository, SubwayLineResponseMapper subwayLineResponseMapper) {
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineResponseMapper = subwayLineResponseMapper;
    }

    @Override
    public List<SubwayLineResponse> findAll() {
        List<SubwayLineJpa> subwayLineJpas = subwayLineRepository.findAllWithSections();
        return subwayLineJpas.stream().map(subwayLineResponseMapper::from).collect(Collectors.toList());
    }
}
