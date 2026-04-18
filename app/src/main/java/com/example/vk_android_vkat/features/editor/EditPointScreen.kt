package com.example.vk_android_vkat.features.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.vk_android_vkat.R
import com.example.vk_android_vkat.common.saveImageToInternalStorage
import com.example.vk_android_vkat.features.editor.domain.RoutePointModel
import com.example.vk_android_vkat.features.navigation.Editor
import java.io.File

@Composable
fun EditPointScreen(
    state: EditorState,
    navController: NavHostController,
    onEvent: (EditorEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val draft = state.draftPoint ?: return

    var routeNamePoint by rememberSaveable(draft.latitude, draft.longitude) {
        mutableStateOf(draft.pointName)
    }
    var routeDescriptionPoint by rememberSaveable(draft.latitude, draft.longitude) {
        mutableStateOf(draft.pointDescription)
    }
    var selectedImageUriStringPoint by rememberSaveable(draft.latitude, draft.longitude) {
        mutableStateOf(draft.photoUri?.toString())
    }

    val selectedImageUriPoint = selectedImageUriStringPoint?.let(Uri::parse)

    val isFormValid =
        routeNamePoint.trim().isNotEmpty() &&
                routeDescriptionPoint.trim().isNotEmpty() &&
                selectedImageUriPoint != null

    val context = LocalContext.current
    val galleryLauncherPoint = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = saveImageToInternalStorage(context, it)
            selectedImageUriStringPoint = savedPath
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.route_point_selection),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable { galleryLauncherPoint.launch("image/*") },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (selectedImageUriStringPoint != null) {
                        Image(
                            painter = rememberAsyncImagePainter(File(selectedImageUriStringPoint)),
                            contentDescription = stringResource(R.string.selected_point_photo),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = stringResource(R.string.attach_photo),
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.attach_photo),
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = routeNamePoint,
                onValueChange = { routeNamePoint = it },
                label = { Text(stringResource(R.string.point_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = routeDescriptionPoint,
                onValueChange = { routeDescriptionPoint = it },
                label = { Text(stringResource(R.string.point_description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Button(
                onClick = {
                    val finalPoint = draft.copy(
                        pointName = routeNamePoint.trim(),
                        pointDescription = routeDescriptionPoint.trim(),
                        photoUri = selectedImageUriStringPoint
                    )

                    onEvent(EditorEvent.ConfirmDraftPoint(finalPoint))
                    navController.popBackStack(route = Editor, inclusive = false)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text(stringResource(R.string.save_point))
            }
        }
    }
}