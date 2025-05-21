package org.kollarappa.familytree.ui.personlist


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.kollarappa.familytree.ui.add.PersonListViewModel

//@Composable
//fun PersonListView(viewModel: PersonListViewModel) {
//    val state = viewModel.state.collectAsState().value
//    val scope = rememberCoroutineScope()
//
//    when (state) {
//        PersonListState.Loading -> Text("Loading...")
//        is PersonListState.Success.Success -> {
//            state.persons.forEach { person ->
//                Text(person.firstName + " " + person.lastName)
//            }
//        }
//        is PersonListState.Error.Error -> Text("Error: ${state.message}")
//    }
//
//    scope.launch {
//        viewModel.processIntent(PersonListIntent.LoadPersons)
//    }
//}