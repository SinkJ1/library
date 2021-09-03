package sinkj1.library.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sinkj1.library.domain.Book;
import sinkj1.library.domain.DeletePermission;
import sinkj1.library.domain.MaskAndObject;
import sinkj1.library.domain.PermissionVM;
import sinkj1.library.service.dto.BookDTO;
import sinkj1.library.service.dto.DeletePermissionDto;

/**
 * Service Interface for managing {@link sinkj1.library.domain.Book}.
 */
public interface BookService {
    /**
     * Save a book.
     *
     * @param bookDTO the entity to save.
     * @return the persisted entity.
     */
    BookDTO save(Book bookDTO);

    /**
     * Partially updates a book.
     *
     * @param bookDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookDTO> partialUpdate(Book bookDTO);

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookDTO> findAll(Pageable pageable);

    /**
     * Get all the books with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" book.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookDTO> findOne(Long id);

    /**
     * Delete the "id" book.
     *
     * @param id the id of the entity.
     */
    void delete(Book book);

    void addPermissions(List<PermissionVM> permissionVMS);

    void deletePermission(DeletePermission deletePermissionDto);
}
