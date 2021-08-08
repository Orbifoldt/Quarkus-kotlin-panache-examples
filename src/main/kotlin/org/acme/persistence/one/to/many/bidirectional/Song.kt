package org.acme.persistence.one.to.many.bidirectional

import org.acme.persistence.converters.DurationToMillisConverter
import java.time.Duration
import javax.persistence.AttributeConverter
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Song(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SONG_ID", nullable = false, updatable = false)
    var id: Long? = null,  // Set initially to null as it's generated by the DB

    var title: String,

    @Column(name = "LENGTH_MILLIS")
    @Convert(converter = DurationToMillisConverter::class)
    var length: Duration,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM_ID") // Name of the column in the parent entity, denotes this is the owner of the relation
    var album: Album,
) {
    // Implement toString for bidirectional mappings, else you get a stack overflow due to data class's toString method
    override fun toString(): String {
        return "Song(id=$id, title='$title', length=$length, album=${album.title})"
    }
}
