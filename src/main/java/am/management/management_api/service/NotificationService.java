package am.management.management_api.service;


import am.management.management_api.entity.NotificationEntity;

import java.util.List;

public interface NotificationService {

    NotificationEntity getById(long id);

    NotificationEntity save(NotificationEntity notificationEntity);

    void delete(long id);

    List<NotificationEntity> get(int page, int perPage);

    NotificationEntity updateNotification(NotificationEntity notificationEntity);
}
