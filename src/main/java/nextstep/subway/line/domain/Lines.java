package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineAlreadyExistsException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = Collections.unmodifiableList(lines);
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public void validateExistLine() {
        if(lines.size() > 0) {
            throw new LineAlreadyExistsException();
        };
    }

    public List<LineResponse> toResponses() {
        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }
}
