package org.souzarodrigo

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.Orientation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import android.util.Log
import androidx.compose.material3.MaterialTheme.colorScheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.rememberCoroutineScope

/**
 * Tela principal replicando o AppleMusicScreen do Flutter.
 */
@Composable
fun AppleMusicScreen() {
    // Estados principais
    val selectedIndex = remember { mutableStateOf(0) }
    val showMiniPlayer = remember { mutableStateOf(true) }
    val windowProgress = remember { mutableStateOf(0f) } // Progresso da transformação da "janela"

    // Estado para saber se o player está expandido
    var isPlayerExpanded by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val density = LocalDensity.current
    val screenHeightPx = with(density) { screenHeightDp.toPx() }

    // Calcula os parâmetros de transformação com base no windowProgress (limitado a 0.1)
    val effectiveProgress = windowProgress.value.coerceIn(0f, 0.1f)
    val scale = 1f - effectiveProgress
    val calculatedTranslateY = screenHeightPx * effectiveProgress / 2f
    val borderRadius = (effectiveProgress / 0.1f) * 16.dp

    // Define o border radius a ser usado no Box principal,
    // forçando 16.dp quando o player está expandido (isPlayerExpanded == true)
    val targetBorderRadius = if (isPlayerExpanded) 16.dp else borderRadius
    val animatedBorderRadius by animateDpAsState(
        targetValue = targetBorderRadius,
        animationSpec = tween(durationMillis = 100)
    )

    // Animação dos parâmetros da transformação
    val animatedScale by animateFloatAsState(targetValue = scale, animationSpec = tween(durationMillis = 100))
    val animatedTranslateY by animateFloatAsState(targetValue = calculatedTranslateY, animationSpec = tween(durationMillis = 100))

    // Estado para saber se o usuário está arrastando o player
    var isDragging by remember { mutableStateOf(false) }

    val backgroundColor = MaterialTheme.colorScheme.background

    // Configura o System UI – inicialmente a navigation bar será branca (resultado do container do Scaffold)
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        // Define a cor da navigation bar como branca
        Log.d("AppleMusicScreen", "Definindo a cor da navigation bar como branca")

        systemUiController.setStatusBarColor(backgroundColor)
        systemUiController.setNavigationBarColor(backgroundColor)
    }

    // Box externo com fundo preto para atender o cenário do windowProgress
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Conteúdo principal transformado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = animatedScale
                    scaleY = animatedScale
                    translationY = animatedTranslateY
                }
                .clip(RoundedCornerShape(animatedBorderRadius))
        ) {
            // Scaffold com containerColor definido para branco
            Scaffold(
                containerColor = Color.White,
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedIndex.value == 0,
                            onClick = { selectedIndex.value = 0 },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = selectedIndex.value == 1,
                            onClick = { selectedIndex.value = 1 },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile") }
                        )
                        NavigationBarItem(
                            selected = selectedIndex.value == 2,
                            onClick = { selectedIndex.value = 2 },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile") }
                        )
                        NavigationBarItem(
                            selected = selectedIndex.value == 3,
                            onClick = { selectedIndex.value = 3 },
                            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                            label = { Text("About") }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedIndex.value) {
                        0 -> HomeView()
                        1 -> ProfileView()
                        2 -> ProfileView() // Reutiliza a mesma tela para Profile
                        3 -> AboutView()
                    }
                }
            }
        }

        // Sobreposição do player expansível
        if (showMiniPlayer.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // Se o player estiver expandido, o bottom padding será 0; senão, 78.dp (espaço para a bottom bar)
                    .padding(bottom = if (isPlayerExpanded) 0.dp else 78.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                ExpandablePlayer(
                    isExpanded = isPlayerExpanded,
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onExpandedChange = { isPlayerExpanded = it },
                    onResizeWindow = { progress ->
                        windowProgress.value = progress
                    },
                    onResetWindow = {
                        windowProgress.value = 0f
                    }
                )
            }
        }
    }
}

/**
 * Tela Home com TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Home View")
        }
    }
}

/**
 * Tela Profile com TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Profile View")
        }
    }
}

/**
 * Tela About com TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutView() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("About") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("About View")
        }
    }
}

/**
 * Componente do Player Expandível – simula o comportamento do Flutter com gestos de arrastar,
 * alternando entre os estados "mini" e "expandido", além de atualizar a transformação da janela
 * e a cor da barre de navegação do sistema (via Accompanist).
 */
