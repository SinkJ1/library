package sinkj1.library.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sinkj1.library.service.dto.AuthorDTO;

/**
 * Service Interface for managing {@link sinkj1.library.domain.Author}.
 */
public interface AuthorService {
    /**
     * Save a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    AuthorDTO save(AuthorDTO authorDTO);

    /**
     * Partially updates a author.
     *
     * @param authorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO);

    /**
     * Get all the authors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuthorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" author.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuthorDTO> findOne(Long id);

    /**
     * Delete the "id" author.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
