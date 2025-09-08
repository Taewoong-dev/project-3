## Plan

#### day1
1. remote repository에 브랜치 생성
    - 브랜치 생성시 폴더 내에 브랜치를 생성하도록 설정
        - And01A/main - 메인 브랜치
        - And01A/init-merge - 병합 처리 브랜치
2. repository 병합
    2-1. And01A/main - K006, K021 브랜치 병합
        - K006, K021 브랜치 병합
        - source branch 는 K021
        - ui layer는 K006, data layer는 K021 브랜치 참고
    2-2. ui, data layer 병합
        - 기존 K006, K021 의 두 브랜치간의 dependency 버전 차이를 유의하며 충돌 방지
        - ui layer에서 참조하는 data layer의 클래스 타입명에 맞춰 수정

#### day2
1. 어제 진행한 프로젝트 병합 과정 완성
    
2. 완료하지 못한 기능은 그대로 두며, 약간의 수정이 필요한 기능에 대해서는 수정하여 업데이트

  - activity single top bar -> 각각의 fragment top bar로 변경
  - swipe refresh layout 추가

3. 병합 완료 후, 프로젝트를 실행하여 정상적으로 동작하는지 확인

#### day3

1. Compose 관련 라이브러리 추가

#### day4
1. dagger-hilt 의존성 주입 적용
  - ui, data layer에 각각의 dagger-hilt 모듈을 생성

2. 코드리뷰 리팩토링

3. Bottom Navigation 추가

4. 라벨, 마일스톤 초기 화면 compose로 설계

#### day6

1. 레이블 화면 리팩토링
2. Json 파일 Jvm Unit Test
3. base fragment 이름 수정

#### day7
1. inject 타입 implemented class -> interface
2. okHttpClient, retrofit, Api method interface 를 module로 분리