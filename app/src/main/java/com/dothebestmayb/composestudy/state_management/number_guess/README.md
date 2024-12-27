
State Hoisting을 위해 최상위 composable에 state, action에 따른 람다를 둬야 한다는 것을 배웠다.
그런데, 화면이 복잡해짐에 따라 state, action 람다가 10, 20, 30개씩 계속 늘어나면 어떻게 관리해야 할까?

이때 등장한 개념이 MVI(Model-View-Intent) 패턴이다.

Composable에 필요한 state를 data class로 정의한다. -> Model
Action에 따른 event를 각각의 람다 파라미터로 정의하지 않고, 한 개의 람다에 어떤 action을 처리해야 하는지 sealed interface를 전달한다. -> Intent

Composable에서 사용자와의 interaction에 따른 state 갱신은 ViewModel에서 처리한다.
-> 버그가 발생했을 때 Composable을 따라가며 모든 코드를 살펴볼 필요 없이, ViewModel 한 곳에서만 확인하면 된다는 장점