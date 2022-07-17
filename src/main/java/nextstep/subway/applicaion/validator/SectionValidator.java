package nextstep.subway.applicaion.validator;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.exception.AlreadyRegisteredException;
import nextstep.subway.exception.NotDownStationException;
import nextstep.subway.exception.OnlyOneSectionException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SectionValidator {

    public void createValidate(Line line, SectionRequest sectionRequest) {
        validateFinalDownStation(line, sectionRequest.getUpStationId());
        validateAlreadyExist(line, sectionRequest.getDownStationId());
    }

    public void deleteValidate(Line line, Long stationId) {
        validateFinalDownStation(line, stationId);
        validateOnlyOneSection(line);
    }

    private void validateFinalDownStation(Line line, Long stationId) {
        if (!Objects.equals(line.getFinalDownStationId(stationId), stationId)) {
            throw new NotDownStationException(String.format("%d번 노선의 %d이/가 하행 종점역이 아닙니다.", line.getId(), stationId));
        }
    }

    private void validateAlreadyExist(Line line, Long stationId) {
        if (line.getAllStationIds().contains(stationId)) {
            throw new AlreadyRegisteredException(String.format("이미 %d번 노선에 등록된 역입니다.", line.getId()));
        }
    }

    private void validateOnlyOneSection(Line line) {
        if (line.getSections().size() == 1) {
            throw new OnlyOneSectionException();
        }
    }
}
