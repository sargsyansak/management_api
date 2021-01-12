package am.management.management_api.repository;

import am.management.management_api.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "delete from comment where comment_id = :commentId", nativeQuery = true)
    void deleteComment(@Param("commentId") long commentId);
}
