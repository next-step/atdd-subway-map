package subway.subway.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SectionOperateManager {
    private final SectionAddOperator sectionAddOperator;

    @Autowired
    public SectionOperateManager(SectionAddOperator sectionAddOperator) {
        this.sectionAddOperator = sectionAddOperator;
    }

    public SectionOperator getOperator(SubwaySections subwaySections) {
        return sectionAddOperator;
    }
}
