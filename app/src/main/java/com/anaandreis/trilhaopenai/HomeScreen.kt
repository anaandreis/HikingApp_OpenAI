package com.anaandreis.trilhaopenai


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {

    val whereInput = remember { mutableStateOf("") }
    val difficultyInput = remember { mutableStateOf("") }
    val goalInput = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF085640))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Vamos escolher \nsua próxima trilha!",
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 50.sp,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // EditText
            TextField(
                value = whereInput.value,
                onValueChange = { whereInput.value = it },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xff0B3621),
                    textColor = Color.White,  // Set the color of the text
                    cursorColor = Color.White,  // Set the color of the cursor
                    focusedIndicatorColor = Color(0xff0B3621),  // Set the color of the line when the TextField is focused
                    unfocusedIndicatorColor = Color(0xff0B3621),  // Set the color of the line when the TextField is not focused
                    disabledIndicatorColor = Color(0xff0B3621)),
                label = { Text("Onde?", color = Color.White) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Row of buttons
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    onClick = { difficultyInput.value = "Fácil" },
                    modifier = Modifier
                        .width(width = 91.dp)
                        .height(height = 41.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =  if (difficultyInput.value == "Fácil") Color(0xff78FF7B) else Color.White, // Change background color
                        contentColor = Color.Black // Change text color
                    ),
                   // Add elevation
                ) {
                    Text(
                        text = "Fácil",
                    )
                }


              Button(
                    onClick = { difficultyInput.value = "Médio" },
                    colors = ButtonDefaults.buttonColors(
                        // Change background color
                        containerColor = if (difficultyInput.value == "Médio") Color(0xff78FF7B) else Color.White,
                        contentColor = Color.Black // Change text color
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                    modifier = Modifier
                        .width(width = 91.dp)
                        .height(height = 41.dp)
                ) {
                    Text(
                        "Médio"
                    )

                }
                Button(
                    onClick = { difficultyInput.value = "Difícil" },
                    colors = ButtonDefaults.buttonColors(
                        // Change background color
                        containerColor = if (difficultyInput.value == "Difícil")  Color(0xff78FF7B) else Color.White,
                        contentColor = Color.Black // Change text color
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .width(width = 91.dp)
                        .height(height = 41.dp)
                ) {
                    Text(
                        "Difícil",
                    )

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = goalInput.value,
                onValueChange = { goalInput.value = it },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xff0B3621),
                    textColor = Color.White,  // Set the color of the text
                    cursorColor = Color.White,  // Set the color of the cursor
                    focusedIndicatorColor = Color(0xff0B3621),  // Set the color of the line when the TextField is focused
                    unfocusedIndicatorColor = Color(0xff0B3621),  // Set the color of the line when the TextField is not focused
                    disabledIndicatorColor = Color(0xff0B3621)),
                label = { Text("Objetivo? (aventura, fotografia...)", color = Color.White,
                    )
                }

            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.createSearchMessage(
                        whereInput.value,
                        difficultyInput.value,
                        goalInput.value
                    )

                    // Navigate to the Maps screen
                    navController.navigate(route = Screen.Maps.route)
                },
                colors = ButtonDefaults.buttonColors(
                    // Change background color
                    containerColor = Color(0xffff5449),
                    contentColor = Color.White // Change text color
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .width(width = 91.dp)
                    .height(height = 41.dp)
            ) {
                Text(
                    "BORA!"
                )
            }
            Spacer(modifier = Modifier.height(80.dp)) // Add spacer here
        }

        Image(
            painter = painterResource(id = R.drawable.mountains_framed),
            contentDescription = "mountains",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}



@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), viewModel = viewModel())
}