//@file:OptIn(ExperimentalMaterial3Api::class)
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.material.Icon
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Info
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.PlayArrow
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import kotlin.math.roundToInt
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.core.FastOutSlowInEasing
//
//// Tela principal com abas e overlay do player expansível
//@Composable
//fun AppleMusicScreen() {
//    val selectedIndex = remember { mutableStateOf(0) }
//    val showMiniPlayer = remember { mutableStateOf(false) }
//    val items = listOf("Home", "Profile", "Profile2", "About")
//
//    Scaffold(
//        bottomBar = {
//            NavigationBar {
//                items.forEachIndexed { index, title ->
//                    NavigationBarItem(
//                        icon = {
//                            when (index) {
//                                0 -> Icon(Icons.Filled.Home, contentDescription = "Home")
//                                1, 2 -> Icon(Icons.Filled.Person, contentDescription = "Profile")
//                                3 -> Icon(Icons.Filled.Info, contentDescription = "About")
//                                else -> Icon(Icons.Filled.Home, contentDescription = null)
//                            }
//                        },
//                        label = { Text(title) },
//                        selected = selectedIndex.value == index,
//                        onClick = { selectedIndex.value = index }
//                    )
//                }
//            }
//        }
//    ) { innerPadding ->
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .padding(innerPadding)) {
//            when (selectedIndex.value) {
//                0 -> HomeView()
//                1 -> ProfileView()
//                2 -> ProfileView() // Replicando duas telas de Profile
//                3 -> AboutView()
//            }
//            if (showMiniPlayer.value) {
//                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
//                    ExpandablePlayer(show = showMiniPlayer)
//                }
//            }
//        }
//    }
//
//    // Simula o onAppear (SwiftUI) para mostrar o mini player
//    LaunchedEffect(Unit) {
//        showMiniPlayer.value = true
//    }
//}
//
//@Composable
//fun HomeView() {
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Home") }) }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Home View")
//        }
//    }
//}
//
//@Composable
//fun ProfileView() {
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Profile") }) }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Profile View")
//        }
//    }
//}
//
//@Composable
//fun AboutView() {
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("About") }) }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("About View")
//        }
//    }
//}
//
//// Tela base com TextField e botões que exibem overlays
//@Composable
//fun BaseScreen() {
//    val textState = remember { mutableStateOf("") }
//    val showFloatingVideo = remember { mutableStateOf(false) }
//    val showSheet = remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Universal Overlay") }) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp)
//        ) {
//            OutlinedTextField(
//                value = textState.value,
//                onValueChange = { textState.value = it },
//                label = { Text("Message") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = { showFloatingVideo.value = true }) {
//                Text("Floating Video Player")
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = { showSheet.value = true }) {
//                Text("Show Dummy Sheet")
//            }
//        }
//
//        // Overlay flutuante para o vídeo
//        if (showFloatingVideo.value) {
//            FloatingVideoPlayerView(show = showFloatingVideo)
//        }
//        // Exemplo de apresentação de sheet com AlertDialog
//        if (showSheet.value) {
//            AlertDialog(
//                onDismissRequest = { showSheet.value = false },
//                title = { Text("Sheet") },
//                text = { Text("Hello From Sheet!") },
//                confirmButton = {
//                    Button(onClick = { showSheet.value = false }) {
//                        Text("OK")
//                    }
//                }
//            )
//        }
//    }
//}
//
//// Exemplo de um player de vídeo flutuante com gesto de arrastar
//@Composable
//fun FloatingVideoPlayerView(show: MutableState<Boolean>) {
//    var offset by remember { mutableStateOf(Offset.Zero) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(250.dp)
//            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
//            .background(Color.Gray)
//            .pointerInput(Unit) {
//                detectDragGestures { _, dragAmount ->
//                    offset += dragAmount
//                }
//            }
//    ) {
//        // Placeholder para VideoPlayer (você pode integrar o ExoPlayer, por exemplo)
//        Text("Video Player", modifier = Modifier.align(Alignment.Center), color = Color.White)
//    }
//}
//
//// Exemplo do ExpandablePlayer com duas aparências: mini e expandido, e gesto de arrastar
//@Composable
//fun ExpandablePlayer(show: MutableState<Boolean>) {
//    var expanded by remember { mutableStateOf(false) }
//    var offsetY by remember { mutableStateOf(0f) }
//    val configuration = LocalConfiguration.current
//    val screenHeight = configuration.screenHeightDp.dp
//    val targetHeight = if (expanded) screenHeight else 55.dp
//    val animatedHeight by animateDpAsState(
//        targetValue = targetHeight,
//        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(animatedHeight)
//            .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
//            .offset { IntOffset(0, offsetY.roundToInt()) }
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDrag = { _, dragAmount ->
//                        offsetY += dragAmount.y
//                    },
//                    onDragEnd = {
//                        // Lógica simples para alternar entre estados com base no gesto
//                        expanded = offsetY <= 100f
//                        offsetY = 0f
//                    }
//                )
//            }
//    ) {
//        if (!expanded) {
//            // MiniPlayer
//            Row(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 10.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Exemplo de imagem (substitua por Image se tiver recurso)
//                Box(
//                    modifier = Modifier
//                        .size(45.dp)
//                        .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
//                )
//                Spacer(modifier = Modifier.width(12.dp))
//                Text("Calm Down", color = Color.White)
//                Spacer(modifier = Modifier.weight(1f))
//                IconButton(onClick = { /* ação de play */ }) {
//                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.White)
//                }
//                IconButton(onClick = { /* ação de forward */ }) {
//                    Icon(Icons.Filled.Refresh, contentDescription = "Forward", tint = Color.White)
//                }
//            }
//            // Detecta tap para expandir e bloqueia gestos nas views internas
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .zIndex(1f) // Garante que este Box fique acima dos elementos internos
//                    .pointerInput(Unit) {
//                        detectTapGestures(
//                            onTap = { expanded = true }
//                        )
//                    }
//            )
//        } else {
//            // ExpandedPlayer
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                // "Capsule" indicador para arrastar
//                Box(
//                    modifier = Modifier
//                        .width(35.dp)
//                        .height(5.dp)
//                        .background(Color.Gray, shape = RoundedCornerShape(2.dp))
//                        .align(Alignment.CenterHorizontally)
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Box(
//                        modifier = Modifier
//                            .size(80.dp)
//                            .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
//                    )
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Column {
//                        Text("Calm Down", color = Color.White, style = MaterialTheme.typography.bodyLarge)
//                        Text(
//                            "Rema, Selena Gomez",
//                            color = Color.White.copy(alpha = 0.7f),
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                    }
//                    Spacer(modifier = Modifier.weight(1f))
//                    IconButton(onClick = { /* ação de star */ }) {
//                        Icon(Icons.Filled.Favorite, contentDescription = "Favorite", tint = Color.White)
//                    }
//                    IconButton(onClick = { /* ação de mais */ }) {
//                        Icon(Icons.Filled.Notifications, contentDescription = "More", tint = Color.White)
//                    }
//                }
//            }
//        }
//    }
//}
