package com.hyzoka.xperience.ui.main

import android.app.PictureInPictureUiState
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.commandiron.expandable_horizontal_pager.ExpandableHorizontalPager
import com.hyzoka.xperience.R
import com.hyzoka.xperience.databinding.FragmentPositionBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.hyzoka.xperience.utils.networkState.PositionState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PositionFragment : Fragment() {

    companion object {
        fun newInstance() = PositionFragment()
    }

    private val positionViewModel: PositionViewModel by viewModels()

    private var _binding: FragmentPositionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPositionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        lifecycleScope.launch {
            positionViewModel.positions.collect { uiState ->
                setupComposeView(uiState)
            }
        }
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positionViewModel.getPositionsData()

    }

    @OptIn(ExperimentalPagerApi::class)
    private fun setupComposeView(uiState: PositionState) {
        binding.composeView.apply {
            setContent {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ExpandableHorizontalPager(
                        count = uiState.data.size,
                        initialHorizontalPadding = 64.dp,
                        initialWidth = 240.dp,
                        targetWidth = maxWidth,
                        mainContent = { page, isExpanded ->
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.data[page]?.position?.image)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight
                            )
                        },
                        overMainContentCollapsed = { page ->
                            OverMainContent(
                                title = "Details",
                                imageVector = Icons.Default.KeyboardArrowDown
                            )
                        },
                        overMainContentExpanded = { page ->
                            OverMainContent(
                                title = "Close",
                                imageVector = Icons.Default.KeyboardArrowUp,
                                iconOnTop = true
                            )
                        },
                        hiddenContent = { page ->
                            uiState.data[page]?.let {
                                HiddenContent(
                                    title = it.id,
                                    overview = it.position.description
                                )
                            }
                        },
                        onTransform = { isExpanded -> }
                    )
                }
            }
        }
    }

    @Composable
    fun OverMainContent(
        title: String,
        imageVector: ImageVector,
        iconOnTop: Boolean = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.65f)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            if(iconOnTop) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.White
            )
            if(!iconOnTop) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    @Composable
    fun HiddenContent(title: String, overview: String) {
        Column(Modifier.padding(16.dp)) {
            Text(text = title)
            Spacer(Modifier.height(8.dp))
            Text(text = overview)
        }
    }

}