# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 지하철 노선관리 요구사항 분석
### 응답 객체
- LinesResponse
    - id, name, color, stations
    - stations: List<id, name>

### 기능 별 응답 정리
- 생성
    - 엔드포인트: /lines
    - 응답: 201
        - LinesResponse
    - 파라미터
        - name, color, upStation, downStation, distance
- 수정
    - 엔드포인트: /lines/{id}
    - 응답: 200
        - none
    - 파라미터
        - name, color
- 조회 list
    - 엔드포인트: /lines
    - 응답: 200
        - List<LinesResponse> 
- 조회
    - 엔드포인트: /lines/{id}
    - 응답: 200
        - LinesResponse
- 삭제
    - 엔드포인트: /lines/{id}
    - 응답: 204
        - none

### 사전 준비 필요한 요소
- Station
    - 최소 2개 역: upStation, downStation
- context혹은 table 초기화
    - Truncate를 사용해서 개선하는 것은 일단 만들고 진행하기

### 고려사항
- 인수테스트 리펙토링

## 지하철 구간 관리 요구사항 정리
### 필요 작업 사항
- [ ] 인수 조건 도출
- [ ] 인수 테스트 작성
- [ ] 예외 케이스 검증

### 구간 등록 기능
- 엔드포인트: POST /lines/{id}/sections
- 응답: 201
    - LinesResponse
- 조건
    - 새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행역이어야 한다.
    - 위 조건에 부합하지 않는 경우 에러 처리한다. 
- 인수테스트
    - 성공하는 경우
        - When 지하철 구간을 추가하면
        - Then 지하철 노선 조회시 추가된 구간을 확인할 수 있다.
    - 실패하는 경우
        - When 지하철 구간이 하행선이 아닌 곳에서 추가하면
        - Then 400 에러가 리턴된다.
### 구간 제거 기능
- 엔드포인트: DELETE /lines/{id}/sections?stationId=
- 응답: 204
- 조건
    - 하행 종점역만 제거 가능
    - 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우는 삭제할 수 없음
    - 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리
- 인수 테스트
    - 성공하는 경우
        - Given 3개의 역이 포함된 지하철 역이 주어지고
        - When 하행 종점역을 삭제하면
        - Then 정상 삭제가 된다.
    - 실패하는 경우
        - 1조건
            - Given 2개의 역이 포함된 지하철 역이 주어지고
            - When 하행 종점역을 삭제하면
            - Then 에러가 발생한다.
        - 2조건
            - Given 3개의 역이 포함된 지하철 역이 주어지고
            - When 하행 종점역이 아닌 역을 삭제하면
            - Then 에러가 발생한다.


