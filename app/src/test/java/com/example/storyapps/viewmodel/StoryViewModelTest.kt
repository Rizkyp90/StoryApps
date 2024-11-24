package com.example.storyapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapps.adapter.StoryAdapter
import com.example.storyapps.api.ListStoryItem
import com.example.storyapps.helper.StoryRepository
import com.example.storyapps.utils.DataDummy
import com.example.storyapps.utils.MainDispatcherRule
import com.example.storyapps.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyToken = "dummy_token"

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null And Return Success`() = runTest {
        val dummyStories = DataDummy.generateDummyStoryResponse()
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = PagingData.from(dummyStories)

        `when`(storyRepository.getStories(dummyToken)).thenReturn(expectedStory)

        val actualStory = storyViewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.STORY_COMPARATOR,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Unconfined,
            mainDispatcher = Dispatchers.Unconfined
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return Zero Item`() = runTest {
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = PagingData.from(emptyList())

        `when`(storyRepository.getStories(dummyToken)).thenReturn(expectedStory)

        val actualStory = storyViewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.STORY_COMPARATOR,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Unconfined,
            mainDispatcher = Dispatchers.Unconfined
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
