package com.boost.issueTracker.ui.issue.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import coil3.compose.AsyncImage
import com.boost.issueTracker.data.model.VO.IssueComment
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.ui.components.LoadingIndicator
import com.boost.issueTracker.ui.components.NetworkErrorScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragmentIssue : Fragment() {
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentIssueArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DetailFragmentIssue", "onCreate: ${args.issueNum}")
        viewModel.getIssueDetail(args.issueNum)
        viewModel.getIssueComments(args.issueNum)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DetailScreen(viewModel)
            }
        }
    }

    @Composable
    fun DetailScreen(viewModel: DetailViewModel) {
        val snackBarHostState = SnackbarHostState()
        val issueDetailUiState = viewModel.issueDetail.collectAsStateWithLifecycle()
        val issueComments by viewModel.issueComments.collectAsStateWithLifecycle()
        val onSubmit: (String) -> Unit = { comment ->
            viewModel.createIssueComment(args.issueNum, comment)
        }

        Scaffold(
            topBar = { DetailTopBar() },
            bottomBar = { DetailBottomBar(onSubmit, snackBarHostState) },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) { innerPadding ->
            when (val state = issueDetailUiState.value) {
                is IssueDetailUiState.Loading -> {
                    LoadingIndicator(innerPadding)
                }

                is IssueDetailUiState.Success -> {
                    val issueDetail = state.issue
                    IssueDetailContainer(innerPadding, issueDetail, issueComments)
                }

                is IssueDetailUiState.Failure -> {
                    NetworkErrorScreen(innerPadding)
                }
            }
        }
    }

    @Composable
    private fun IssueDetailContainer(
        innerPadding: PaddingValues,
        issueDetail: IssueContent,
        issueComment: List<IssueComment>
    ) {
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            IssueTitleText(issueDetail.title, issueDetail.issueNumber)
            HorizontalDivider()
            IssueInfoContainer(issueDetail)
            HorizontalDivider()
            ContentAndCommentsContainer(issueDetail, issueComment)
        }
    }

    @Composable
    private fun ContentAndCommentsContainer(issueDetail: IssueContent, issueComment: List<IssueComment>) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
        ) {
            item {
                IssueContent(issueDetail)
            }
            item {
                Text(text = "코멘트", fontSize = 24.sp, modifier = Modifier.padding(4.dp))
            }

            items(issueComment.size) {
                IssueComments(issueComment[it])
                HorizontalDivider()
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun DetailTopBar() {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "뒤로 가기"
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "추가 메뉴"
                    )
                }
            },
            title = { }
        )
    }

    @Composable
    private fun IssueTitleText(title: String, issueNumber: Int) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 24.sp,
            text = title + " #${issueNumber}"
        )
    }

    @Composable
    private fun IssueInfoContainer(issueDetail: IssueContent) {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("${issueDetail.createdAt} 전에 ${issueDetail.writer}님이 작성했습니다.")
            Spacer(Modifier.size(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "이슈 정보"
                )
                Spacer(Modifier.size(8.dp))
                Text(if (issueDetail.state == "open") "열린 이슈" else "닫힌 이슈")
            }
            Spacer(Modifier.size(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "작성자"
                )
                Spacer(Modifier.size(8.dp))
                Text(issueDetail.writer)
            }
            Spacer(Modifier.size(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.AutoMirrored.Outlined.Label,
                    contentDescription = "레이블"
                )
                Spacer(Modifier.size(8.dp))
                val issueString =
                    if (issueDetail.labels.isNotEmpty()) issueDetail.labels.joinToString { it.name }
                    else "레이블 없음"
                Text(issueString)
            }
            Spacer(Modifier.size(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.Flag,
                    contentDescription = "마일스톤"
                )
                Spacer(Modifier.size(8.dp))
                Text(issueDetail.mileStone ?: "마일스톤 없음")
            }
        }
    }

    @Composable
    private fun IssueContent(issueDetail: IssueContent) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ProfileImage(issueDetail.avatarUrl)

            Spacer(Modifier.size(8.dp))
            ContentBody(issueDetail.createdAt, issueDetail.writer, issueDetail.content)

            Spacer(Modifier.size(8.dp))
            RightCornerButton({}, Icons.Default.MoreVert)
        }

        Spacer(Modifier.size(16.dp))
        HorizontalDivider(thickness = 2.dp)
    }


    @Composable
    private fun ProfileImage(avatarUrl: String) {
        AsyncImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            model = avatarUrl,
            contentDescription = "프로필 이미지"
        )
    }

    @Composable
    private fun ContentBody(createdAt: String, writer: String, content: String?) {
        Column(
            Modifier.fillMaxWidth(0.8f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(fontWeight = FontWeight.Bold, text = writer)
                Spacer(Modifier.size(8.dp))
                Text("$createdAt 전")
            }
            Spacer(Modifier.size(8.dp))
            Text(content ?: "내용 없음")
        }
    }

    @Composable
    private fun RightCornerButton(
        onClick: () -> Unit,
        iconVector: ImageVector
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = iconVector,
                contentDescription = "추가 기능"
            )
        }
    }

    @Composable
    private fun IssueComments(issueDetail: IssueComment) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ProfileImage(issueDetail.userImage)

            Spacer(Modifier.size(8.dp))
            ContentBody(issueDetail.createdAt, issueDetail.user, issueDetail.comment)

            Spacer(Modifier.size(8.dp))
            RightCornerButton({}, Icons.Outlined.AddReaction)
        }
    }

    @Composable
    private fun DetailBottomBar(onSubmit: (String) -> Unit, snackbarHostState: SnackbarHostState) {
        val focusManager = LocalFocusManager.current
        val coroutineScope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }
        val resetTextState : () -> Unit = {
            text = ""
        }

        BottomAppBar {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("내용을 입력하세요") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    onSendComment(text, coroutineScope, snackbarHostState, focusManager, onSubmit, resetTextState)
                }),
                trailingIcon = {
                    IconButton(onClick = {
                        onSendComment(text, coroutineScope, snackbarHostState, focusManager, onSubmit, resetTextState)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }

    private fun onSendComment(
        text: String,
        coroutineScope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
        focusManager: FocusManager,
        onSubmit: (String) -> Unit,
        textStateResetter: () -> Unit,
    ) {
        if (text.isBlank()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("내용이 비어있습니다.")
            }
            return
        }
        onSubmit(text)
        textStateResetter()
        focusManager.clearFocus()
        coroutineScope.launch {
            snackbarHostState.showSnackbar("등록이 완료되었습니다.")
        }
    }

    @Preview
    @Composable
    fun PreviewScaffold() {
        DetailScreen(viewModel)
    }
}