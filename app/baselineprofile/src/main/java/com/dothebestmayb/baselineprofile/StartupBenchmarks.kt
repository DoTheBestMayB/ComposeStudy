package com.dothebestmayb.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 에뮬레이터에서 실행이 안 될 수 있다. 실기기 연결해서 실행하면 된다.
 *
 * 실행 결과
 *
 * SM-S918N - 15 Tests 0/2 completed. (0 skipped) (0 failed)
 * Timed out waiting for process (com.dothebestmayb.composestudy) to appear on samsung-sm_s918n.
 * StartupBenchmarks_startupCompilationBaselineProfiles
 * frameCount               min 349.0,   median 427.0,   max 492.0
 * timeToInitialDisplayMs   min 277.2,   median 284.3,   max 310.6
 * frameDurationCpuMs       P50    3.5,   P90    5.2,   P95    5.9,   P99    8.7
 * frameOverrunMs           P50    0.1,   P90    0.6,   P95    1.0,   P99    3.8
 * Traces: Iteration 0 1 2 3
 *
 * SM-S918N - 15 Tests 1/2 completed. (0 skipped) (0 failed)
 * StartupBenchmarks_startupCompilationNone
 * frameCount               min 397.0,   median 496.5,   max 526.0
 * timeToInitialDisplayMs   min 323.4,   median 341.0,   max 425.8
 * frameDurationCpuMs       P50    4.4,   P90    8.1,   P95    9.1,   P99   13.1
 * frameOverrunMs           P50   -1.4,   P90    2.2,   P95    5.3,   P99   10.5
 * Traces: Iteration 0 1 2 3
 *
 * Finished 2 tests on SM-S918N - 15
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            // StartupTimingMetric : 앱이 첫 프레임을 랜더링하기까지 걸리는 시간
            // FrameTimingMetric : 프레임을 생성하는 데 소요되는 평균 시간
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 4,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()

                scrollThroughMainList() // 테스트할 대상

                device.waitForIdle()
            }
        )
    }
}
