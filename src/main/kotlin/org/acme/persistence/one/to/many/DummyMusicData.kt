package org.acme.persistence.one.to.many

import java.time.Duration

data class SongData(val title: String, val length: Duration)

private fun duration(minutes: Int, seconds: Int): Duration = Duration.ofSeconds((minutes * 60 + seconds).toLong())

object SongCollections {
    val randomAccessMemoriesSongData = listOf(
        SongData(title = "Give Life Back to Music", length = duration(4, 36)),
        SongData(title = "The Game of Love", length = duration(5, 22)),
        SongData(title = "Giorgio by Moroder", length = duration(9, 5)),
        SongData(title = "Within", length = duration(3, 49)),
        SongData(title = "Instant Crush", length = duration(5, 38)),
        SongData(title = "Lose Yourself to Dance", length = duration(5, 54)),
        SongData(title = "Touch", length = duration(8, 19)),
        SongData(title = "Get Lucky", length = duration(6, 10)),
        SongData(title = "Beyond", length = duration(4, 51)),
        SongData(title = "Motherboard", length = duration(5, 42)),
        SongData(title = "Fragments of Time", length = duration(4, 40)),
        SongData(title = "Doin' it Right", length = duration(4, 12)),
        SongData(title = "Contact", length = duration(6, 24)),
    )

    val humanAfterAllSongData = listOf(
        SongData(title = "Human After All", length = duration(5, 19)),
        SongData(title = "The Prime Time of Your Life", length = duration(4, 23)),
        SongData(title = "Robot Rock ", length = duration(4, 47)),
        SongData(title = "Steam Machine", length = duration(5, 22)),
        SongData(title = "Make Love", length = duration(4, 48)),
        SongData(title = "The Brainwasher", length = duration(4, 8)),
        SongData(title = "On/Off", length = duration(0, 19)),
        SongData(title = "Television Rules the Nation", length = duration(4, 47)),
        SongData(title = "Technologic", length = duration(4, 44)),
        SongData(title = "Emotion", length = duration(6, 57)),
    )

    val wishYouWereHereSongData = listOf(
        SongData(title = "Shine on You Crazy Diamond, Pts. 1-5", length = duration(13, 32)),
        SongData(title = "Welcome to the Machine", length = duration(7, 31)),
        SongData(title = "Have a Cigar", length = duration(5, 7)),
        SongData(title = "Wish You Were Here", length = duration(5, 34)),
        SongData(title = "Shine on You Crazy Diamond, Pts. 6-9", length = duration(12, 29))
    )

    val modalSoulSongData = listOf(
        SongData(title = "Feather", length = duration(2, 55)),
        SongData(title = "Ordinary Joe", length = duration(5, 7)),
        SongData(title = "Reflection Eternal", length = duration(4, 17)),
        SongData(title = "Luv (Sic.) Part 3", length = duration(5, 36)),
        SongData(title = "Music Is Mine", length = duration(4, 20)),
        SongData(title = "Eclipse", length = duration(3, 34)),
        SongData(title = "The Sign", length = duration(4, 49)),
        SongData(title = "Thank You", length = duration(4, 9)),
        SongData(title = "World's End Rhapsody", length = duration(5, 41)),
        SongData(title = "Modal Soul", length = duration(4, 41)),
        SongData(title = "Flowers", length = duration(3, 59)),
        SongData(title = "Sea of Cloud", length = duration(3, 1)),
        SongData(title = "Light on the Land", length = duration(3, 55)),
        SongData(title = "Horizon", length = duration(7, 20)),
    )
}