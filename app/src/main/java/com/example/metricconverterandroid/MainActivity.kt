package com.example.metricconverterandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metricconverterandroid.ui.theme.MetricConverterAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetricConverterAndroidTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MetricConverterApp()
                }
            }
        }
    }
}

@Composable
fun MetricConverterApp() {
    var selectedMetric by remember { mutableStateOf("Panjang") }
    var fromUnit by remember { mutableStateOf("Meter") }
    var toUnit by remember { mutableStateOf("Centimeter") }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val units = when (selectedMetric) {
        "Panjang" -> listOf("Meter", "Centimeter", "Kilometer", "Millimeter")
        else -> listOf()
    }

    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFF64B5F6), Color(0xFFBBDEFB))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Metric Converter",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .shadow(8.dp)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "By: Gizelda Lewu",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Conversion settings card
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Pilih Metrik", style = MaterialTheme.typography.bodyLarge)
                    DropdownMenu(selectedMetric, listOf("Panjang")) { selectedMetric = it }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Dari:", style = MaterialTheme.typography.bodyLarge)
                    DropdownMenu(fromUnit, units) { fromUnit = it }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ke:", style = MaterialTheme.typography.bodyLarge)
                    DropdownMenu(toUnit, units) { toUnit = it }
                }
            }

            // Input field card
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        label = { Text("Nilai untuk Konversi") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val input = inputValue.toDoubleOrNull()
                            if (input != null) {
                                result = performConversion(input, fromUnit, toUnit)
                            } else {
                                result = "Input tidak valid"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Konversi")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Result display card
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hasil: $result",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.shadow(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DropdownMenu(selectedItem: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedItem)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun performConversion(value: Double, fromUnit: String, toUnit: String): String {
    val conversionFactors = mapOf(
        "Meter to Centimeter" to 100.0,
        "Centimeter to Meter" to 0.01,
        "Kilometer to Meter" to 1000.0,
        "Meter to Kilometer" to 0.001,
        "Millimeter to Meter" to 0.001,
        "Meter to Millimeter" to 1000.0
    )
    val conversionKey = "$fromUnit to $toUnit"
    val factor = conversionFactors[conversionKey]
    return if (factor != null) {
        (value * factor).toString()
    } else {
        "Konversi tidak tersedia"
    }
}
