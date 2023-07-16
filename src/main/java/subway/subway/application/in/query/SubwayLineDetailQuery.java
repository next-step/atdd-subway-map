package subway.subway.application.in.query;

import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

public interface SubwayLineDetailQuery {

    SubwayLineResponse findOne(Command command);

    class Command {
        private final SubwayLine.Id id;

        public Command(SubwayLine.Id id) {
            this.id = id;
        }

        public SubwayLine.Id getId() {
            return id;
        }
    }
}
