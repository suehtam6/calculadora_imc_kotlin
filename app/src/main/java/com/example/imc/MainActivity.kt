package com.example.imc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imc.ui.theme.IMCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IMCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImcScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ImcScreen(modifier: Modifier = Modifier) {
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var resultadoIMC by remember { mutableStateOf("RESULTADO") }
    var imc by remember { mutableStateOf(0.0) }

    Column(modifier = modifier.fillMaxSize()) {

        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(color = colorResource(R.color.cor_app)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(80.dp)
                    .padding(vertical = 16.dp),
                painter = painterResource(R.drawable.bmi),
                contentDescription = "Icone do IMC"
            )
            Text(
                text = "CALCULADORA DE IMC",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Conteúdos do formulário
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .offset(y = (-30).dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F6F6)),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "SEUS DADOS",
                        color = colorResource(R.color.cor_app),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    // Campo da Altura com filtro anti-letras
                    OutlinedTextField(
                        modifier = Modifier.width(300.dp),
                        value = altura,
                        onValueChange = { novoTexto ->
                            val filtrado = novoTexto.filter { it.isDigit() || it == '.' }
                            if (filtrado.count { it == '.' } <= 1) altura = filtrado
                        },
                        placeholder = { Text(text = "Ex: 1.75") },
                        label = { Text(text = "Altura (m)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = colorResource(R.color.cor_app)
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier.width(300.dp),
                        value = peso,
                        onValueChange = { novoTexto ->
                            val filtrado = novoTexto.filter { it.isDigit() || it == '.' }
                            if (filtrado.count { it == '.' } <= 1) peso = filtrado
                        },
                        placeholder = { Text(text = "Ex: 70.5") },
                        label = { Text(text = "Peso (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = colorResource(R.color.cor_app)
                        )
                    )

                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly

                    ) {
                        Button(
                            onClick = {
                                val pesoDouble = peso.toDoubleOrNull()
                                val alturaDouble = altura.toDoubleOrNull()

                                peso = ""
                                altura = ""


                                if (pesoDouble == null || alturaDouble == null || pesoDouble <= 0.0 || alturaDouble <= 0.0) {
                                    resultadoIMC = "CAMPO INVÁLIDO"
                                } else {
                                    imc = CalcularImc(pesoDouble, alturaDouble)
                                    resultadoIMC = DecisaoImc(imc)
                                }



                            },
                            modifier = Modifier
                                .width(150.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.cor_app)
                            ),


                        ) {
                            Text(text = "CALCULAR",
                                fontWeight = FontWeight.Bold

                            )
                        }

                        Button(
                            onClick = {
                                peso = ""
                                altura = ""
                            },

                            modifier = Modifier
                                .width(150.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.cor_app)
                            ),
                        ) {
                            Text(text = "CANCELAR",
                                fontWeight = FontWeight.Bold

                            )
                        }
                    }





                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val cor: Color = CorFundo(imc)
            CriarCard(cor, resultadoIMC, imc)


        }
    }
}







fun CalcularImc(peso: Double, altura: Double): Double {
    return peso / (altura * altura)
}







fun DecisaoImc(imc: Double): String {
    return when {
        imc < 18.5 -> "Abaixo do peso"
        imc < 25.0 -> "Peso Ideal"
        imc < 30.0 -> "Levemente acima do peso"
        imc < 35.0 -> "Obesidade grau I"
        imc < 40.0 -> "Obesidade grau II"
        else -> "Obesidade grau III"
    }
}






@Composable
fun CriarCard(corFundo: Color, resultadoIMC: String, imc: Double) {
    val imcFormatado = String.format("%.2f", imc)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .background(corFundo)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (imc > 0) imcFormatado else "--",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = resultadoIMC,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

fun CorFundo(imc: Double): Color {
    return if (imc == 0.0) {
        Color.Gray
    } else if (imc < 18.5 || imc >= 30.0) {
        Color(255, 3, 32, 255)
    } else if (imc < 25.0) {
        Color(85, 155, 111)
    } else {
        Color(231, 144, 8, 255)
    }
}