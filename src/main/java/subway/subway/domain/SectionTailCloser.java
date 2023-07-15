package subway.subway.domain;

import org.springframework.stereotype.Component;

@Component
class SectionTailCloser implements SectionCloser{
    @Override
    public void apply(SubwayLine subwayLine, Station station) {
        subwayLine.closeSection(station);
    }
}
