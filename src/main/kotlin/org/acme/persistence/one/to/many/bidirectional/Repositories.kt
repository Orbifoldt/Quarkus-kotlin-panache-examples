package org.acme.persistence.one.to.many.bidirectional

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BandRepository: PanacheRepository<Band> {
    fun findFirstByName(name: String): Band = find("name", name).firstResult()
}

@ApplicationScoped
class AlbumRepository: PanacheRepository<Album>

@ApplicationScoped
class SongRepository: PanacheRepository<Song>