- main safety : suspend function은 Main Thread에서 호출해도 안전(Blocking 하지 않음)하게 만들어야 한다.

- 단순히 suspend function을 붙이는 것에 그치지 않고, withContext를 이용해 적절한 Dispatcher로 context switching 하는 작업이 필요하다.
- logcat에서 Skipped frames 로그 값을 통해 Main Thread에서 너무 많은 작업을 하고 있는지 확인할 수 있다.
- state를 update할 때, 3중 for문과 같이 복잡한 연산을 필요로 하는 경우에도 Default Dispatcher로 switching 한 후 작업하는 것이 좋다.
