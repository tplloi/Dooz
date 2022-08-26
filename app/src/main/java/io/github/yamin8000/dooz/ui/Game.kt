/*
 *     Dooz
 *     Game.kt Created by Yamin Siahmargooei at 2022/8/25
 *     This file is part of Dooz.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Dooz is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Dooz is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Dooz.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.dooz.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orhanobut.logger.Logger
import io.github.yamin8000.dooz.ui.game.DoozCell
import io.github.yamin8000.dooz.ui.theme.DoozTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HomeContent(
    navController: NavController? = null
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val gameState = rememberHomeState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val boxPadding = 16.dp
    val boxSize = screenWidth - (2 * boxPadding.value).dp
    val boxItemSize = (boxSize.value / gameState.gameSize.value).dp

    DoozTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = gameState.currentPlayer.value.name)
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(boxPadding)
                        .size(boxSize),
                    columns = GridCells.Fixed(gameState.gameSize.value),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    userScrollEnabled = false
                ) {
                    gameState.gameCells.value.forEachIndexed { rowIndex, row ->
                        itemsIndexed(row) { columnIndex, cell ->
                            DoozItem(
                                state = gameState,
                                itemSize = boxItemSize,
                                doozCell = cell,
                                onClick = {
                                    gameState.playItem(cell)
                                    //gameState.changePlayer()
                                    //Logger.d(gameState.currentPlayer)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DoozItem(
    state: GameState,
    itemSize: Dp = 50.dp,
    doozCell: DoozCell = DoozCell(0, 0),
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(itemSize)
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            doozCell.owner?.let { doozCellOwner ->
                Box(
                    modifier = Modifier
                        .size((itemSize.value / 2).dp)
                        .clip(state.getOwnerShape(doozCellOwner))
                        .background(MaterialTheme.colorScheme.onSecondary),
                )
            }
        }
    }
}