package subway.application.service;

import subway.domain.LineDomain;

import java.util.List;

public interface LineLoadUseCase {

    LineDomain loadLine(Long createdLineId);

    List<LineDomain> loadLines();

}
