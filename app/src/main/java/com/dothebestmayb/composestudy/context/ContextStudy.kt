package com.dothebestmayb.composestudy.context

/**
 * Context receiver를 활용하면 불필요한 코드 호출을 줄일 수 있다.
 *
 * 예를 들어 Fragment에서 Flow를 collect할 때 아래와 같이 호출한다.
 *
 * ```kotlin
 * viewLifecycleOwner.lifecycleScope.launch {
 *     viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
 *         viewModel.state.collect {
 *             // Update ui...
 *         }
 *     }
 * }
 * ```
 *
 * 이것을 확장 함수로 선언해 아래와 같이 한 줄로 줄일 수 있다.
 * ```kotlin
 * inline fun <T> Flow<T>.launchAndCollectIn(
 *     owner: LifecycleOwner,
 *     minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
 *     crossinline action: suspend CoroutineScope.(T) -> Unit
 * ) = owner.lifecycleScope.launch {
 *     owner.repeatOnLifecycle(minActiveState) {
 *         collect {
 *             action(it)
 *         }
 *     }
 * }
 *
 * ...
 *
 * viewModel.state.launchAndCollectIn(viewLifecycleOwner) {
 *     // Update ui...
 * }
 * ```
 *
 * collect 함수는 일반적으로 Activity, Fragment 내부에서 호출하는 것이 분명하다.
 * conext receiver를 활용해 아래와 같이 확장 함수를 변경하면 viewLifecycleOwner를 파라미터로 전달하지 않아도 된다.
 *
 * ```kotlin
 * context (Fragment)
 * inline fun <T> Flow<T>.launchAndCollectIn(
 *     minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
 *     crossinline action: suspend CoroutineScope.(T) -> Unit
 * ) = viewLifecycleOwner.lifecycleScope.launch {
 *     viewLifecycleOwner.repeatOnLifecycle(minActiveState) {
 *         collect {
 *             action(it)
 *         }
 *     }
 * }
 *
 * ...
 *
 * viewModel.state.launchAndCollectIn {
 *     // Update ui...
 * }
 * ```
 *
 *
 * 출처 : https://medium.com/@hzolfagharipour/comprehensive-guide-to-kotlin-context-receiver-f5478eea6b42
 */


class A {
    fun a() {}
}

context(A)
fun b() {}

fun A.c() {}

fun main() {
    val obj: A = A()

    with(obj) {
        a() // Ok
        b() // Ok
        c() // Ok
    }
    obj.a() // Ok
//    obj.b() // Compile error; Unresolved reference: b
    obj.c() // Ok
}