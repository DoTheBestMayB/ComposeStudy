@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dothebestmayb.composestudy.R
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme


val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }

/**
 * 예시에서는 두 개의 Composable 간에 전환을 보여줬는데
 * 동일한 Animation 로직을 Screen 간에도 적용할 수 있다.
 *
 * 중요한 것은 애니메이션을 적용할 Composable이
 * 동일한 SharedTransitionLayout에 포함되어야 한다는 것이다.
 *
 * CompositionLocal을 이용해 Screen 간에 SharedTransitionLayout을 공유할 수 있다.
 * staticCompositionLocalOf + CompositionLocalProvider
 * 하지만 CompositionLocal을 이용하는 것은 주의해서 사용해야 하기 때문에 수신객체지정람다로 전달하는 것이 더 권장된다.
 *
 * SharedTransitionScope 영역에서 사용하는 Modifier 2가지
 * Modifier.sharedBounds : Composable 사이에 사용
 * Modifier.sharedElements : 동일한 종류의 Composable 사이에 사용
 *
 * Layout 혹은 여러 Composable이 사용된 Composable은 SharedBounds를 사용?
 */
@Composable
private fun SharedTransitionAnimation(modifier: Modifier = Modifier) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    // 내부에서 SharedTransitionScope를 사용함
    // SharedTransitionScope을 이용해 서로 다른 Compoable을 동일한 것처럼 애니메이션을 적용해보자.
    SharedTransitionLayout(
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        // Box가 세로 상으로 가운데에 위치하도록 하기 위해 weight 설정
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    this@Column.AnimatedVisibility(
                        visible = !isExpanded
                    ) {
                        RowListItem(
                            onClick = {
                                isExpanded = !isExpanded
                            },
                            animatedVisibilityScope = this,
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("item-layout"),
                                    animatedVisibilityScope = this,
                                )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    this@Column.AnimatedVisibility(
                        visible = isExpanded
                    ) {
                        ColumnListItem(
                            onClick = {
                                isExpanded = !isExpanded
                            },
                            animatedVisibilityScope = this,
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("item-layout"),
                                    animatedVisibilityScope = this,
                                )
                        )
                    }
                }
            }
        }
    }
}

// CompositionLocal을 사용해 SharedTransitionScope을 공유하지 않을 경우, 수신객체를 지정해줘야 함
@Composable
fun SharedTransitionScope.RowListItem(
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    // CompositionLocal을 사용해 SharedTransitionLocal을 공유하는 경우 아래와 같이 접근할 수 있다.
//    val scope = LocalSharedTransitionScope.current
//    with (scope) {
//
//    }
    Row(
        modifier = modifier
            // child 중 가장 작은 Composable의 height 크기로 설정함
            .height(IntrinsicSize.Min)
            .padding(16.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.living_room),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .sharedElement(
                    // dynamic layout에서는 `image-${selectedItem.id}`와 같이 고유 key가 할당되도록 해야 함
                    state = rememberSharedContentState("image"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                // Text의 높이를 이미지 크기 만큼 차지하도록 설정하기 위한 modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "List item",
                fontSize = 20.sp,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState("title"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
            )
            Text(
                text = "This is the list item description",
                fontSize = 14.sp,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState("description"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
            )
        }
    }
}

@Composable
fun SharedTransitionScope.ColumnListItem(
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.living_room),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(250.dp)
                .sharedElement(
                    state = rememberSharedContentState("image"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
        )
        Text(
            text = "List item",
            fontSize = 20.sp,
            modifier = Modifier
                .sharedElement(
                    state = rememberSharedContentState("title"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
        )
        Text(
            text = "This is the list item description",
            fontSize = 14.sp,
            modifier = Modifier
                .sharedElement(
                    state = rememberSharedContentState("description"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun SharedTransitionAnimationPreview() {
    ComposeStudyTheme {
        SharedTransitionAnimation()
    }
}
