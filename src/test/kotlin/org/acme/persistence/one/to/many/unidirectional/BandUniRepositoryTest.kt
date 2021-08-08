package org.acme.persistence.one.to.many.unidirectional

import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject
import javax.transaction.Transactional
import kotlin.streams.toList

@QuarkusTest
internal class BandUniRepositoryTest {

    @Inject
    lateinit var bandRepository: BandUniRepository

    @Inject
    lateinit var albumRepository: AlbumUniRepository

    @Inject
    lateinit var songRepository: SongUniRepository

    @Inject
    lateinit var dummyDataSeparate: DummyDataSeparate

    @BeforeEach
    @Transactional
    fun setup() {
        songRepository.deleteAll()
        albumRepository.deleteAll()
        bandRepository.deleteAll()
//        bandRepository.findAll().list<BandUnidirectional>().forEach { bandRepository.delete(it) } // this doesn't work for some reason...
    }

    @Test
    internal fun `Creating just a band and persisting it, then adding an album`() {
        persistRhcp()
        val foundBand = bandRepository.findFirstByName("Red Hot Chili Peppers")
        assertThat(foundBand.name).isEqualTo("Red Hot Chili Peppers")
        assertThat(foundBand.formationDate).isEqualTo(LocalDate.ofYearDay(1983, 1))
        assertThat(foundBand.id).isNotNull

        // Then create an album using the id of the band
        persistStadiumArcadium(foundBand.id!!)
        val foundAlbum = albumRepository.find("TITLE", "Stadium Arcadium").firstResult<AlbumUnidirectional>()
        assertThat(foundAlbum.releaseDate).isEqualTo(LocalDate.of(2006, 5, 5))
    }

    @Transactional
    fun persistRhcp() = bandRepository.persist(
        BandUnidirectional(
            name = "Red Hot Chili Peppers",
            formationDate = LocalDate.ofYearDay(1983, 1),
            albums = listOf()
        )
    )

    @Transactional
    fun persistStadiumArcadium(bandId: Long) = albumRepository.persist(
        AlbumUnidirectional(
            title = "Stadium Arcadium",
            releaseDate = LocalDate.of(2006, 5, 5),
            bandId = bandId,
            songs = listOf()
        )
    )

    @Test
    internal fun `Retrieving dummy data in various ways`() {
        dummyDataSeparate.setupBandAlbumAndSongsInSeparateTransactions()

        val daftPunk = bandRepository.findFirstByName("Daft Punk")
        assertThat(daftPunk.formationDate?.year).isEqualTo(1993)

        val daftPunk2 = bandRepository.findById(daftPunk.id)
        assertThat(daftPunk2.name).isEqualTo("Daft Punk")

        val pinkFloyd = bandRepository.find("year(FORMATION_DATE)", 1965).firstResult<BandUnidirectional>()
        assertThat(pinkFloyd.name).isEqualTo("Pink Floyd")

        val orderedBands = bandRepository.list("order by formation_date desc")
        assertThat(orderedBands[0].name).isEqualTo("Nujabes")
        assertThat(orderedBands[1].name).isEqualTo("Daft Punk")
        assertThat(orderedBands[2].name).isEqualTo("Pink Floyd")
    }

    @Test
    internal fun `Fetching lazily loaded data`() {
        dummyDataSeparate.setupBandAlbumAndSongsInSeparateTransactions()

        val pinkFloyd = bandRepository.findFirstByName("Pink Floyd")
        assertThat(pinkFloyd.albums).hasSize(1)

        val album = pinkFloyd.albums[0]
        assertThat(album.title).isEqualTo("Wish You Were Here")
        assertThat(album.songs).hasSize(5)
        assertThat(songRepository.find("PARENT_ALBUM_ID", album.id).count()).isEqualTo(5)
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
    internal fun `We can delete individual songs or albums with the individual repositories`() {
        dummyDataSeparate.setupBandAlbumAndSongsInSeparateTransactions()

        // Finding a song from a specific album and artist and then deleting it
        val humanAfterAll = albumRepository.find("TITLE", "Human After All").firstResult<AlbumUnidirectional>()
        deleteEmotion(humanAfterAll.id)
        assertThat(humanAfterAll.songs).hasSize(9)
        assertThat(songRepository.find("TITLE", "Emotion").count()).isEqualTo(0L)

        // Now deleting a full album
        val wishYouWereHere = albumRepository.find("TITLE", "Wish You Were Here").firstResult<AlbumUnidirectional>()
        deleteWishYouWereHere(wishYouWereHere.id!!)
        val pinkFloyd = bandRepository.findFirstByName("Pink Floyd")
        assertThat(pinkFloyd.albums).hasSize(0)
        assertThat(songRepository.find("TITLE", "Welcome to the Machine").count()).isEqualTo(0)
        assertThat(songRepository.find("TITLE", "Giorgio by Moroder").count()).isEqualTo(1)
    }

    @Transactional
    fun deleteEmotion(albumId: Long?) {
        val songs = songRepository.find("TITLE", "Emotion").stream<SongUnidirectional>()
            .filter { it.albumId == albumId } // We have to filter on album id now, can't use artist or album name
            .toList()
        assertThat(songs).hasSize(1)
        songRepository.deleteById(songs[0].id) // Deleting by ID will auto update the album entity object
    }

    @Transactional
    fun deleteWishYouWereHere(id: Long) {
        songRepository.delete("PARENT_ALBUM_ID", id)
        albumRepository.deleteById(id)
    }
}