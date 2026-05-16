package com.example.audioanalyzercompanion

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class AudioAnalyzerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch analyses should update analyses list on success`() = runTest {
        // This is a template - implement when you add ViewModel
        // val viewModel = AudioAnalyzerViewModel(repository)
        // viewModel.fetchAnalyses()
        // advanceUntilIdle()
        // assertThat(viewModel.analyses.value).isNotEmpty()
    }
}