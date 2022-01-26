<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```

### Step1 피드백
엔티티를 update할 때, 인자를 직접 받아서 해당 상태를 변경하도록 한다.

### Step3 생각해보기
* 구간 등록 기능
- 요구사항 설명이 주어져있음.(아 이런이런 것들이 있어야해요. 지하철 노선에 구간을 등록해야하고요. 새로운 구간을 등록할 때, 노선 상행 하행이 잘 맞아야해요. 상하상 안되고 1 2 3 1 안되고요. 네 이렇게 등록할 수 있으면 될 것 같아요)
- 위의 말을 통과하는 인수조건을 만들고, 그걸 바탕으로 인수테스트 작성 후에, 기능을 구현해야한다. 이게 ATDD 사이클임.
- 인수조건이 뭐지? 상대방한테 인수되기 위한 조건인데, 이는 당연하게 요구사항임. 인수조건 = 요구사항.
- 그러면 요구사항을 바탕으로 인수테스트를 작성해야하는데, 요구사항이 테스트를 작성하기에 적합하게 정리되어있지 않음. 그래서 한 단계 중간과정을 낌.
- 그 중간과정이 인수조건을 시나리오 형식으로 표현하는 것임.
- 그러면 제일 중요한 것. 요구사항을 시나리오 형식으로 나타내는 것. 그러니깐 이걸 먼저 해야함.

### Step3 시나리오 작성해보기
* 구간 등록 기능 (정상적인 시나리오)
- 지하철노선 신분당선(상행:강남역, 하행:양재역)이 등록되어있다.
- 신분당선에 구간(상행:양재역, 하행:판교역) 등록 요청한다.
- 구간 등록이 성공한다.

구간 등록 기능 (비정상적인 시나리오1 - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.)
- 지하철노선 신분당선(상행:강남역, 하행:양재역)이 등록되어있다.
- 신분당선에 구간(상행:역삼역, 하행:판교역) 등록 요청한다.
- 구간 등록이 실패한다.

구간 등록 기능 (비정상적인 시나리오2 - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.)
- 지하철노선 신분당선(상행:강남역, 하행:양재역)이 등록되어있다.
- 신분당선에 구간(상행:양재역, 하행:강남역) 등록 요청한다.
- 구간 등록이 실패한다.