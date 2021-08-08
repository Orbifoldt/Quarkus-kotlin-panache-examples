package org.acme.persistence.one.to.many.unidirectional

import java.lang.IllegalStateException
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "BAND_UNIDIRECTIONAL")
data class BandUnidirectional(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAND_ID", nullable = false, updatable = false)
    var id: Long? = null,

    var name: String,

    @Column(name = "FORMATION_DATE")
    var formationDate: LocalDate? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true) // We removed the mappedBy as we use JoinColumn now:
    @JoinColumn( // Now this entity is the owner of the relation
        name = "PARENT_BAND_ID", // Column name in the child (ALBUM_UNI) table that references this entity
        referencedColumnName = "BAND_ID" // The precise column in this table that is referenced by above child column
    )
    var albums: List<AlbumUnidirectional>,
) {
    private fun addAlbum(album: AlbumUnidirectional) = album.apply { albums += album }

    fun addNewAlbum(title: String, releaseDate: LocalDate): AlbumUnidirectional {
        if (id == null) throw IllegalStateException("'id' is null, persist this before adding albums!")
        return addAlbum(AlbumUnidirectional(title = title, releaseDate = releaseDate, songs = listOf(), bandId = id!!))
    }
}
