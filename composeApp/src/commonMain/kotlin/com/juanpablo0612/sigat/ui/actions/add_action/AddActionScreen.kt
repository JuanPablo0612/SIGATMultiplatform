package com.juanpablo0612.sigat.ui.actions.add_action

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.juanpablo0612.sigat.domain.model.Obligation
import com.juanpablo0612.sigat.utils.timestampToText
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_add_action
import sigat.composeapp.generated.resources.button_add_image
import sigat.composeapp.generated.resources.button_select
import sigat.composeapp.generated.resources.date_label
import sigat.composeapp.generated.resources.description_label
import sigat.composeapp.generated.resources.image_source_camera
import sigat.composeapp.generated.resources.image_source_gallery
import sigat.composeapp.generated.resources.images_label
import sigat.composeapp.generated.resources.obligation_label
import sigat.composeapp.generated.resources.select_image_source

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActionScreen(viewModel: AddActionViewModel = koinInject(), onNavigateBack: () -> Unit) {
    val uiState = viewModel.uiState
    val datePickerState = rememberDatePickerState()
    val sheetState = rememberModalBottomSheetState()
    val imageLauncher =
        rememberFilePickerLauncher(type = FileKitType.Image, mode = FileKitMode.Multiple()) {
            it?.let {
                viewModel.onImageSourceSelectorVisibilityChange(false)
                viewModel.onAddImages(it)
            }
        }

    Scaffold(topBar = { AddActionTopAppBar(onBack = onNavigateBack) }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ObligationSelector(
                obligations = uiState.obligations,
                expandObligationList = uiState.expandObligationList,
                onExpandObligationListChange = viewModel::onExpandObligationListChange,
                selectedObligation = uiState.obligation,
                onObligationChange = viewModel::onObligationChange
            )

            TextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text(text = stringResource(Res.string.description_label)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                },
                isError = !uiState.validDescription,
                enabled = !uiState.loading,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.showDatePicker) {
                DatePickerDialog(
                    state = datePickerState,
                    onVisibilityChange = viewModel::onDatePickerVisibilityChange,
                    onSelect = viewModel::onTimestampChange
                )
            }

            Text(
                text = stringResource(Res.string.date_label),
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.timestamp != null) {
                    Text(
                        text = timestampToText(uiState.timestamp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.button_select),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                IconButton(onClick = { viewModel.onDatePickerVisibilityChange(true) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
            }

            Text(
                text = stringResource(Res.string.images_label),
                style = MaterialTheme.typography.titleLarge
            )

            SelectedImageList(
                images = uiState.images,
                onAddClick = { viewModel.onImageSourceSelectorVisibilityChange(true) },
                onDeleteClick = viewModel::onDeleteImage,
                modifier = Modifier.fillMaxWidth()
            )

            val buttonEnabled =
                uiState.obligation != null && uiState.validDescription && !uiState.loading && uiState.timestamp != null

            Button(
                onClick = viewModel::onAdd,
                enabled = buttonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(Res.string.button_add_action))
                }
            }
        }

        if (uiState.showImageSourceSelector) {
            ImageSourceSelector(
                state = sheetState,
                onVisibilityChange = viewModel::onImageSourceSelectorVisibilityChange,
                onGalleryClick = {
                    imageLauncher.launch()
                },
                onCameraClick = {

                }
            )
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
    onExpandObligationListChange: (Boolean) -> Unit,
    selectedObligation: Obligation?,
    onObligationChange: (Obligation) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DropdownMenu(
            expanded = expandObligationList,
            onDismissRequest = { onExpandObligationListChange(false) },
            modifier = Modifier.weight(1f)
        ) {
            obligations.forEach { obligation ->
                Column {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${obligation.number}. ${obligation.name}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            onObligationChange(obligation)
                            onExpandObligationListChange(false)
                        }
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    HorizontalDivider(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }

        val fontStyle =
            if (selectedObligation == null) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge

        Text(
            text = selectedObligation?.name ?: stringResource(Res.string.obligation_label),
            style = fontStyle,
            modifier = Modifier.weight(1.0f)
        )

        IconButton(onClick = { onExpandObligationListChange(!expandObligationList) }) {
            val icon =
                if (expandObligationList) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    state: DatePickerState,
    onVisibilityChange: (Boolean) -> Unit,
    onSelect: (timestamp: Long) -> Unit
) {
    val confirmButtonEnabled by remember {
        derivedStateOf { state.selectedDateMillis != null }
    }

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = { onVisibilityChange(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    onSelect(state.selectedDateMillis!!)
                    onVisibilityChange(false)
                },
                enabled = confirmButtonEnabled
            ) {
                Text(text = stringResource(Res.string.button_select))
            }
        }
    ) {
        DatePicker(state = state, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun SelectedImageList(
    images: List<PlatformFile>,
    onAddClick: () -> Unit,
    onDeleteClick: (uri: PlatformFile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            AddImageButton(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        itemsIndexed(images, key = { _, image -> image.toString() }) { index, image ->
            SelectedImageItem(
                image = image,
                onDeleteClick = { onDeleteClick(image) },
                number = index + 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem()
                    .padding(bottom = 8.dp)
            )
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourceSelector(
    state: SheetState,
    onVisibilityChange: (Boolean) -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onVisibilityChange(false) },
        sheetState = state,
        modifier = Modifier.wrapContentHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(Res.string.select_image_source))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ImageSourceOption(
                    icon = Icons.Default.Photo,
                    name = stringResource(Res.string.image_source_gallery),
                    onClick = onGalleryClick
                )

                ImageSourceOption(
                    icon = Icons.Default.CameraAlt,
                    name = stringResource(Res.string.image_source_camera),
                    onClick = onCameraClick
                )
            }
        }
    }
}

@Composable
private fun ImageSourceOption(icon: ImageVector, name: String, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = name)
        }
    }
}

@Composable
fun AddImageButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = stringResource(Res.string.button_add_image))
    }
}