# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## Step1

### 인수 조건

```
Feature: 지하철역 목록 조회

    Given 2개의 지하철역을 생성하고
    When 지하철역 목록을 조회하면
    Then 2개의 지하철역을 응답 받는다
    
Feature: 지하철역 삭제

    Given 지하철역을 생성하고
    When 그 지하철역을 삭제하면
    Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
```

## Step0

### 인수 조건

```
Feature: 구글 페이지 접근

    Scenario: 구글 페이지 요청
        Given 구글 URL "https://google.com"이 있다.
        When URL로 요청한다.
        Then 200응답 코드를 응답받는다.
```
