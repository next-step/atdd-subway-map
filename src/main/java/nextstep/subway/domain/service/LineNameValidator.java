package nextstep.subway.domain.service;

import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.repository.LineRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class LineNameValidator {

    private final LineRepository lineRepository;

    public LineNameValidator(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void validate(final String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("노선의 이름은 필수 입니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("노선의 이름은 1 자 이상 이어야 합니다.");
        }

        final List<Line> lines = lineRepository.findByName(name);
        if (!lines.isEmpty()) {
            throw new DuplicateArgumentException("중복된 이름 입니다.");
        }
    }
}
