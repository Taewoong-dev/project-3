package com.boost.issueTracker.ui.milestone.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.boost.issueTracker.R
import com.boost.issueTracker.data.model.VO.Milestone
import com.boost.issueTracker.ui.milestone.uistate.MilestoneUiState
import com.boost.issueTracker.ui.milestone.viewmodel.MilestoneViewModel
import com.boost.issueTracker.ui.milestone.listener.HideBotNavListener
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MileStoneScreen(
    navController: NavController,
    viewmodel: MilestoneViewModel,
    hideBotNavListener: HideBotNavListener
) {
    val milestoneUiState = viewmodel.milestoneList.collectAsStateWithLifecycle()
    val milestoneCreationUiState by viewmodel.creationMilestoneEvent.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    if (milestoneCreationUiState is MilestoneUiState.Completion) {
        viewmodel.loadMilestoneList()
        LaunchedEffect(milestoneCreationUiState) {
            scope.launch {
                listState.scrollToItem(0) // 상태 변화 시 스크롤을 맨 위로 이동
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ),
                title = {
                    Text(
                        text = "마일스톤",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        hideBotNavListener.hide()
                        navController.navigate("milestone_create")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Create new milestone"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        when (val state = milestoneUiState.value) {
            is MilestoneUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MilestoneUiState.Success -> {
                MileStoneLazyGrid(state.milestoneList, listState, innerPadding)
            }

            is MilestoneUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Error",
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Text("네트워크 에러")
                }
            }
            else ->{}
        }
    }
}

@Composable
fun MileStoneLazyGrid(
    milestones: List<Milestone>,
    listState: LazyListState,
    padding: PaddingValues
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        content = {
            items(milestones.size) { index ->
                IndividualMilestone(milestones[index])
            }
        },
        contentPadding = padding
    )
}

@Composable
fun IndividualMilestone(milestone: Milestone) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(0.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)  // 이를 제외할 경우 내부 row의 fillMaxWidth가 Card의 전체 width를 차지하게 됨
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // 첫 번째 줄: 제목과 진행률
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = milestone.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    val progress: Float =
                        if (milestone.closedIssues == 0) 0f
                        else (milestone.closedIssues.toFloat() / (milestone.openIssues + milestone.closedIssues)) * 100f
                    Text(
                        text = "${progress.toInt()}%", // 진행률을 퍼센트로 표시
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                // 두 번째 줄: 설명과 완료일
                Text(
                    text = milestone.description ?: "설명 없음",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                // 세 번째 줄: 완료일
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "완료일 ${milestone.dueOn?.substring(0, 10) ?: "YYYY-MM-DD"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                // 네 번째 줄: 열린 이슈와 닫힌 이슈
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_error_24),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "열린 이슈 ${milestone.openIssues}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_archive_24),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "닫힌 이슈 ${milestone.closedIssues}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
            LabelMoreButton(Modifier.weight(0.1f))
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
}

@Composable
private fun LabelMoreButton(modifier: Modifier) {
    IconButton(
        onClick = { },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
    }
}
