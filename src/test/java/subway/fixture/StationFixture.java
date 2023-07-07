package subway.fixture;

import subway.station.dto.request.SaveStationRequestDto;

public class StationFixture {

    public static final SaveStationRequestDto GANGNAM_STATION =
            SaveStationRequestDto.builder()
                    .name("강남역")
                    .build();

    public static final SaveStationRequestDto GWANGGYO_STATION =
            SaveStationRequestDto.builder()
                    .name("광교역")
                    .build();

    public static final SaveStationRequestDto CHEONGNYANGNI_STATION =
            SaveStationRequestDto.builder()
                    .name("청량리역")
                    .build();

    public static final SaveStationRequestDto CHUNCHEONSTATION_STATION =
            SaveStationRequestDto.builder()
                    .name("춘천역")
                    .build();
}
