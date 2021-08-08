package org.acme.persistence.one.to.many.unidirectional

import org.acme.persistence.converters.DurationToMillisConverter
import java.time.Duration
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "SONG_UNIDIRECTIONAL")
data class SongUnidirectional(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SONG_ID", nullable = false, updatable = false)
    var id: Long? = null,

    var title: String,

    @Column(name = "LENGTH_MILLIS")
    @Convert(converter = DurationToMillisConverter::class)
    var length: Duration,

    // Replaced the reference entity with just an ID
    @Column(name = "PARENT_ALBUM_ID") // We added PARENT_ to make explicit this column is a foreign key
    var albumId: Long  // This holds the reference to the parent (ALBUM_UNI) table, it's the column which gets joined
)
