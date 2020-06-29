# 카카오 숙제
## 카카오페이 뿌리기 기능 구현하
### 기능 요구 사항
* 뿌리기, 받기, 조회 기능을 수행하는 REST API를 구현합니다.
    * 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로
      전달됩니다.
    * 청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는
       HTTP Header로 전달됩니다.
    * 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로
      잔액에 관련된 체크는 하지 않습니다.
* 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에
  문제가 없도록 설계되어야 합니다.
* 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

### 상세 구현 요건 및 제약사항
1. 뿌리기 API
* 다음 조건을 만족하는 뿌리기 API를 만들어 주세요
    * 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
    * 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
    * 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
    * token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.    
    
2. 받기 API
* 다음 조건을 만족하는 받기 API를 만들어 주세요.
    * 뿌리기 시 발급된 token을 요청값으로 받습니다.
    * token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를
    * 뿌리기당한사용자는한번만받을수있습니다.
    * 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
    * 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
      있습니다.
    * 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기
      실패 응답이 내려가야 합니다.
      
3. 조회 API
* 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.
    * 뿌리기 시 발급된 token을 요청값으로 받습니다.
    * token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
      상태는 다음의 정보를 포함합니다.
        * 뿌린시각,뿌린금액,받기완료된금액,받기완료된정보([받은금액,받은사용자 아이디] 리스트)
    * 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
    * 뿌린건에대한조회는7일동안할수있습니다.

### 구현 설명
1. 스펙
- springboot
- JPA
- mysql
- thymeleaf

2. DB 스키마
- schema.sql에 적어 놓았습니다.

3. 화면
- localhost:8080 <-- thymeleaf로 만든 api 로그를 볼수 있는 프론트 개발

4. 구현 기능
* comtroller
    * HomeController
        - 화면에서 첫화면 index 연결
    * KpayApiController
        - 돈 뿌리기 기능 --> /kpay/inputMoney
        - 뿌린 토큰을 찾아 금액을 받을 때 마다 로그를 쌓는 기능 --> /kpay/outputMoney
        - 토큰을 기반으로 뿌린 log를 찾는 기능 --> /kpay/{token}
    * KpayController
        - thymeleaf로 만든 화면에 log 전체를 조회하는 컨트롤러
* exception
    * KpayAlreadyGotMoneyException
        - 이미 돈을 받을 예외
    * KpayAuthException
        - 트큰으로 조회할 때 자신것만 볼수 있는 예외
    * KpayHeaderException
        - 헤더가 없을 때 예외
    * KpayLowMoneyException
        - 뿌린 돈이 일정 이상이 아닐 때 예외
    * KpaySelfMoneyException      
        - 자기가 뿌린 돈을 자기가 받을 때 예외  
* model
    * BaseTimeEntity
        - 입력 일자, 수정 일자 추상화
    * Kmoney
        - 돈 객체
    * Kpay
        - 돈 뿌리기 객체
    * Kuser
        - 유저 객체
    * SeparateMoney
        - 돈 뿌리 정보를 다루는 객체
* repositoy
* util
    * KpayMakeRandom
        - 랜덤으로 받는 돈 액수를 정해주는 기능
        - 랜덤으로 3자리 토큰을 정해주는 기능
* KpayApplication
    - 컨트롤러
    
* test
    * KpayApiControllerTest
        - 최초 뿌리기 쌓기
        - 최초 뿌리기 쌓기 -> 돈 타먹기
        - 토큰으로 조회  
    * KmoneyTest
        - 적은금액 입력 테스트     