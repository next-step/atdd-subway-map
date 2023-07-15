package subway.subway.application.in;

import subway.subway.domain.Station;
import subway.subway.domain.SubwayLine;

public interface SubwaySectionCloseUsecase {

    void closeSection(SubwaySectionCloseUsecase.Command command);

    class Command {
        private SubwayLine.Id subwayLineId;

        private SubwaySectionCloseUsecase.Command.SubwaySection subwaySection;

        public SubwayLine.Id getSubwayLineId() {
            return subwayLineId;
        }

        public Station.Id getStationId() {
            return subwaySection.getStationId();
        }

        public Command(SubwayLine.Id subwayLineId, SubwaySection subwaySection) {
            this.subwayLineId = subwayLineId;
            this.subwaySection = subwaySection;
        }

        public static class SubwaySection {
            private Station.Id stationId;

            public SubwaySection(Station.Id stationId) {
                this.stationId = stationId;
            }

            public Station.Id getStationId() {
                return stationId;
            }
        }

    }

}
