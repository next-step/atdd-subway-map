package subway.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class SectionAddOperator implements SectionOperator{
    @Override
    public void apply(SubwaySections subwaySections, SubwaySection subwaySection) {
        subwaySections.add(subwaySection);
    }
}
