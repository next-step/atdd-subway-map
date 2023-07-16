package subway.subway.application.in;

import subway.subway.domain.Station;
import subway.subway.domain.SubwayLine;

public interface StationCloseUsecase {
    void closeStation(StationCloseUsecase.Command command);

    class Command {
        private final Station.Id id;

        public Command(Station.Id id) {
            this.id = id;
        }

        public Station.Id getId() {
            return id;
        }
    }
}
