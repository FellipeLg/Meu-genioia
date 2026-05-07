package com.startup.studyapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.studyapp.ui.viewmodel.AppViewModel

@Composable
fun OnboardingScreen(vm: AppViewModel, onContinue: () -> Unit) {
    var name by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Bem-vindo ao Study Startup")
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Seu nome") })
        Button(onClick = { vm.finishOnboarding(name.ifBlank { "Estudante" }); onContinue() }) { Text("Começar") }
    }
}

@Composable fun HomeScreen() { Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Home") ; Text("Cards: Tutor IA, Resumidor, Flashcards, Histórico") } }

@Composable
fun TutorScreen(vm: AppViewModel) {
    var prompt by remember { mutableStateOf("") }
    val messages by vm.messages.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = prompt, onValueChange = { prompt = it }, label = { Text("Digite sua dúvida") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { vm.askTutor(prompt); prompt = "" }) { Text("Perguntar") }
        LazyColumn { items(messages) { Text("Q: ${it.prompt}\nA: ${it.response}") } }
    }
}

@Composable
fun SummaryScreen(vm: AppViewModel) {
    var text by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Cole o conteúdo") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { vm.summarize(text) { summary = it } }) { Text("Gerar resumo") }
        Text(summary)
    }
}

@Composable
fun FlashcardsScreen(vm: AppViewModel) {
    var q by remember { mutableStateOf("") }
    var a by remember { mutableStateOf("") }
    val cards by vm.flashcards.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = q, onValueChange = { q = it }, label = { Text("Pergunta") })
        OutlinedTextField(value = a, onValueChange = { a = it }, label = { Text("Resposta") })
        Button(onClick = { vm.addFlashcard(q, a); q = ""; a = "" }) { Text("Salvar flashcard") }
        LazyColumn { items(cards) { Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) { Text("${it.question}\n${it.answer}", Modifier.padding(12.dp)) } } }
    }
}

@Composable
fun HistoryScreen(vm: AppViewModel) {
    val history by vm.history.collectAsStateWithLifecycle()
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) { items(history) { Text("${it.type}: ${it.content}") } }
}

@Composable
fun SettingsScreen(vm: AppViewModel) {
    val name by vm.userName.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp)) { Text("Configurações") ; Text("Usuário: $name") }
}
