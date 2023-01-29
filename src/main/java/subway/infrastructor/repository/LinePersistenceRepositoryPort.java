package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.application.repository.LineRepositoryPort;
import subway.domain.LineCreateDomain;

@Component
class LinePersistenceRepositoryPort implements LineRepositoryPort {

    private final LineRepository repository;
    private final LineMapper mapper;

    LinePersistenceRepositoryPort(LineRepository repository, LineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Long createLine(LineCreateDomain lineCreateDomain) {
        LineJpaEntity lineJpaEntity = mapper.domainToEntity(lineCreateDomain);
        return repository.save(lineJpaEntity).getId();
    }

}
