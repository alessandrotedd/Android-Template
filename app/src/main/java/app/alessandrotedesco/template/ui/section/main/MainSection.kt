package app.alessandrotedesco.template.ui.section.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.alessandrotedesco.template.R
import app.alessandrotedesco.template.apiservice.model.Pokemon
import app.alessandrotedesco.template.ui.theme.TemplateTheme
import app.alessandrotedesco.template.utils.createNotificationChannel
import app.alessandrotedesco.template.utils.showNotification
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun MainSection(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    MainSectionUI(navController, viewModel.pokemon) {
        viewModel.getPokemon(it)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainSectionUI(
    navController: NavHostController,
    pokemon: MutableLiveData<Pokemon>,
    getPokemon: (String) -> Unit = {}
) {
    val notificationsPermissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.createNotificationChannel("channelId", "channelName", "channelDescription")
    }

    Column {
        Text(text = pokemon.observeAsState().value?.let { it.name + " #" + it.id }
            ?: "??? #??") // TODO example
        Button(onClick = { getPokemon.invoke("pikachu") }) {
            Text(text = "Get Pikachu")
        }
        Button(onClick = { getPokemon("charizard") }) {
            Text(text = "Get Charizard")
        }
        if (notificationsPermissionState.status.isGranted) {
            OutlinedButton(onClick = {
                context.showNotification(
                    title = "Title",
                    content = "Text",
                    channelId = "channelId"
                )
            }) {
                Text(text = "Show notification")
            }
        } else {
            OutlinedButton(onClick = notificationsPermissionState::launchPermissionRequest) {
                Text(text = "Allow notifications")
            }
        }
    }
    // TODO example: navController.navigate(Main.Example2.route)
}

@Preview(showBackground = true)
@Composable
fun MainSectionPreview() {
    TemplateTheme {
        val navController = rememberNavController()
        MainSectionUI(navController, MutableLiveData(Pokemon("Pikachu", 25, 4)))
    }
}