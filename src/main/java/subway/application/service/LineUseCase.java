package subway.application.service;

import subway.domain.LineCreateDomain;
import subway.domain.LineDomain;

import java.util.List;

public interface LineUseCase {

    Long createLine(LineCreateDomain lineCreateDomain);

    LineDomain loadLine(Long createdLineId);

    List<LineDomain> loadLines();

}
