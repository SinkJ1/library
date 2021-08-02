package sinkj1.library.service.mapper;

import org.mapstruct.*;
import sinkj1.library.domain.*;
import sinkj1.library.service.dto.PurchaseDTO;

/**
 * Mapper for the entity {@link Purchase} and its DTO {@link PurchaseDTO}.
 */
@Mapper(componentModel = "spring", uses = { BookMapper.class, CustomerMapper.class })
public interface PurchaseMapper extends EntityMapper<PurchaseDTO, Purchase> {
    @Mapping(target = "book", source = "book", qualifiedByName = "name")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "name")
    PurchaseDTO toDto(Purchase s);
}
