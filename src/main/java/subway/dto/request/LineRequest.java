package subway.dto.request;

import lombok.Getter;
import subway.models.Line;
import subway.models.Section;
import subway.models.Station;

public class LineRequest {
    @Getter
    public static class Create {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public Line toEntity(Section section) {
            Line line = Line.builder()
                .name(this.name)
                .color(this.color)
                .build();
            line.addSection(section);
            return line;
        }

        public Section toSectionEntity(Station upStation, Station downStation) {
            return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .sequence(1)
                .build();
        }
    }
}
