package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.WaterLogEntity
import com.example.ui.components.CircularHydrationProgress
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GlowCyan
import com.example.ui.theme.MintTertiary
import com.example.ui.theme.TurquoisePrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationScreen(
    viewModel: HydrationViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GlowCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = TurquoisePrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "HydroTrack",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFE6E1E5)
                                )
                            )
                            Text(
                                text = "Suivi d'Hydratation",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF938F99)
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Streak Badge
                    Surface(
                        color = DarkSurface,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF49454F)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "🔥 ${state.streakDays}j",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TurquoisePrimary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.setGoalDialogVisible(true) },
                        modifier = Modifier
                            .testTag("btn_goal_settings")
                            .background(DarkSurface, CircleShape)
                            .border(1.dp, Color(0xFF49454F), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Modifier l'objectif",
                            tint = TurquoisePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.navigationBars),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular Progress Banner
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    CircularHydrationProgress(
                        progress = state.progress,
                        currentMl = state.currentIntakeMl,
                        targetMl = state.targetGoalMl,
                        size = 250.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status Motivational Message Card
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.isGoalReached) Color(0xFF004D40) else DarkSurface
                        ),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (state.isGoalReached) TurquoisePrimary else Color(0xFF49454F)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isGoalReached) Icons.Default.Check else Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = TurquoisePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (state.isGoalReached) {
                                    "Félicitations ! Objectif quotidien de 2L atteint ! 🎉"
                                } else {
                                    "Encore ${state.remainingMl} ml pour atteindre votre objectif de ${state.targetGoalMl} ml !"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFFE6E1E5),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            // Primary Controls: +250ml & Reset Buttons
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Big Prominent +250 ml Button
                    Button(
                        onClick = { viewModel.addWater(250) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TurquoisePrimary
                        ),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("btn_add_250")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Ajouter 250ml",
                                tint = Color(0xFF003732),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ajouter 250 ml",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF003732)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secondary Actions Row (Reset & Custom Amount)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Reset Button
                        OutlinedButton(
                            onClick = { viewModel.setResetDialogVisible(true) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkSurface,
                                contentColor = TurquoisePrimary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF49454F)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("btn_reset")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Réinitialiser",
                                    tint = TurquoisePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Réinitialiser",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = TurquoisePrimary
                                    )
                                )
                            }
                        }

                        // Custom Amount Option Button
                        Button(
                            onClick = { viewModel.setCustomAmountDialogVisible(true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurface
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF49454F)),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("btn_custom_amount")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = TurquoisePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Personnalisé",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFE6E1E5)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Presets Row (+100ml, +330ml, +500ml) & Beverage Selection
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Raccourcis de boisson",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE6E1E5)
                        ),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        val presets = listOf(100, 200, 250, 330, 500)
                        items(presets) { amount ->
                            Surface(
                                onClick = { viewModel.addWater(amount) },
                                color = DarkSurface,
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF49454F)),
                                modifier = Modifier.testTag("preset_$amount")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = null,
                                        tint = TurquoisePrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "+$amount ml",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFE6E1E5)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Beverage Type Filter
                    val beverageTypes = listOf("Eau", "Thé", "Café", "Jus", "Infusion")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        items(beverageTypes) { beverage ->
                            val isSelected = state.selectedBeverage == beverage
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectBeverage(beverage) },
                                label = { Text(beverage) },
                                shape = RoundedCornerShape(16.dp),
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (beverage == "Café" || beverage == "Thé") Icons.Default.Coffee else Icons.Default.LocalDrink,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF004D40),
                                    selectedLabelColor = TurquoisePrimary,
                                    selectedLeadingIconColor = TurquoisePrimary,
                                    containerColor = DarkSurface,
                                    labelColor = Color(0xFF938F99)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = Color(0xFF49454F),
                                    selectedBorderColor = TurquoisePrimary
                                )
                            )
                        }
                    }
                }
            }

            // History Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Historique d'aujourd'hui",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE6E1E5)
                        )
                    )
                    Text(
                        text = "${state.todayLogs.size} prise(s)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF938F99)
                        )
                    )
                }
            }

            // History Log List Items
            if (state.todayLogs.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF49454F)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = TurquoisePrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Aucune consommation enregistrée",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFE6E1E5)
                                )
                            )
                            Text(
                                text = "Appuyez sur '+ Ajouter 250 ml' pour démarrer !",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF938F99)
                                )
                            )
                        }
                    }
                }
            } else {
                items(
                    items = state.todayLogs,
                    key = { it.id }
                ) { log ->
                    WaterLogRowItem(
                        log = log,
                        onDelete = { viewModel.deleteLog(log) }
                    )
                }
            }
        }
    }

    // Reset Confirmation Dialog
    if (state.showResetDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setResetDialogVisible(false) },
            title = {
                Text(
                    text = "Réinitialiser la journée ?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE6E1E5)
                    )
                )
            },
            text = {
                Text(
                    text = "Voulez-vous réinitialiser votre consommation d'eau pour aujourd'hui ? Tous les enregistrements du jour seront effacés.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF938F99))
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetToday() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                ) {
                    Text("Réinitialiser", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setResetDialogVisible(false) }) {
                    Text("Annuler", color = Color(0xFFE6E1E5))
                }
            },
            containerColor = DarkSurface
        )
    }

    // Custom Goal Dialog
    if (state.showGoalDialog) {
        var newGoalText by remember { mutableStateOf(state.targetGoalMl.toString()) }
        AlertDialog(
            onDismissRequest = { viewModel.setGoalDialogVisible(false) },
            title = {
                Text(
                    text = "Modifier l'objectif quotidien",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE6E1E5)
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "Fixez votre objectif quotidien d'hydratation (en ml) :",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF938F99))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newGoalText,
                        onValueChange = { newGoalText = it.filter { char -> char.isDigit() } },
                        label = { Text("Objectif (ml)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = newGoalText.toIntOrNull() ?: 2000
                        viewModel.updateGoal(amount)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TurquoisePrimary)
                ) {
                    Text("Enregistrer", color = Color(0xFF003732))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setGoalDialogVisible(false) }) {
                    Text("Annuler", color = Color(0xFFE6E1E5))
                }
            },
            containerColor = DarkSurface
        )
    }

    // Custom Amount Dialog
    if (state.showCustomAmountDialog) {
        var customAmountText by remember { mutableStateOf("250") }
        AlertDialog(
            onDismissRequest = { viewModel.setCustomAmountDialogVisible(false) },
            title = {
                Text(
                    text = "Ajouter une quantité",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE6E1E5)
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "Entrez la quantité consommée en ml :",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF938F99))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customAmountText,
                        onValueChange = { customAmountText = it.filter { char -> char.isDigit() } },
                        label = { Text("Quantité (ml)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = customAmountText.toIntOrNull() ?: 250
                        viewModel.addWater(amount)
                        viewModel.setCustomAmountDialogVisible(false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TurquoisePrimary)
                ) {
                    Text("Ajouter", color = Color(0xFF003732))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setCustomAmountDialogVisible(false) }) {
                    Text("Annuler", color = Color(0xFFE6E1E5))
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
fun WaterLogRowItem(
    log: WaterLogEntity,
    onDelete: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(log.timestamp) { timeFormat.format(Date(log.timestamp)) }

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF49454F)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .testTag("log_item_${log.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF004D40)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (log.beverageType == "Café" || log.beverageType == "Thé") Icons.Default.Coffee else Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = TurquoisePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.beverageType,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE6E1E5)
                    )
                )
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF938F99)
                    )
                )
            }

            Text(
                text = "+${log.amountMl} ml",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TurquoisePrimary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = Color(0xFF938F99),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
