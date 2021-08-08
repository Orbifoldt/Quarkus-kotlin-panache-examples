package org.acme.persistence.one.to.many.bidirectional

import org.acme.persistence.one.to.many.SongCollections.humanAfterAllSongData
import org.acme.persistence.one.to.many.SongCollections.modalSoulSongData
import org.acme.persistence.one.to.many.SongCollections.randomAccessMemoriesSongData
import org.acme.persistence.one.to.many.SongCollections.wishYouWereHereSongData
import org.acme.persistence.one.to.many.SongData
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

@ApplicationScoped
class DummyDataCascade(
    val bandRepository: BandRepository
) {
    @Transactional
    fun setupBandAndCascadeAlbumsAndSongs() {
        val pinkFloyd = Band(name = "Pink Floyd", formationDate = LocalDate.ofYearDay(1965, 1), albums = listOf())
        pinkFloyd.addWishYouWereHereAlbum()
        bandRepository.persist(pinkFloyd)

        val daftPunk = Band(name = "Daft Punk", formationDate = LocalDate.ofYearDay(1993, 1), albums = listOf())
        daftPunk.addRamAlbum()
        daftPunk.addHumanAfterAllAlbum()
        bandRepository.persist(daftPunk)
    }
}

@ApplicationScoped
class DummyDataSeparate(
    val bandRepository: BandRepository,
    val albumRepository: AlbumRepository,
    val songRepository: SongRepository
) {
    fun setupBandAlbumAndSongsInSeparateTransactions() {
        setupNujabes()
            .setupModalSoul()
            .setupModalSoulSongs()
    }

    @Transactional
    fun setupNujabes(): Band {
        val nujabes = Band(name = "Nujabes", formationDate = LocalDate.ofYearDay(1995, 1), albums = listOf())
        bandRepository.persist(nujabes)
        return nujabes
    }

    @Transactional
    fun Band.setupModalSoul(): Album {
        val modalSoul =
            Album(
                title = "Modal Soul",
                releaseDate = LocalDate.of(2005, 11, 11),
                band = this,
                songs = listOf()
            )
        albumRepository.persist(modalSoul)
        return modalSoul
    }

    @Transactional
    fun Album.setupModalSoulSongs() {
        modalSoulSongData.forEach {
            songRepository.persist(
                Song(
                    title = it.title,
                    length = it.length,
                    album = this
                )
            )
        }
    }
}


private fun Album.withSongs(songDataList: List<SongData>) =
    apply { songDataList.forEach { this.addNewSong(it.title, it.length) } }

private fun Band.addRamAlbum() {
    this.addNewAlbum(title = "Random Access Memories", releaseDate = LocalDate.of(2013, 5, 17))
        .withSongs(randomAccessMemoriesSongData)
}

private fun Band.addHumanAfterAllAlbum() {
    this.addNewAlbum(title = "Human After All", releaseDate = LocalDate.of(2005, 3, 15))
        .withSongs(humanAfterAllSongData)
}

private fun Band.addWishYouWereHereAlbum() {
    this.addNewAlbum(title = "Wish You Were Here", releaseDate = LocalDate.of(1975, 9, 12))
        .withSongs(wishYouWereHereSongData)
}



