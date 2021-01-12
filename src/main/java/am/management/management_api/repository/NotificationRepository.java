package am.management.management_api.repository;


import am.management.management_api.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Optional<NotificationEntity> findByComment_CommentId(long commentId);
}