@Composable
fun ExpandablePlayer(
    isExpanded: Boolean,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onResizeWindow: (Float) -> Unit,
    onResetWindow: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    var isDragging by remember { mutableStateOf(false) }
    
    // Utilizamos Animatable para armazenar o offset vertical e permitir animações suaves
    val offsetY = remember { Animatable(0f) }
    
    val miniHeight = 70.dp

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val density = LocalDensity.current
    val screenHeightPx = with(density) { screenHeightDp.toPx() }
    val miniHeightPx = with(density) { miniHeight.toPx() }

    // Calcula a altura atual do player com base no estado e no offset animado
    val currentHeightPx = if (isExpanded) (screenHeightPx - offsetY.value) else (miniHeightPx - offsetY.value)
    val currentHeightDp = with(density) { currentHeightPx.toDp() }

    val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val bottomPosition = if (isExpanded) 0.dp else bottomPadding

    val animatedBottom by animateDpAsState(targetValue = bottomPosition, animationSpec = tween(200))

    val horizontalMargin = if (isExpanded) 0.dp else 16.dp
    val verticalMargin = if (isExpanded) 0.dp else 8.dp

    val backgroundColor = MaterialTheme.colorScheme.background

    SideEffect {
        Log.d("ExpandablePlayer", "isExpanded: $isExpanded, isDragging: $isDragging")
        if (isDragging) {
            systemUiController.setStatusBarColor(color = Color.Black)
            systemUiController.setNavigationBarColor(color = Color.Black)
        } else {
            if (isExpanded) {
                systemUiController.setStatusBarColor(color = Color(0xFF42A5F5))
                systemUiController.setNavigationBarColor(color = Color(0xFF3F51B5))
            } else {
                systemUiController.setStatusBarColor(color = backgroundColor)
                systemUiController.setNavigationBarColor(color = backgroundColor)
            }
        }
    }

    val currentShape = if (isExpanded && !isDragging) RectangleShape else RoundedCornerShape(16.dp)
    
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, animatedBottom.roundToPx()) }
            .height(currentHeightDp)
            .padding(horizontal = horizontalMargin, vertical = verticalMargin)
            .background(
                color = if (isExpanded) Color.Black.copy(alpha = 0.87f) else Color(0xFF212121),
                shape = currentShape
            )
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { dragAmount ->
                    // Envolve a chamada a 'snapTo' dentro de uma coroutine
                    coroutineScope.launch {
                        if (isExpanded) {
                            // Em modo expandido, permitimos somente valores positivos (arrastar para baixo)
                            offsetY.snapTo((offsetY.value + dragAmount).coerceAtLeast(0f))
                            // Atualiza o progresso da transformação da janela
                            val progress = (offsetY.value / screenHeightPx).coerceIn(0f, 0.1f)
                            onResizeWindow(progress)
                        } else {
                            // No modo mini, permitimos somente valores negativos (arrastar para cima)
                            offsetY.snapTo((offsetY.value + dragAmount).coerceAtMost(0f))
                        }
                    }
                },
                onDragStarted = {
                    isDragging = true
                    onDragStart()
                },
                onDragStopped = { velocity ->
                    isDragging = false
                    if (isExpanded) {
                        // Se o usuário arrastou para baixo além de 30% da altura da tela, fecha o player
                        if (offsetY.value > screenHeightPx * 0.3f) {
                            onExpandedChange(false)
                            coroutineScope.launch {
                                offsetY.animateTo(0f, animationSpec = tween(durationMillis = 300))
                            }
                            onResetWindow()
                        } else {
                            coroutineScope.launch {
                                offsetY.animateTo(0f, animationSpec = tween(durationMillis = 300))
                            }
                            onResizeWindow(0f)
                        }
                    } else {
                        // No modo mini, se o usuário arrastar para cima além de 50 pixels, expande o player
                        if (offsetY.value < -50f) {
                            onExpandedChange(true)
                        } else {
                            coroutineScope.launch {
                                offsetY.animateTo(0f, animationSpec = tween(durationMillis = 300))
                            }
                        }
                    }
                    onDragEnd()
                }
            )
            .shadow(
                elevation = 5.dp,
                shape = currentShape
            )
    ) {
        // Alterna entre a versão mini e expandida com o Crossfade
        Crossfade(targetState = isExpanded) { expanded ->
            if (expanded) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Fundo com gradiente no modo expandido
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF42A5F5), Color(0xFF3F51B5))
                                )
                            )
                    )
                    // Indicador (capsule) no topo
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp)
                            .width(35.dp)
                            .height(5.dp)
                            .background(color = Color.Black, shape = RoundedCornerShape(2.dp))
                    )
                    ExpandedPlayer(topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                }
            } else {
                MiniPlayer()
            }
        }
        // Detecta tap para expandir quando estiver em modo mini
        if (!isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onExpandedChange(true) }
            )
        }
    }
}

/**
 * Visualização do MiniPlayer, com imagem (simulada), título e botões.
 */
@Composable
fun MiniPlayer() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 5.dp,
        tonalElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Simula a artwork com efeito similar ao Hero
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Artwork",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Calm Down", color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* ação de play */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.Black)
            }
            IconButton(onClick = { /* ação de forward */ }) {
                Icon(Icons.Default.Person, contentDescription = "Forward", tint = Color.Black)
            }
        }
    }
}

/**
 * Visualização do ExpandedPlayer com a artwork, informações da música e botões de ações.
 */
@Composable
fun ExpandedPlayer(topPadding: Dp) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(
                top = topPadding + 32.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Artwork com efeito similar ao Hero
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Artwork",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Calm Down", color = Color.White, fontSize = 16.sp)
                Text(text = "Rema, Selena Gomez", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* ação de star */ }) {
                Icon(Icons.Default.Star, contentDescription = "Star", tint = Color.White)
            }
            IconButton(onClick = { /* ação de more */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Conteúdo do Expanded Player...", color = Color.White)
    }
} 