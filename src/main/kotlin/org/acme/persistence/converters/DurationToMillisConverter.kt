package org.acme.persistence.converters

import java.time.Duration
import javax.persistence.AttributeConverter

class DurationToMillisConverter : AttributeConverter<Duration, Long> {
    override fun convertToDatabaseColumn(duration: Duration): Long = duration.toMillis()
    override fun convertToEntityAttribute(long: Long): Duration = Duration.ofMillis(long)
}