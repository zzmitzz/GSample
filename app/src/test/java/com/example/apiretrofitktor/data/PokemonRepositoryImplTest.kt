package com.example.apiretrofitktor.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.room.Room
import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import com.example.apiretrofitktor.data.local.room.AppDataBase
import com.example.apiretrofitktor.data.local.room.PokemonLocalDAO
import com.example.apiretrofitktor.data.remote.model.PokemonResponse
import com.example.apiretrofitktor.data.remote.retrofit.ApiServicePokemon
import com.example.apiretrofitktor.ui.model.Pokemon
import com.example.apiretrofitktor.ui.model.toPokemonEntity
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.math.exp

@ExtendWith(MockKExtension::class)
class PokemonRepositoryImplTest {
    private lateinit var repository: PokemonRepositoryImpl
    private val apiServicePokemon = mockk<ApiServicePokemon>()
    private val pokemonLocalDAO = mockk<PokemonLocalDAO>()
    private val context = mockk<Context>()

    private val bitmap = mockk<Bitmap>()
    @Before
    fun setUp() {
        repository = PokemonRepositoryImpl(apiServicePokemon,pokemonLocalDAO)
    }

//    @Test
//    fun `getPokemon returns success from network`() = runTest {
//        // Given
//        val limit = 2
//        val offset = 0
//        val pokemonList = listOf(
//            Pokemon(
//                id = 1,
//                name = "A1",
//                image = bitmap.copy(bitmap.config,false)
//            ),
//            Pokemon(
//                id = 2,
//                name = "A2",
//                image = bitmap.copy(bitmap.config,false)
//            )
//
//        )
//        val pokemonEntityList = pokemonList.map { it.toPokemonEntity() }
//        coEvery { pokemonLocalDAO.insertAllPokemon(pokemonEntityList) } just Awaits
//
//        // When
//        val result = repository.getPokemon(networkConnection = true, limit = limit, offset = offset)
//
//        // Then
//        assertTrue(result is ResultOf.Success)
////        assertEquals(pokemonList, (result as ResultOf.Success).data)
//        coVerify { pokemonLocalDAO.insertAllPokemon(pokemonEntityList) }
//    }
    @Test
    fun `insertPokemon calls DAO to insert`() = runBlocking {
        // Given
        val pokemonEntityList = listOf(
            PokemonEntity(1, "Pikachu", ByteArray(0)),
            PokemonEntity(2, "Bulbasaur", ByteArray(0))
        )
        coEvery { pokemonLocalDAO.insertAllPokemon(pokemonEntityList) } returns listOf()

        // When
        repository.insertPokemon(pokemonEntityList)

        // Then
        coVerify { pokemonLocalDAO.insertAllPokemon(pokemonEntityList) }
    }
    @Test
    fun `deleteOfflineCache calls DAO to delete`() = runTest {
        // Given
        coEvery { pokemonLocalDAO.dumpTable() } just Runs

        // When
        repository.deleteOfflineCache()

        // Then
        coVerify { pokemonLocalDAO.dumpTable() }
    }
    @After
    fun tearDown() {
    }
}