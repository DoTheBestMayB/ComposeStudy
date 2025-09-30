package com.dothebestmayb.composestudy.performance_optimization

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.R

data class Profile(
    val username: String,
    val pictureResId: Int,
)

/**
 * Toggle 버튼을 누를 때마다 Composition이 발생한다. Recomposition이 아니기 때문에 Layout Inspector에는 잡히지 않는다.
 * Composition은 Composable을 다시 생성한다. 가로, 세로 배치만 다른 동일한 Composable을 다시 생성하는 것은 비효율적이다.
 *
 * key composable을 지정해도 Composition이 발생한다. key는 동일한 layout에서 동작하기 때문이다. toggle을 누르면 Row <-> Column 으로 Layout이 변경된다.
 *
 * 이때 movableContentOf를 이용할 수 있다.
 */
@Composable
fun MovableContent(modifier: Modifier = Modifier) {
    var isCondensedView by remember {
        mutableStateOf(true)
    }

    var profile by remember {
        mutableStateOf(
            Profile(
                username = "John Doe",
                pictureResId = R.drawable.living_room
            )
        )
    }
    // 일반 형태
    val movableUserName = remember {
        movableContentOf {
            Text(profile.username)
        }
    }
    // 전달해야 하는 파라미터가 있는 경우 사용
    // Modifier를 설정하면 Composition(크기 변경) 혹은 Recomposition(배경색 변경)이 발생할 수 있다.
    val movableProfileImage = remember {
        movableContentWithReceiverOf<Modifier> {
            ProfileImage(profile.pictureResId, modifier = this)
        }
    }
    // Scope이 필요한 경우 다음과 같이 사용할 수 있다. RowScope은 그냥 예시임
    val movableProfileImage2 = remember {
        movableContentOf<RowScope> {
            ProfileImage(profile.pictureResId)
        }
    }

    Column(modifier) {
        if (isCondensedView) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // key를 지정해도 효과가 없다.
//                key("profile-image") {
//                    ProfileImage(profile.pictureResId)
//                }
                movableProfileImage(
                    Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                movableUserName()
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                movableProfileImage(
                    Modifier.background(Color.Red)
                )
                Spacer(modifier = Modifier.height(16.dp))
                movableUserName()
            }
        }
        Button(
            onClick = {
                isCondensedView = !isCondensedView
            }
        ) {
            Text("Toggle")
        }
    }
}

@Composable
fun ProfileImage(
    pictureResId: Int,
    modifier: Modifier = Modifier,
) {
    // SideEffect는 Recomposition 성공한 이후 매번 호출된다.
    SideEffect {
        println("Function was called!")
    }
    Image(
        painter = painterResource(pictureResId),
        contentDescription = "profile pic",
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}
