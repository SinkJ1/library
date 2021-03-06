package sinkj1.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import sinkj1.library.domain.Book;

/**
 * Spring Data SQL repository for the Book entity.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')")
    @Query(
        value = "select distinct book from Book book left join fetch book.authors where book.id in :booksIds",
        countQuery = "select count(distinct book) from Book book"
    )
    List<Book> findAllWithEagerRelationships(@Param("booksIds")List<Long> booksIds);

    @Query(
        value = "select distinct book from Book book left join fetch book.authors",
        countQuery = "select count(distinct book) from Book book"
    )
    List<Book> findAllWithEagerRelationships();

    //@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'WRITE') or hasPermission(filterObject, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')")
    @Query(value = "select distinct book from Book book left join fetch book.authors where book.id in :booksIds")
    List<Book> findAll(@Param("booksIds")List<Long> booksIds);

    @Query(value = "select distinct book from Book book left join fetch book.authors")
    List<Book> findAll();

    @PostAuthorize("hasPermission(returnObject, 'READ') or hasPermission(returnObject, 'WRITE') or hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')")
    @Query("select book from Book book left join fetch book.authors where book.id =:id")
    Optional<Book> findOneWithEagerRelationships(@Param("id") Long id);
}
