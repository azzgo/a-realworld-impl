package how.realworld.server.repository

import how.realworld.server.repository.mapper.TagId
import how.realworld.server.repository.mapper.TagMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: JpaRepository<TagMapper, TagId>