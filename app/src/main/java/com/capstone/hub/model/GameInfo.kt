package com.capstone.hub.model

import androidx.compose.ui.graphics.Color

data class GameInfo(
    val id: Int,
    val title: String,
    val developer: String,
    val description: String,
    val packageName: String,
    val accentColor: Color,
)

object GameCatalog {
    val games: List<GameInfo> = listOf(
        GameInfo(
            id = 1,
            title = "게임 1",
            developer = "팀원 A",
            description = "게임 설명을 여기에 입력하세요.",
            packageName = "com.capstone.game1",
            accentColor = Color(0xFF5C6BC0),
        ),
        GameInfo(
            id = 2,
            title = "게임 2",
            developer = "팀원 B",
            description = "게임 설명을 여기에 입력하세요.",
            packageName = "com.capstone.game2",
            accentColor = Color(0xFF26A69A),
        ),
        GameInfo(
            id = 3,
            title = "게임 3",
            developer = "팀원 C",
            description = "게임 설명을 여기에 입력하세요.",
            packageName = "com.capstone.game3",
            accentColor = Color(0xFFFF7043),
        ),
        GameInfo(
            id = 4,
            title = "게임 4",
            developer = "팀원 D",
            description = "게임 설명을 여기에 입력하세요.",
            packageName = "com.capstone.game4",
            accentColor = Color(0xFFAB47BC),
        ),
    )
}
