package com.secondpulse.app.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.secondpulse.app.domain.SecondaryScreen
import com.secondpulse.app.ui.theme.SpColors

@Composable
fun PaperBackground(content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(SpColors.Background).drawBehind {
        val step = 28.dp.toPx()
        var x = 8.dp.toPx()
        while (x < size.width) { drawCircle(SpColors.TextMuted.copy(alpha = .018f), .7f, Offset(x, (x * 1.73f) % size.height)); x += step }
    }, content = content)
}

@Composable fun SpTopHeader(title: String, subtitle: String, onMenu: () -> Unit = {}, onSettings: () -> Unit = {}) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp).padding(top = 14.dp)) {
        Box(Modifier.fillMaxWidth().heightIn(min = 88.dp)) {
            IconButton(onClick = onMenu, Modifier.align(Alignment.TopStart).size(48.dp)) { Icon(Icons.Outlined.Menu, "Menu", tint = SpColors.TextPrimary) }
            Column(Modifier.align(Alignment.TopCenter).padding(horizontal = 52.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title, style = MaterialTheme.typography.displaySmall, color = SpColors.TextPrimary, textAlign = TextAlign.Center)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = SpColors.TextSecondary, textAlign = TextAlign.Center, maxLines = 2)
            }
            IconButton(onClick = onSettings, Modifier.align(Alignment.TopEnd).size(48.dp)) { Icon(Icons.Outlined.Settings, "Réglages", tint = SpColors.TextPrimary) }
        }
        Box(Modifier.fillMaxWidth().height(16.dp)) {
            Divider(Modifier.align(Alignment.Center), color = SpColors.BorderSoft)
            Text("◇", Modifier.align(Alignment.Center), color = SpColors.TextMuted)
        }
    }
}

data class NavItem(val screen: SecondaryScreen, val label: String, val icon: ImageVector)
private val items = listOf(
    NavItem(SecondaryScreen.STORY, "Récit", Icons.Outlined.MenuBook), NavItem(SecondaryScreen.PHONE, "Téléphone", Icons.Outlined.Phone),
    NavItem(SecondaryScreen.CAMPUS, "Campus", Icons.Outlined.AccountBalance), NavItem(SecondaryScreen.DOSSIERS, "Dossiers", Icons.Outlined.Folder),
    NavItem(SecondaryScreen.JOURNAL, "Journal", Icons.Outlined.Book)
)

@Composable fun SpBottomNavigation(active: SecondaryScreen, onNavigate: (SecondaryScreen) -> Unit) {
    Row(Modifier.fillMaxWidth().height(72.dp).background(SpColors.Background.copy(alpha = .98f)).drawBehind { drawLine(SpColors.BorderSoft.copy(alpha=.7f), Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx()) }, verticalAlignment = Alignment.CenterVertically) {
        items.forEach { item ->
            val selected = active == item.screen
            Column(Modifier.weight(1f).fillMaxHeight().clickable { onNavigate(item.screen) }.padding(top = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(item.icon, item.label, tint = if (selected) SpColors.Accent else SpColors.IconInactive, modifier = Modifier.size(26.dp))
                Text(item.label, style = MaterialTheme.typography.labelMedium, color = if (selected) SpColors.Accent else SpColors.IconInactive)
                Spacer(Modifier.weight(1f)); Box(Modifier.width(30.dp).height(2.dp).background(if (selected) SpColors.Accent else androidx.compose.ui.graphics.Color.Transparent))
            }
        }
    }
}

@Composable fun SpCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier.background(SpColors.Surface.copy(alpha=.72f), RoundedCornerShape(16.dp)).border(1.dp, SpColors.BorderSoft, RoundedCornerShape(16.dp)).padding(18.dp), content = content)
}

@Composable fun Monogram(name: String, size: Int = 92) {
    val letters = name.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
    Box(Modifier.size(size.dp).border(1.dp, SpColors.BorderStrong, RoundedCornerShape(999.dp)).semantics { contentDescription = "Dossier de $name" }, contentAlignment = Alignment.Center) {
        Text(letters, style = MaterialTheme.typography.headlineMedium, color = SpColors.TextPrimary)
    }
}
