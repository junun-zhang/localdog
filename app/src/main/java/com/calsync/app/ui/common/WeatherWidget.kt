package com.calsync.app.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calsync.app.domain.model.Weather

@Composable
fun WeatherCard(weather: Weather) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = weather.city,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${weather.temperature}\u00B0C",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            WeatherIcon(condition = weather.condition, size = 48.dp)
        }
        weather.humidity?.let { humidity ->
            Text(
                text = "\u6e7f\u5ea6: $humidity%",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeatherCompact(weather: Weather) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        WeatherIcon(condition = weather.condition, size = 18.dp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${weather.temperature}\u00B0",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun WeatherIcon(condition: Weather.Condition, size: Dp) {
    val icon: ImageVector = when (condition) {
        Weather.Condition.SUNNY -> Icons.Default.WbSunny
        Weather.Condition.CLOUDY -> Icons.Default.Cloud
        Weather.Condition.RAINY -> Icons.Default.WaterDrop
        Weather.Condition.SNOWY -> Icons.Default.AcUnit
        Weather.Condition.STORMY -> Icons.Default.Thunderstorm
        Weather.Condition.WINDY -> Icons.Default.Air
        Weather.Condition.FOGGY -> Icons.Default.WbCloudy
    }
    Icon(
        imageVector = icon,
        contentDescription = condition.name,
        modifier = Modifier.size(size),
        tint = MaterialTheme.colorScheme.primary
    )
}
