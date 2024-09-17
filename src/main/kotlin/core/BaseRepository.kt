package core

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository : JpaRepository<BaseEntity, Long> , JpaSpecificationExecutor<BaseEntity> {
    fun findAllByStatusIsTrue(): List<BaseEntity>
}