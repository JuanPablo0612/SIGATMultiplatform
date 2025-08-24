package com.juanpablo0612.sigat.ui.actions.action_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.theme.Dimens
import coil3.compose.AsyncImage
import com.juanpablo0612.sigat.domain.model.Action
import com.juanpablo0612.sigat.ui.components.LoadingContent
import com.juanpablo0612.sigat.utils.timestampToDayMonthYearFormat
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_add_action
import sigat.composeapp.generated.resources.content_description_action_image

@Composable
fun ActionListScreen(
    viewModel: ActionListViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onAddActionClick: () -> Unit
) {
    val uiState = viewModel.uiState
    val cols = remember(windowSize.widthSizeClass) {
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> GridCells.Fixed(1)
            WindowWidthSizeClass.Medium -> GridCells.Fixed(2)
            WindowWidthSizeClass.Expanded -> GridCells.Adaptive(300.dp)
            else -> GridCells.Fixed(1)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddActionClick) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(Res.string.button_add_action)
                )
            }
        },
        topBar = {
            ActionListTopAppBar()
        }
    ) { innerPadding ->
        if (!uiState.loading) {
            LazyVerticalGrid(
                columns = cols,
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
            ) {
                items(uiState.actions, key = { it.id }) {
                    ActionCard(action = it, modifier = Modifier.fillMaxWidth())
                }
            }
        } else {
            LoadingContent(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}

@Composable
private fun ActionCard(action: Action, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraSmall)
        ) {
            Text(
                text = timestampToDayMonthYearFormat(action.timestamp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = action.description, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)) {
                action.images.forEach {
                    AsyncImage(
                        model = it,
                        contentDescription = stringResource(Res.string.content_description_action_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.width(50.dp).height(50.dp)
                    )
                }
            }
        }
    }
}