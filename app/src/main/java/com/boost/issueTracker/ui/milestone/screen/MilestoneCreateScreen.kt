package com.boost.issueTracker.ui.milestone.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.boost.issueTracker.ui.milestone.uistate.MilestoneUiState
import com.boost.issueTracker.ui.milestone.viewmodel.MilestoneViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneCreateScreen(
    navController: NavController,
    viewModel: MilestoneViewModel,
    showBotNavListener: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var titleText by remember { mutableStateOf("") }
    var contentText = ""
    var selectedDate = ""
    val titleTextHoist: (String) -> Unit = { titleText = it }
    val contentTextHoist: (String) -> Unit = { contentText = it }
    val selectedDataHoist: (String) -> Unit = { selectedDate = it }

    val creationState by viewModel.creationMilestoneEvent.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    when (creationState) {
        is MilestoneUiState.Success -> {
            viewModel.backToMilestoneList()
            showBotNavListener()
            navController.popBackStack("milestone_list", false)
        }
        is MilestoneUiState.Error -> {
            LaunchedEffect(creationState) {
                snackbarHostState.showSnackbar(
                    message = "마일스톤 생성을 실패했습니다.",
                    duration = SnackbarDuration.Short,
                    actionLabel = "확인")
            }
        }
        else -> {}
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "새로운 마일스톤 ") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            showBotNavListener()
                            navController.navigateUp()

                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (titleText == "") return@IconButton
                            viewModel.createMilestone(titleText, contentText, selectedDate)
                        },
                        colors = if (titleText == "") {
                            IconButtonColors(
                                contentColor = Color.LightGray,
                                containerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        } else {
                            IconButtonColors(
                                contentColor = Color.Black,
                                containerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        },
                    ) {
                        Icon(Icons.Default.Done, contentDescription = "Done")
                    }
                },
                modifier = Modifier.clickable { focusManager.clearFocus() }
            )
        },
    ) { innerPadding ->
        MilestoneEditScreen(
            innerPadding,
            focusManager,
            titleTextHoist,
            contentTextHoist,
            selectedDataHoist
        )
    }
}

@Composable
fun MilestoneEditScreen(
    innerPadding: PaddingValues,
    focusManager: FocusManager,
    titleTextHoist: (String) -> Unit,
    contentTextHoist: (String) -> Unit,
    selectedDataHoist: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
            .clickable(
                onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        MilestoneTextField(label = "제목", placeHolder = "필수 입력", titleTextHoist)
        Spacer(modifier = Modifier.height(8.dp))
        MilestoneTextField(label = "내용", placeHolder = "선택", contentTextHoist)
        Spacer(modifier = Modifier.height(8.dp))
        MilestoneDateField(selectedDataHoist)
    }
}

@Composable
private fun MilestoneTextField(
    label: String,
    placeHolder: String,
    inputTextHoist: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    LaunchedEffect(text) {
        snapshotFlow { text }.collectLatest {
            inputTextHoist(it)
        }
    }
    TextField(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        placeholder = { Text(placeHolder) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MilestoneDateField(selectedDataHoist: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= System.currentTimeMillis()
        }
    })
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToIso8601(it)
    } ?: ""
    LaunchedEffect(datePickerState) {
        snapshotFlow { datePickerState.selectedDateMillis }.collectLatest { selectedDateMillis ->
            selectedDateMillis?.let {
                selectedDataHoist(convertMillisToIso8601(it))
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = !showDatePicker }
    ) {
        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth(),
            value = if (selectedDate.lastIndex < 9) selectedDate else selectedDate.substring(0, 10),
            onValueChange = { },
            label = { Text("완료일") },
            placeholder = { Text("선택") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            readOnly = true
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = !showDatePicker },
                alignment = Alignment.TopStart,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 60.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        title = {
                            Text(
                                text = "마일스톤 완료 날짜",
                                modifier = Modifier
                                    .padding(
                                        PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)
                                    ),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        headline = {
                            Text(
                                text = "날짜를 선택하세요",
                                modifier = Modifier.padding(
                                    PaddingValues(start = 24.dp, end = 12.dp, bottom = 12.dp)
                                ),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    )
                }
            }
        }
    }
}

fun convertMillisToIso8601(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val formatter = DateTimeFormatter.ISO_INSTANT
    return formatter.format(instant)
}