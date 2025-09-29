package com.dothebestmayb.composestudy.performance_optimization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.dothebestmayb.composestudy.R
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

@Composable
fun ImageLoading(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(100) {
//            Image(
//                // painterResource는 내부 캐싱(LocalResourceIdCache)을 통해 빠르게 drawable 리소스를 불러온다.
//                // local disk가 아닌 network로부터 이미지를 불러오는 경우, 직접 캐싱 구조를 설계해야 한다.
//                // 캐싱 구조 설계 없이 사용하면, 이전에 가져왔던 이미지도 화면에 다시 표시될 때마다 네트워크 요청을 다시한다.
//                painter = painterResource(R.drawable.living_room),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillParentMaxWidth(),
//                contentScale = ContentScale.Crop,
//            )

            // coil 라이브러리를 이용해 이미지를 불러오면 내부에서 캐싱 처리를 해준다.
            // ref : https://github.com/coil-kt/coil
            // implementation("io.coil-kt.coil3:coil-compose:3.3.0")
            // implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
            AsyncImage(
                model = "https://picsum.photos/200/300",
                contentDescription = null,
                modifier = Modifier
                    .fillParentMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview
@Composable
private fun ImageLoadingPreview() {
    ComposeStudyTheme {
        ImageLoading()
    }
}
