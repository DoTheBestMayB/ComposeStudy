package com.dothebestmayb.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * BaselineProfile은 xml 기반의 View 대상으로 만들어졌다.
 * Compose Modifier.testTag를 인식시키기 위해 Scaffold modifier에 semantics 설정을 해줘야 한다.
 * ```kotlin
 *     Scaffold(
 *         modifier = modifier
 *             .fillMaxSize()
 *             .semantics {
 *                 testTagsAsResourceId = true
 *             },
 * ```
 *
 * 생성된 결과물은 app - baselineprofile - build - outputs - connected_android_test_additional_output - benchmarkRelease - connected - DeviceName 하위에 있다.
 *
 * 이 파일을 app - src - main 하위에 복사 붙여넣기 한다.
 *
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            includeInStartupProfile = true
        ) {
            pressHome()
            startActivityAndWait()

            scrollThroughMainList()

            device.waitForIdle() // fling action이 끝날 때까지 기다림
        }
    }
}

fun MacrobenchmarkScope.scrollThroughMainList() {
    val lazyColumn = device.findObject(By.res("main_list"))
    lazyColumn.fling(Direction.DOWN)
    lazyColumn.fling(Direction.UP)
}
