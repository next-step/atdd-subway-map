# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 지하철 노선관리 요구사항 분석
### 응답 객체
- LinesResponse
    - id, name, color, stations
    - stations: List<id, name>

### 기능 별 응답 정리
- 생성
    - 엔드포인트: /line
    - 응답: 201
        - LinesResponse
    - 파라미터
        - name, color, upStation, downStation, distance
- 수정
    - 엔드포인트: /line/{id}
    - 응답: 200
        - none
    - 파라미터
        - name, color
- 조회 list
    - 엔드포인트: /line
    - 응답: 200
        - List<LinesResponse> 
- 조회
    - 엔드포인트: /line/{id}
    - 응답: 200
        - LinesResponse
- 삭제
    - 엔드포인트: /line/{id}
    - 응답: 204
        - none

### 사전 준비 필요한 요소
- Station
    - 최소 2개 역: upStation, downStation
- context혹은 table 초기화
    - Truncate를 사용해서 개선하는 것은 일단 만들고 진행하기

### 고려사항
- 인수테스트 리펙토링