package org.acme.persistence.one.to.many.bidirectional

import io.quarkus.test.junit.QuarkusTest
import org.acme.persistence.one.to.many.unidirectional.AlbumUnidirectional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject
import javax.transaction.Transactional
import kotlin.streams.toList

@QuarkusTest
internal class BandRepositoryTest {

    @Inject lateinit var bandRepository: BandRepository
    @Inject lateinit var albumRepository: AlbumRepository
    @Inject lateinit var songRepository: SongRepository

    @Inject lateinit var dummyDataCascade: DummyDataCascade
    @Inject lateinit var dummyDataSeparate: DummyDataSeparate

    @BeforeEach
    @Transactional
    fun setup() {
        // bandRepository.deleteAll() doesn't cascade
        bandRepository.findAll().list<Band>().forEach { bandRepository.delete(it) }
    }

    @Test
    @Transactional
    internal fun `Creating just a band and persisting it`() {
        val redHotChiliPeppers =
            Band(name = "Red Hot Chili Peppers", formationDate = LocalDate.ofYearDay(1983, 1), albums = listOf())
        bandRepository.persist(redHotChiliPeppers)

        val foundBand = bandRepository.findFirstByName("Red Hot Chili Peppers")
        assertThat(foundBand.name).isEqualTo(redHotChiliPeppers.name)
        assertThat(foundBand.formationDate).isEqualTo(redHotChiliPeppers.formationDate)
        assertThat(foundBand.id).isNotNull
    }

    @Test
    internal fun `Retrieving dummy data in various ways`() {
        dummyDataCascade.setupBandAndCascadeAlbumsAndSongs()

        val daftPunk = bandRepository.findFirstByName("Daft Punk")
        assertThat(daftPunk.formationDate?.year).isEqualTo(1993)

        val daftPunk2 = bandRepository.findById(daftPunk.id)
        assertThat(daftPunk2.name).isEqualTo("Daft Punk")

        val pinkFloyd = bandRepository.find("year(FORMATION_DATE)", 1965).firstResult<Band>()
        assertThat(pinkFloyd.name).isEqualTo("Pink Floyd")

        val orderedBands = bandRepository.list("order by formation_date desc")
        assertThat(orderedBands[0].name).isEqualTo("Daft Punk")
        assertThat(orderedBands[1].name).isEqualTo("Pink Floyd")
    }

    @Test
    internal fun `Fetching lazily loaded data`() {
        dummyDataCascade.setupBandAndCascadeAlbumsAndSongs()

        val pinkFloyd = bandRepository.findFirstByName("Pink Floyd")
        assertThat(pinkFloyd.albums).hasSize(1)

        val album = pinkFloyd.albums[0]
        assertThat(album.title).isEqualTo("Wish You Were Here")
        assertThat(album.songs).hasSize(5)
        assertThat(songRepository.find("ALBUM_ID", album.id).count()).isEqualTo(5)
    }

    @Test
    internal fun `It's also possible to add albums or songs with their respective repositories`() {
        dummyDataSeparate.setupBandAlbumAndSongsInSeparateTransactions()

        val nujabes = bandRepository.findFirstByName("Nujabes")
        assertThat(nujabes.albums).hasSize(1)

        val modalSoul = nujabes.albums[0]
        assertThat(modalSoul.title).isEqualTo("Modal Soul")
        assertThat(modalSoul.songs).hasSize(14)

        val songs = modalSoul.songs
        assertThat(songs[0].title).isEqualTo("Feather")
        assertThat(songs[0].length).isEqualTo(Duration.ofSeconds(175))
    }

    @Test
    internal fun `We can delete individual songs or albums with the individual repositories`(){
        dummyDataCascade.setupBandAndCascadeAlbumsAndSongs()

        // Finding a song from a specific album and artist and then deleting it
        deleteEmotion()
        val humanAfterAll = albumRepository.find("TITLE", "Human After All").firstResult<Album>()
        assertThat(humanAfterAll.songs).hasSize(9)
        assertThat(songRepository.find("TITLE", "Emotion").count()).isEqualTo(0L)

        // Now deleting a full album
        val wishYouWereHere = albumRepository.find("TITLE", "Wish You Were Here").firstResult<Album>()
        deleteWishYouWereHere(wishYouWereHere.id!!)
        val pinkFloyd = bandRepository.findFirstByName("Pink Floyd")
        assertThat(pinkFloyd.albums).hasSize(0)
        assertThat(songRepository.find("TITLE", "Welcome to the Machine").count()).isEqualTo(0)
        assertThat(songRepository.find("TITLE", "Giorgio by Moroder").count()).isEqualTo(1)
    }

    @Transactional
    fun deleteEmotion(){
        val songs = songRepository.find("TITLE", "Emotion").stream<Song>()
            .filter {it.album.band.name == "Daft Punk"}
            .filter {it.album.title == "Human After All"}
            .toList()
        assertThat(songs).hasSize(1)
        songRepository.delete(songs[0])
    }

    @Transactional
    fun deleteWishYouWereHere(id: Long) = albumRepository.deleteById(id)
}
