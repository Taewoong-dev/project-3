package com.boost.issueTracker.ui.label

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boost.issueTracker.data.model.VO.Label
import com.boost.issueTracker.ui.components.LoadingIndicator
import com.boost.issueTracker.ui.components.NetworkErrorScreen
import android.graphics.Color as AndroidColor

class LabelListFragment : Fragment() {

    private val viewModel: LabelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LabelListScaffold(viewModel)
            }
        }
    }

    @Composable
    private fun LabelListScaffold(viewModel: LabelViewModel) {
        val labelUiState = viewModel.labelList.collectAsStateWithLifecycle()

        Scaffold(
            topBar = { LabelListTopBar() },
        ) { contentPadding ->
            when (val state = labelUiState.value) {
                is LabelUiState.Loading -> {
                    LoadingIndicator(contentPadding)
                }

                is LabelUiState.Success -> {
                    val labels = state.labelList
                    LabelListResultColumn(labels, contentPadding)
                }

                is LabelUiState.Error -> {
                    NetworkErrorScreen(contentPadding)
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun LabelListTopBar() {
        CenterAlignedTopAppBar(
            title = { Text("레이블") },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                }
            },
        )
    }

    @Composable
    private fun LabelListResultColumn(labels: List<Label>, contentPadding: PaddingValues) {
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(top = contentPadding.calculateTopPadding()),
        ) {
            repeat(labels.size) {
                LabelCardItem(labels[it])
            }
        }
    }

    @Composable
    private fun LabelCardItem(label: Label) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LabelNameButton(label)
                LabelMoreButton()
            }
            LabelDescriptionText(label.description)
        }
    }

    @Composable
    private fun LabelNameButton(label: Label) {
        val buttonColor = Color(AndroidColor.parseColor("#" + label.color)) // 16진수 문자열을 Color로 변환
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            )
        ) {
            Text(
                text = label.name,
                color = if (buttonColor.luminance() > 0.5) Color.Black else Color.White
            )
        }
    }

    @Composable
    private fun LabelMoreButton() {
        IconButton(onClick = onClickLabelMore()) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null
            )
        }
    }

    private fun onClickLabelMore() = {

    }

    @Composable
    private fun LabelDescriptionText(labelDescription: String?) {
        if (labelDescription.isNullOrBlank().not()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                text = labelDescription ?: ""
            )
        }
    }

    @Preview
    @Composable
    private fun LabelListPreview() {
        LabelListScaffold(viewModel)
    }
}
