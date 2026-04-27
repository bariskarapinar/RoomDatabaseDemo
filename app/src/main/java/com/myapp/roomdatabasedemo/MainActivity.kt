package com.myapp.roomdatabasedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myapp.roomdatabasedemo.data.Word
import com.myapp.roomdatabasedemo.ui.WordViewModel
import com.myapp.roomdatabasedemo.ui.WordViewModelFactory
import com.myapp.roomdatabasedemo.ui.theme.RoomDatabaseDemoTheme

class MainActivity : ComponentActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomDatabaseDemoTheme {
                WordApp(wordViewModel)
            }
        }
    }
}

@Composable
fun WordApp(viewModel: WordViewModel) {
    val words by viewModel.allWords.collectAsState()
    var wordText by remember { mutableStateOf("") }
    var editingWord by remember { mutableStateOf<Word?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Room Database CRUD",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = wordText,
                    onValueChange = { wordText = it },
                    label = { Text(if (editingWord != null) "Edit word" else "New word") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (wordText.isNotBlank()) {
                        val currentEditingWord = editingWord
                        if (currentEditingWord != null) {
                            viewModel.update(currentEditingWord.copy(word = wordText))
                            editingWord = null
                        } else {
                            viewModel.insert(Word(word = wordText))
                        }
                        wordText = ""
                    }
                }) {
                    Text(if (editingWord != null) "Update" else "Add")
                }
            }

            if (editingWord != null) {
                Button(
                    onClick = {
                        editingWord = null
                        wordText = ""
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancel Edit")
                }
            }

            Button(
                onClick = { viewModel.deleteAll() },
                modifier = Modifier.padding(vertical = 16.dp),
                enabled = words.isNotEmpty()
            ) {
                Text("Delete All Words")
            }

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(words) { word ->
                    WordItem(
                        word = word,
                        onDelete = { viewModel.delete(word) },
                        onEdit = {
                            editingWord = word
                            wordText = word.word
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WordItem(
    word: Word,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = word.word,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
