package org.acme.persistence.one.to.many.unidirectional

import org.acme.persistence.one.to.many.SongCollections.humanAfterAllSongData
import org.acme.persistence.one.to.many.SongCollections.modalSoulSongData
import org.acme.persistence.one.to.many.SongCollections.randomAccessMemoriesSongData
import org.acme.persistence.one.to.many.SongCollections.wishYouWereHereSongData
import org.acme.persistence.one.to.many.SongData
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

// This no longer works! We have to persist the band before we can add an album. Else, band doesn't have an ID, and
// then we can't create an album entity (it needs a non-null band id)!
//
//@ApplicationScoped
//class DummyDataCascade(
//    val bandRepository: BandUniRepository
//) {
//    @Transactional
//    fun setupBandAndCascadeAlbumsAndSongs() {
//        val pinkFloyd =
//            BandUnidirectional(name = "Pink Floyd", formationDate = LocalDate.ofYearDay(1965, 1), albums = listOf())
//        pinkFloyd.addWishYouWereHereAlbum()
//        bandRepository.persist(pinkFloyd)
//
//        val daftPunk =
//            BandUnidirectional(name = "Daft Punk", formationDate = LocalDate.ofYearDay(1993, 1), albums = listOf())
//        daftPunk.addRamAlbum()
//        daftPunk.addHumanAfterAllAlbum()
//        bandRepository.persist(daftPunk)
//    }
//}

@ApplicationScoped
class DummyDataSeparate(
    val bandRepository: BandUniRepository,
    val albumRepository: AlbumUniRepository,
    val songRepository: SongUniRepository
) {
    fun setupBandAlbumAndSongsInSeparateTransactions() {
        persistNewBand(name = "Pink Floyd", formationDate = LocalDate.ofYearDay(1965, 1))
            .persistNewAlbum(title = "Wish You Were Here", releaseDate = LocalDate.of(1975, 9, 12))
            .persistNewSongs(wishYouWereHereSongData)

        persistNewBand(name = "Daft Punk", formationDate = LocalDate.ofYearDay(1993, 1))
            .apply {
                this.persistNewAlbum(title = "Random Access Memories", releaseDate = LocalDate.of(2013, 5, 17))
                    .persistNewSongs(randomAccessMemoriesSongData)
            }
            .apply {
                this.persistNewAlbum(title = "Human After All", releaseDate = LocalDate.of(2005, 3, 15))
                    .persistNewSongs(humanAfterAllSongData)
            }

        persistNewBand(name = "Nujabes", formationDate = LocalDate.ofYearDay(1995, 1))
            .persistNewAlbum(title = "Modal Soul", releaseDate = LocalDate.of(2005, 11, 11))
            .persistNewSongs(modalSoulSongData)
    }

    @Transactional
    fun persistNewBand(name: String, formationDate: LocalDate): BandUnidirectional =
        BandUnidirectional(name = name, formationDate = formationDate, albums = listOf())
            .apply { bandRepository.persist(this) }


    @Transactional
    fun BandUnidirectional.persistNewAlbum(title: String, releaseDate: LocalDate): AlbumUnidirectional =
        this.addNewAlbum(title = title, releaseDate = releaseDate)
            .apply { albumRepository.persist(this) }


    @Transactional
    fun AlbumUnidirectional.persistNewSongs(songDataList: List<SongData>) =
        songDataList.forEach {
            this.addNewSong(it.title, it.length)
                .apply { songRepository.persist(this) }
        }


}




