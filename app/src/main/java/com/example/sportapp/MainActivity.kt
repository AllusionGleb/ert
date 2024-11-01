package com.example.sportapp

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sportapp.ui.theme.SportAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                    // Или можете использовать конкретный цвет:
                    // color = Color(0xFFE0F7FA) // Светло-голубой
                    // color = Color(0xFFE8F5E9) // Светло-зеленый
                    // color = Color(0xFFF3E5F5) // Светло-фиолетовый
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("nutrition") { NutritionScreen() }
        composable("workouts") { WorkoutsScreen() }
        composable("bmi_calculator") { BMICalculatorScreen() }
        composable("notes") { NotesScreen() }
    }
}


@Composable
fun NutritionScreen() {
    Text(text = "Информация о правильном питании")
}

@Composable
fun WorkoutsScreen() {
    Text(text = "Информация о тренировках")
}

@Composable
fun BMICalculatorScreen() {
    val height = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val bmiResult = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Калькулятор ИМТ",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = height.value,
            onValueChange = { height.value = it },
            label = { Text("Рост (см)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = weight.value,
            onValueChange = { weight.value = it },
            label = { Text("Вес (кг)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                try {
                    val h = height.value.toFloat() / 100 // переводим см в метры
                    val w = weight.value.toFloat()
                    val bmi = w / (h * h)
                    val result = when {
                        bmi < 18.5 -> "Недостаточный вес (ИМТ: %.1f)".format(bmi)
                        bmi < 25 -> "Нормальный вес (ИМТ: %.1f)".format(bmi)
                        bmi < 30 -> "Избыточный вес (ИМТ: %.1f)".format(bmi)
                        else -> "Ожирение (ИМТ: %.1f)".format(bmi)
                    }
                    bmiResult.value = result
                } catch (e: Exception) {
                    bmiResult.value = "Пожалуйста, введите корректные значения"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Рассчитать")
        }

        if (bmiResult.value.isNotEmpty()) {
            Text(
                text = bmiResult.value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

data class Note(val id: Int, var text: String)

// Добавьте этот object перед функцией NotesScreen
object NotesRepository {
    val notes = mutableStateListOf<Note>()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen() {
    var newNoteText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Мои заметки",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Поле ввода для новой заметки
        OutlinedTextField(
            value = newNoteText,
            onValueChange = { newNoteText = it },
            label = { Text("Новая заметка") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Кнопка добавления заметки
        Button(
            onClick = {
                if (newNoteText.isNotEmpty()) {
                    val newNote = Note(NotesRepository.notes.size, newNoteText)
                    NotesRepository.notes.add(newNote)
                    newNoteText = ""
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 16.dp)
        ) {
            Text("Добавить заметку")
        }

        // Список заметок
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(NotesRepository.notes) { note ->
                NoteItem(note = note, onDelete = { NotesRepository.notes.remove(note) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(note: Note, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = note.text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить заметку"
                )
            }
        }
    }
}

// Функцию rememberNavController можно удалить, так как она уже должна быть
// определена в Android Navigation компоненте

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    TopAppBar(
        title = { Text("Спортивное приложение") }
    )
    // Здесь будет контент главного экрана с кнопками для перехода к разделам
}