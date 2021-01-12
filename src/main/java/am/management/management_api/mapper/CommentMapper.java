package am.management.management_api.mapper;


import am.management.management_api.dto.CommentDao;
import am.management.management_api.entity.CommentEntity;
import org.springframework.stereotype.Component;

@Component
@org.mapstruct.Mapper(componentModel = "spring")
public interface CommentMapper extends Mapper<CommentEntity, CommentDao> {
}
