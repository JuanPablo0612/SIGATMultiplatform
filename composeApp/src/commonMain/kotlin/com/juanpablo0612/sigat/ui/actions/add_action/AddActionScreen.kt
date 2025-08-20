package com.juanpablo0612.sigat.ui.actions.add_action

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.juanpablo0612.sigat.domain.model.Obligation
import com.juanpablo0612.sigat.ui.camera_launcher.isCameraLauncherAvailable
import com.juanpablo0612.sigat.ui.camera_launcher.launchCamera
import com.juanpablo0612.sigat.ui.components.DatePickerTextField
import com.juanpablo0612.sigat.ui.components.LoadingContent
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_add_action
import sigat.composeapp.generated.resources.date_label
import sigat.composeapp.generated.resources.description_label
import sigat.composeapp.generated.resources.image_source_camera
import sigat.composeapp.generated.resources.image_source_gallery
import sigat.composeapp.generated.resources.images_label
import sigat.composeapp.generated.resources.obligation_error
import sigat.composeapp.generated.resources.obligation_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActionScreen(
    viewModel: AddActionViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val screenRows = remember(windowSize.widthSizeClass) {
        if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) 2 else 1
    }
    val coroutineScope = rememberCoroutineScope()

    val datePickerState = rememberDatePickerState()
    val galleryLauncher =
        rememberFilePickerLauncher(type = FileKitType.Image, mode = FileKitMode.Multiple()) {
            it?.let {
                viewModel.onAddImages(it)
            }
        }

    if (uiState.saved) {
        LaunchedEffect(Unit) { onNavigateBack() }
    }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            viewModel.onTimestampChange(it)
        }
    }

    Scaffold(topBar = { AddActionTopAppBar(onBack = onNavigateBack) }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (screenRows > 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    ObligationSelector(
                        obligations = uiState.obligations,
                        expandObligationList = uiState.expandObligationList,
                        alwaysExpand = true,
                        onExpandObligationListChange = viewModel::onExpandObligationListChange,
                        selectedObligation = uiState.obligation,
                        onObligationChange = viewModel::onObligationChange,
                        modifier = Modifier.weight(0.5f)
                    )

                    ActionDetails(
                        uiState = uiState,
                        datePickerState = datePickerState,
                        onDescriptionChange = viewModel::onDescriptionChange,
                        onCameraClick = {
                            coroutineScope.launch {
                                val file = launchCamera()
                                file?.let {
                                    viewModel.onAddImages(listOf(file))
                                }
                            }
                        },
                        onGalleryClick = { galleryLauncher.launch() },
                        onAdd = viewModel::onAdd,
                        onDeleteImage = viewModel::onDeleteImage,
                        modifier = Modifier.weight(0.5f)
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    ObligationSelector(
                        obligations = uiState.obligations,
                        expandObligationList = uiState.expandObligationList,
                        alwaysExpand = false,
                        onExpandObligationListChange = viewModel::onExpandObligationListChange,
                        selectedObligation = uiState.obligation,
                        onObligationChange = viewModel::onObligationChange,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ActionDetails(
                        uiState = uiState,
                        datePickerState = datePickerState,
                        onDescriptionChange = viewModel::onDescriptionChange,
                        onCameraClick = {
                            coroutineScope.launch {
                                val file = launchCamera()
                                file?.let {
                                    viewModel.onAddImages(listOf(file))
                                }
                            }
                        },
                        onGalleryClick = { galleryLauncher.launch() },
                        onAdd = viewModel::onAdd,
                        onDeleteImage = viewModel::onDeleteImage,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                }
            }
            if (uiState.loading) {
                LoadingContent(modifier = Modifier.matchParentSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddActionTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(Res.string.button_add_action)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
private fun ObligationSelector(
    obligations: List<Obligation>,
    expandObligationList: Boolean,
    alwaysExpand: Boolean,
    onExpandObligationListChange: (Boolean) -> Unit,
    selectedObligation: Obligation?,
    onObligationChange: (Obligation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        Row {
            Text(
                text = stringResource(Res.string.obligation_label),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            if (!alwaysExpand) {
                IconButton(onClick = { onExpandObligationListChange(!expandObligationList) }) {
                    val icon =
                        if (expandObligationList) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        }

        AnimatedVisibility(visible = if (alwaysExpand) true else expandObligationList) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(obligations, key = { it.id }) { obligation ->
                    if (obligation == selectedObligation) {
                        SelectedObligationCard(obligation) {
                            onObligationChange(obligation)
                            onExpandObligationListChange(false)
                        }
                    } else {
                        UnselectedObligationCard(obligation) {
                            onObligationChange(obligation)
                            onExpandObligationListChange(false)
                        }
                    }
                }
            }
        }

        if (!alwaysExpand) {
            Text(
                text = selectedObligation?.name ?: stringResource(Res.string.obligation_error),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionDetails(
    uiState: AddActionUiState,
    datePickerState: DatePickerState,
    onDescriptionChange: (String) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onAdd: () -> Unit,
    onDeleteImage: (PlatformFile) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = { Text(text = stringResource(Res.string.description_label)) },
            isError = !uiState.validDescription,
            enabled = !uiState.loading,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerTextField(
            label = stringResource(Res.string.date_label),
            state = datePickerState,
            isError = !uiState.validTimestamp,
            enabled = !uiState.loading
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.images_label),
                style = MaterialTheme.typography.titleLarge
            )

            if (isCameraLauncherAvailable) {
                AssistChip(
                    onClick = onCameraClick,
                    label = { Text(text = stringResource(Res.string.image_source_camera)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
                    }
                )
            }

            AssistChip(
                onClick = onGalleryClick,
                label = { Text(text = stringResource(Res.string.image_source_gallery)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Photo, contentDescription = null)
                }
            )
        }

        uiState.images.forEachIndexed { index, image ->
            SelectedImageItem(
                image = image,
                onDeleteClick = { onDeleteImage(image) },
                number = index + 1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        val buttonEnabled =
            uiState.obligation != null && uiState.validDescription && !uiState.loading && uiState.timestamp != null

        Button(
            onClick = onAdd,
            enabled = buttonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(Res.string.button_add_action))
        }
    }
}

@Composable
fun SelectedObligationCard(obligation: Obligation, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = obligation.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun UnselectedObligationCard(obligation: Obligation, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = obligation.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SelectedImageItem(
    image: PlatformFile,
    number: Int,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .size(size = 50.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = number.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}