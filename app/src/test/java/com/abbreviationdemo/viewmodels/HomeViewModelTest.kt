package com.abbreviationdemo.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abbreviationdemo.AbbreviationAPIs
import com.abbreviationdemo.models.AbbreviationManager
import com.abbreviationdemo.models.FullForms
import com.abbreviationdemo.models.UIState
import com.abbreviationdemo.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var abbreviationManager: AbbreviationManager
    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var abbreviationAPIs: AbbreviationAPIs
    @Mock
    private lateinit var uiStateObserver: Observer<UIState<List<FullForms>>>

    @Before
    fun setUp() {
        // do something if required
        abbreviationManager = AbbreviationManager(abbreviationAPIs)
        viewModel = HomeViewModel(abbreviationManager)
    }

    @Test
    fun givenServerResponse200_whenFetch_emptyList_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<FullForms>())
                .`when`(abbreviationAPIs)
                .getFullForms(anyString())

            viewModel.getFullForms("")
            viewModel.getFullFormListUiState().observeForever(uiStateObserver)
            verify(abbreviationAPIs).getFullForms(anyString())
            verify(uiStateObserver).onChanged(
                UIState.Success(emptyList())
            )
        }
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        val result = mutableListOf<FullForms>().also {
            it.add(FullForms(sf = "", list = emptyList()))
        }
        testCoroutineRule.runBlockingTest {
            doReturn(result)
                .`when`(abbreviationAPIs)
                .getFullForms(anyString())

            viewModel.getFullForms("")
            viewModel.getFullFormListUiState().observeForever(uiStateObserver)
            verify(abbreviationAPIs).getFullForms(anyString())
            verify(uiStateObserver).onChanged(
                UIState.Success(result)
            )
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message For You"
            doThrow(java.lang.RuntimeException(errorMessage))
                .`when`(abbreviationAPIs)
                .getFullForms(anyString())
            abbreviationManager = AbbreviationManager(abbreviationAPIs)
            val viewModel = HomeViewModel(abbreviationManager)
            viewModel.getFullForms("")
            viewModel.getFullFormListUiState().observeForever(uiStateObserver)
            verify(abbreviationAPIs).getFullForms(anyString())
            verify(uiStateObserver).onChanged(
                UIState.Error(errorMessage)
            )
        }
    }
}