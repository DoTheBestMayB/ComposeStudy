package com.dothebestmayb.composestudy.basic_modifier.self_study

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID
import kotlin.math.roundToInt


enum class SwipeToReplyValue {
    RESTING, REPLYING
}

@Composable
fun Message(
    messageInfo: MessageInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = if (messageInfo.sender == MessageSender.OTHER) {
            Arrangement.Start
        } else {
            Arrangement.End
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (messageInfo.sender == MessageSender.OTHER) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color(0xFFE87457),
                modifier = Modifier
                    .size(20.dp)
            )
            Text(
                text = messageInfo.message,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFE87457))
                    .padding(horizontal = 8.dp)

            )
        } else {
            Text(
                text = messageInfo.message,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFE87457))
                    .padding(horizontal = 8.dp)

            )
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color(0xFFE87457),
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}

@Composable
fun WipeToReplyRoot(
    modifier: Modifier = Modifier,
    viewModel: WipeToReplyViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WipeToReply(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WipeToReply(
    state: WipeToReplyState,
    onAction: (WipeToReplyAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var headerHeight by remember { mutableIntStateOf(0) }

    val focusRequester = remember {
        FocusRequester()
    }

    val scrollState = rememberLazyListState()

    val density = LocalDensity.current
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val messageDragState = remember {
        AnchoredDraggableState(
            initialValue = SwipeToReplyValue.RESTING,
            anchors = DraggableAnchors {
                val replyOffset = with(density) {
                    48.dp.toPx()
                }
                SwipeToReplyValue.RESTING at 0f
                SwipeToReplyValue.REPLYING at replyOffset
            },
            positionalThreshold = { density ->
                density * 0.5f
            },
            velocityThreshold = {
                with(density) {
                    100.dp.toPx()
                }
            },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
        )
    }

    val messageOverscroll = ScrollableDefaults.overscrollEffect()

    LaunchedEffect(messageDragState) {
        snapshotFlow { messageDragState.settledValue }
            .collectLatest {
                if (it == SwipeToReplyValue.REPLYING) {
                    focusRequester.requestFocus()
                    delay(100)
                    messageDragState.animateTo(
                        SwipeToReplyValue.RESTING
                    )
                }
            }
    }

    LaunchedEffect(state.isScrollToBottom) {
        if (state.isScrollToBottom) {
            scrollState.scrollToItem(state.messages.lastIndex)
            onAction(WipeToReplyAction.OnScrollBottomConsumed)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 18.dp)
                .padding(WindowInsets.ime.asPaddingValues())
                .padding(bottom = with(LocalDensity.current) {
                    headerHeight.toDp() + 24.dp
                }),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = state.messages, key = { it.messageId }) {
                Message(
                    messageInfo = it,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .anchoredDraggable(
                            state = messageDragState,
                            orientation = Orientation.Horizontal,
                            overscrollEffect = messageOverscroll,
                        )
                        .overscroll(messageOverscroll)
                        .offset {
                            IntOffset(
                                x = messageDragState
                                    .requireOffset()
                                    .roundToInt(),
                                y = 0,
                            )
                        }
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .onGloballyPositioned { coordinates ->
                    headerHeight = coordinates.size.height
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = state.replyInfo.message,
                    onValueChange = { newText: String ->
                        onAction(WipeToReplyAction.OnReplyTextChanged(newText))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                    )
                )
                IconButton(
                    onClick = {
                        focusRequester.freeFocus()
                        onAction(WipeToReplyAction.OnSendMessageClick)
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Send,
                        contentDescription = "Send Message."
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WipeToReplyDemoPreview() {
    ComposeStudyTheme {
        WipeToReply(
            state = WipeToReplyState(
                replyInfo = MessageInfo(
                    message = "",
                    sender = MessageSender.ME,
                ),
                messages = listOf(
                    MessageInfo(
                        messageId = UUID.randomUUID(),
                        message = "necessitatibus",
                        replyTargetMessageInfo = null,
                        sender = MessageSender.OTHER,
                    )
                ),
            ),
            onAction = {},
        )
    }
}
