## 의존성 주입, 리팩토링, Bottom Navigation, 라벨/마일스톤 화면

1. hilt 제대로된 활용
- NetworkDataSource 파일에 httpclient 인스턴스, retrofit 인스턴스, REST API methods 를 사용하여, NetworkDataSource에 정의한 method를 사용하여 데이터를 가져오거나 요청할 수 있도록 구현
- NetworkDataSource 객체를 repository에 주입하여 사용
- repository 객체를 viewModel에 주입하여 사용
- 주입하기 위해 객체로 반환해주는 함수를 interface module(binds) 혹은 class/object module(provides)에서 정의하여 사용
- interface 또는 abstract class내에서 @Binds 어노테이션을 사용하여 객체를 반환하는 함수 정의 부분은 주입할 객체의 타입을 반환하는 함수로 정의

- @Provides 구현시 interface module 내에서 시도하여 이를 발견하지 못하고 `HiltJsonCompilerException`이 계속 발생하여 많은 삽질을 했었다.
  - 그래서 Provides 외부를 companion object로 선언한 후 주입을 시도해보니 성공적으로 주입이 되었다.
  - Provides 는 class 또는 object module 내에서 구현되는 것을 확인하였다.

2. 코드리뷰 리팩토링
- ui 패키지 구조 변경: 상단 feature 하위에 하단 feature를 두고, 그 하위에 adapter, viewmodel, fragment를 두도록 변경
- data 패키지 구조 변경: network, demo, repository로 구분하여 패키지를 나누고, 각 패키지 내에 필요한 파일을 두도록 변경
- data 패키지에서 assets 접근 방식 변경: res/raw -> assets 폴더 내에서 json 파일을 읽어오는 방식을 변경
- application 레벨의 context 사용 방식 변경: repository -> @module 의 @Provides 어노테이션을 사용하여 application context를 주입받아 사용

3. Bottom Navigation 추가
- Bottom Navigation을 통해 이슈, 라벨, 마일스톤 화면으로 이동할 수 있도록 구성하였다.
- XML 파일을 통해 Bottom Navigation을 구성하고, 각각의 아이템을 클릭하면 해당하는 fragment로 이동하도록 구현하였다.
- 기존의 issue 관련 nav_graph를 bottom nav_graph에 nested하여 구성하였다.

4. 라벨, 마일스톤 초기 화면 설계
- 라벨, 마일스톤 화면을 Fragment 내의 onCreatedView에서 ComposeView를 사용하여 구성했다.
- LazyList, LazyGrid를 사용하여 리스트, 그리드 형태로 데이터를 보여주도록 구성하였다.
- Fragment 에서 코루틴 스코프와 라이프사이클 상태를 설정하여 collect 함수를 사용하여 ui state 를 옵저빙하는 방식과 다르게, Composable 함수에서는 collectAsState 함수만을 사용하여 ui state를 옵저빙하는 방식으로 구현하였다.