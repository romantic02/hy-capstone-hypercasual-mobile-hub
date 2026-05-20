package com.capstone.hub.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.hub.R
import com.capstone.hub.launcher.GameLauncher
import com.capstone.hub.launcher.LaunchResult
import com.capstone.hub.model.GameCatalog
import com.capstone.hub.model.GameInfo
import com.capstone.hub.ui.theme.HubColors
import com.capstone.hub.ui.theme.HubTypography
import kotlinx.coroutines.launch

@Suppress("SpellCheckingInspection")
private suspend fun SnackbarHostState.showHubInlineMessage(message: String) {
    showSnackbar(
        message = message,
        duration = SnackbarDuration.Short,
        withDismissAction = true,
    )
}

@Suppress("SpellCheckingInspection")
@Composable
private fun HubScreenScaffold(
    inlineMessageHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = HubColors.FrameYellow,
        snackbarHost = { SnackbarHost(hostState = inlineMessageHostState) },
        content = content,
    )
}

@Composable
fun HubScreen() {
    val context = LocalContext.current
    val games = remember { GameCatalog.games }
    val inlineMessageHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    HubScreenScaffold(inlineMessageHostState) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            HyperCasualHeader()
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(3.dp, HubColors.BorderBlack, RoundedCornerShape(36.dp))
                    .clip(RoundedCornerShape(36.dp))
                    .background(HubColors.PanelWhite),
                contentAlignment = Alignment.Center,
            ) {
                CenteredGameGrid(
                    games = games,
                    onGameClick = { game ->
                        when (GameLauncher.launch(context, game)) {
                            LaunchResult.Success -> Unit
                            LaunchResult.NotInstalled -> scope.launch {
                                inlineMessageHostState.showHubInlineMessage(
                                    context.getString(R.string.not_installed),
                                )
                            }
                            LaunchResult.Failed -> scope.launch {
                                inlineMessageHostState.showHubInlineMessage(
                                    context.getString(R.string.launch_failed),
                                )
                            }
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@Composable
private fun HyperCasualHeader() {
    val density = LocalDensity.current
    val shadowOffsetPx = with(density) { 4.dp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .drawBehind {
                val radius = size.height / 2f
                drawRoundRect(
                    color = HubColors.HeaderRedShadow,
                    topLeft = Offset(0f, shadowOffsetPx),
                    size = size,
                    cornerRadius = CornerRadius(radius, radius),
                )
            }
            .clip(RoundedCornerShape(50))
            .background(HubColors.HeaderRed)
            .padding(horizontal = 17.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
            Text(
                text = "HYPER CASUAL",
                style = TextStyle(
                    color = Color.White,
                    fontFamily = HubTypography.BagelFatOne,
                    fontSize = 20.3.sp,
                    letterSpacing = 0.92.sp,
                    lineHeight = 23.35.sp,
                    textAlign = TextAlign.Center,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
                ),
            )
    }
}

@Composable
private fun CenteredGameGrid(
    games: List<GameInfo>,
    onGameClick: (GameInfo) -> Unit,
) {
    val rows = games.chunked(2)
    val horizontalGap = 18.dp
    val verticalGap = 22.dp

    SubcomposeLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 36.dp),
    ) { constraints ->
        val maxWidthDp = constraints.maxWidth.toDp()
        val slotSize = ((maxWidthDp - horizontalGap) / 2).coerceIn(88.dp, 104.dp)

        val placeable = subcompose(rows to slotSize) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(verticalGap),
            ) {
                rows.forEach { rowGames ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = horizontalGap,
                            alignment = Alignment.CenterHorizontally,
                        ),
                        verticalAlignment = Alignment.Top,
                    ) {
                        rowGames.forEach { game ->
                            GameGridItem(
                                modifier = Modifier.weight(1f),
                                game = game,
                                iconSize = slotSize,
                                onClick = { onGameClick(game) },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "탭하여 게임 실행",
                    color = HubColors.BorderBlack.copy(alpha = 0.45f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }.first().measure(constraints)

        layout(constraints.maxWidth, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

@Composable
private fun GameGridItem(
    game: GameInfo,
    iconSize: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameSlotIcon(
            game = game,
            modifier = Modifier
                .width(iconSize)
                .aspectRatio(1f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        GameTitleText(title = game.title)
    }
}

@Composable
private fun GameTitleText(title: String) {
    val baseStyle = TextStyle(
        color = HubColors.BorderBlack,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        lineHeight = 14.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 2.dp)

    val lines = title.split("\n")
    if (lines.size <= 1) {
        FittingSingleLineTitle(
            text = title,
            baseStyle = baseStyle,
            modifier = modifier,
        )
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            lines.forEach { line ->
                FittingSingleLineTitle(
                    text = line,
                    baseStyle = baseStyle,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun FittingSingleLineTitle(
    text: String,
    baseStyle: TextStyle,
    modifier: Modifier = Modifier,
    maxFontSizeSp: Float = 12f,
    minFontSizeSp: Float = 9f,
) {
    var fontSizeSp by remember(text) { mutableFloatStateOf(maxFontSizeSp) }

    SubcomposeLayout(modifier = modifier) { constraints ->
        val maxWidthDp = constraints.maxWidth.toDp()

        val placeable = subcompose("${text}_${fontSizeSp}_${constraints.maxWidth}") {
            Text(
                text = text,
                modifier = Modifier.widthIn(max = maxWidthDp),
                style = baseStyle.copy(fontSize = fontSizeSp.sp),
                maxLines = 1,
                softWrap = false,
                textAlign = TextAlign.Center,
                onTextLayout = { result ->
                    if (result.hasVisualOverflow && fontSizeSp > minFontSizeSp) {
                        fontSizeSp = (fontSizeSp - 0.5f).coerceAtLeast(minFontSizeSp)
                    }
                },
            )
        }.first().measure(constraints)

        layout(constraints.maxWidth, placeable.height) {
            placeable.place((constraints.maxWidth - placeable.width) / 2, 0)
        }
    }
}

@Composable
private fun GameSlotIcon(
    game: GameInfo,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 4.dp, y = 4.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(HubColors.BorderBlack),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, HubColors.BorderBlack, RoundedCornerShape(24.dp))
                .background(game.accentColor),
            contentAlignment = Alignment.Center,
        ) {
            if (game.iconResId != null) {
                Image(
                    painter = painterResource(game.iconResId),
                    contentDescription = game.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text(
                    text = game.id.toString(),
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
