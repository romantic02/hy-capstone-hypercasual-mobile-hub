package com.capstone.hub.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.capstone.hub.R
import com.capstone.hub.ui.theme.HubColors

data class GameInfo(
    val id: Int,
    val title: String,
    val developer: String,
    val description: String,
    val packageName: String,
    val accentColor: Color,
    @DrawableRes val iconResId: Int? = null,
)

object GameCatalog {
    val games: List<GameInfo> = listOf(
        GameInfo(
            id = 1,
            title = "Tap Blast:\nDrop & Merge",
            developer = "본인 (테스트)",
            description = "탭·드롭·머지 하이퍼캐주얼",
            packageName = "com.tapblast.dropmerge",
            accentColor = HubColors.Slot1,
            iconResId = R.drawable.ic_game_tap_blast,
        ),
        GameInfo(
            id = 2,
            title = "Chrono Cat",
            developer = "팀원",
            description = "Chrono Cat",
            packageName = "com.hyhyper.chronocat",
            accentColor = HubColors.Slot2,
            iconResId = R.drawable.ic_game_slot2,
        ),
        GameInfo(
            id = 3,
            title = "Slime Dash 3D",
            developer = "팀원",
            description = "Slime Dash 3D",
            packageName = "com.DefaultCompany.SlimeDash3D",
            accentColor = HubColors.Slot3,
            iconResId = R.drawable.ic_game_slot3,
        ),
        GameInfo(
            id = 4,
            title = "Sword Rush:\nHold Action",
            developer = "팀원",
            description = "Sword Rush: Hold Action",
            packageName = "com.MySWCapstone.Sword_Rush",
            accentColor = HubColors.Slot4,
            iconResId = R.drawable.ic_game_slot4,
        ),
    )
}
