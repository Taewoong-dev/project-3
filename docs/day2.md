## 프로젝트 병합

1. 아키텍처: Single Activity Architecture 
   - Single Viewmodel on Activity - 모든 Fragment가 공유하는 ViewModel 

2. UI 구성
   - Fragment 별로 top bar 를 각각 갖음
     - top bar의 navigationIcon, menuIcon 설정을 programmatically 하게 변경
   
   - 이슈 화면 
     - 필터에 따라 동적으로 github issue REST API get method 호출
     - QeueryMap 을 통해 필터링 조건을 설정
       - ViewModel에서 REST API methods 까지 map 형태로 데이터 전달
     - Retrofit2 의 @QueryMap 어노테이션을 통해 동적으로 필터링 조건을 설정
     - UiState Success, Loading, Failure 세가지로 나누어 세 경우에 따라 작업 처리 수정
       - Success 경우 stateFlow의 value에 값 대입, loading indicator 비활성화, 이슈 리스트 띄우기
       - Loading 경우 loading indicator 활성화, 이슈 리스트 감추기
       - Failure 경우 Snackbar 띄우기
     - SwipeRefreshLayout 을 통해 아래로 당겨서 새로고침 가능
       - 새로고침 시 list API method 호출
     
   - 다중 이슈 화면
     - 이슈 화면에서 다중 이슈 화면으로 navigate 할 시, 뷰모델에 stateFlow에 이슈 화면의 데이터를 다시 대입한 뒤 다중 이슈 화면에서 관찰하여 업로드 하도록함
     - 삭제할 시 다중 이슈 화면 UI 상에서만 제거
     - close할 때에 close API method 호출을 하며 UI 상에서 제거한다.
     - 각 item들은 클릭 가능하여 상세 화면으로 이동 가능하다.
   
   - 새로운 이슈 생성 화면
     - 제목, 내용을 입력하고 담당자를 선택하여 체크 모양 버튼으로 이슈 생성이 가능하다.
     - top bar의 menuIcon 설정을 통해 제목, 내용이 비어있는경우 비활성화, 둘다 채워져있을 경우 활성화
       - 클릭 시 create API method 호출 및 이슈 화면으로 navigate
     - 이슈 생성이 실패되면 failure ui state를 이슈 화면 UI에 전송한다.
   
   - 상세 화면 
     - 미완성 화면으로 추후 작업 예정
     - top bar만 설정완료
       - navigationIcon 은 backPressed 함수로 뒤로가기 설정
       - menuIcon 은 popUpBackStack 함수로 초기 화면으로 이동 설정

3. ViewModel
   - 이슈 리스트 데이터 관리를 liveData -> StateFlow로 변경
   - 이슈 UI에서 스와이프로 refresh 할 시 stateFlow의 상태를 loading으로 변경 후 API method 호출
   - 이슈 화면 -> 다중 이슈 선택 화면 또는 역방향일 때의 상태를 viewmodel로 전달 후 stateFlow에 저장하여 이동된 화면에서 관찰하여 UI 업데이트
     - safe-args 를 통해 이동된 화면에서 데이터를 받아올 수 있도록 설정 예정
   - UI 에서 뷰모델을 옵저버하는 옵저버 패턴 준수

4. data layer 구성
   - repository - networkAPI 구조로 설계
   - 데이터 model 을 DTO, VO로 나누어 구성
     - DTO 구성 : Request, Response
     - Response 속성은 API response schema에 맞춰 구성
   - DTO -> VO로 변환하는 로직을 repository에서 처리
   - retrofit API 인스턴스를 repository에 주입하여 사용
     - hilt를 사용했지만 아직 학습이 요구됨
   - viewmodel에 repository를 주입할 때에도 hilt를 사용했지만 아직 학습이 요구됨
     - interface를 통해 객체를 반환하는 함수 선언과 module에서 bind를 통해 구현하는 방식을 생략하고 class를 곧바로 주입
   