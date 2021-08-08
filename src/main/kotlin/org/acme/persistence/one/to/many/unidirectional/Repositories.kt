package org.acme.persistence.one.to.many.unidirectional

import io.quarkus.hibernate.orm.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BandUniRepository: PanacheRepository<BandUnidirectional> {
    fun findFirstByName(name: String): BandUnidirectional = find("name", name).firstResult()
}

@ApplicationScoped
class AlbumUniRepository: PanacheRepository<AlbumUnidirectional>

@ApplicationScoped
class SongUniRepository: PanacheRepository<SongUnidirectional>