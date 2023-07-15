package subway.subway.application.in;

import subway.subway.application.query.StationResponse;

public interface StationRegisterUsecase {
    StationResponse saveStation(StationRegisterUsecase.Command command);

    class Command {
        private final String name;

        public Command(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
