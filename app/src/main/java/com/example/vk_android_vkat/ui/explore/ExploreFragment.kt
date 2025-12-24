package com.example.vk_android_vkat.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vk_android_vkat.databinding.FragmentExploreBinding
import com.example.vk_android_vkat.domain.model.RouteUi
import com.example.vk_android_vkat.mock_data.mockRoutes

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exploreViewModel =
            ViewModelProvider(this).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textHome
        exploreViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return ComposeView(requireContext()).apply {
            setContent {
                MainScreen(exploreViewModel)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Composable
    fun MainScreen(viewModel: ExploreViewModel) {
        Scaffold(
            topBar = { ToolBar() },
        ){ innerPadding ->
            ScrollContent(
                innerPadding = innerPadding,
                routes = mockRoutes
                )
        }
    }

    @Composable
    fun ToolBar() {
        LazyRow(
            modifier = Modifier.
            padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                AssistChip(
                    onClick = { },
                    label = { Text("Рядом") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                    }
                )
            }
            item { AssistChip(onClick = { }, label = { Text("Цель") }) }
            item { AssistChip(onClick = { }, label = { Text("Местность") }) }
            item { AssistChip(onClick = { }, label = { Text("Время суток") }) }
            item { AssistChip(onClick = { }, label = { Text("Длительность") }) }
        }
    }

//    @Preview(showBackground = true)
//    @Composable
//    fun AppBarPreview(){
//        ToolBar()
//    }

    @Composable
    fun ScrollContent(
        innerPadding: PaddingValues,
        routes: List<RouteUi> = mockRoutes,
        onLoadNext: () -> Unit = {},
    ) {
        val bottomNavHeight = 56.dp
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + bottomNavHeight)
        ) {
            items(routes, key = { it.id }){ route ->
                RouteCard(route = route)
            }
        }
//
//        item {
//            LaunchedEffect(Unit) {
//                onLoadNext()
//            }
//        }
    }

}