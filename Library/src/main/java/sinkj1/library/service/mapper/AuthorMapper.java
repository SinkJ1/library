package sinkj1.library.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import sinkj1.library.domain.*;
import sinkj1.library.service.dto.AuthorDTO;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<AuthorDTO> toDtoNameSet(Set<Author> author);
}
