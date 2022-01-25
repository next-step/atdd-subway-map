package nextstep.subway.domain.service;

import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.repository.LineRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class LineValidator {

    private final LineRepository lineRepository;

    public LineValidator(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void validateLine(final String name, final String color) {
        validateName(name);
        validateColor(color);
    }

    public void validateName(final String name) {
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

    public void validateColor(final String color) {
        if (Objects.isNull(color)) {
            throw new IllegalArgumentException("노선의 색상은 필수 입니다.");
        }
        if (color.isEmpty()) {
            throw new IllegalArgumentException("노선의 색상은 1 자 이상 이어야 합니다.");
        }
    }
}
