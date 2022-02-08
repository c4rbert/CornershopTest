package com.cornershop.counterstest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.cornershop.counterstest.data.database.AppDatabase
import com.cornershop.counterstest.data.database.daos.CountersDao
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.domain.api.CounterAPI
import org.junit.*
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import gentera.yastas.yas_app_client_gestion_ventas.api.client.Client
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var countersDao: CountersDao

    private lateinit var countersAPI: CounterAPI


    @Before
    fun setUp() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            ).allowMainThreadQueries()
                .build()
        countersDao = db.counterDao()
        countersAPI =
            Client(ApplicationProvider.getApplicationContext()).getClient(CounterAPI::class.java) as CounterAPI
    }

    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun insertCounter() = runBlockingTest {
        val counter = ArrayList<CountersItemEntity>()
        counter.add(CountersItemEntity("1", 2, "Test"))
        countersDao.insertCounters(counter)
        val counters = countersDao.getSavedCounters()
        assertThat(counters).doesNotContain(counter)
    }

    @Test
    fun deleteLocalCounters() = runBlockingTest {
        val counters = ArrayList<CountersItemEntity>()
        counters.add(CountersItemEntity("1", 2, "Test"))
        counters.add(CountersItemEntity("2", 2, "Test"))
        counters.add(CountersItemEntity("3", 2, "Test"))
        countersDao.insertCounters(counters)

        countersDao.deleteSelectedCounters(counters.map { it.id })

        val savedCounters = countersDao.getSavedCounters()
        if (savedCounters != null) {
            assertThat(savedCounters.isEmpty())
        }
    }

    @Test
    fun deleteServerCounters() = runBlockingTest {
        val latch: CountDownLatch = CountDownLatch(1)

        val counters = ArrayList<CountersItemEntity>()
        counters.add(CountersItemEntity("1", 2, "Test"))
        counters.add(CountersItemEntity("2", 2, "Test"))
        counters.add(CountersItemEntity("3", 2, "Test"))

        for (item in counters) {
            GlobalScope.launch(Dispatchers.IO) {
                countersAPI.deleteCounter(IdCounter(item.id))
                try {
                    latch.await(3_000, TimeUnit.MILLISECONDS)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
}