package sinkj1.library.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.library.domain.Book;
import sinkj1.library.repository.BookRepository;
import sinkj1.library.service.BookService;
import sinkj1.library.service.PermissionService;
import sinkj1.library.service.dto.BookDTO;
import sinkj1.library.service.mapper.BookMapper;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final PermissionService permissionService;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, PermissionService permissionService, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.permissionService = permissionService;
        this.bookMapper = bookMapper;
    }


    public Book saveWithPermission(Book book){
        return bookRepository.save(book);
    }


    @Override
    @PreAuthorize("hasPermission(#book, 'CREATE') or hasPermission(#book, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')")
    public BookDTO save(Book book) {
        log.debug("Request to save Book : {}", book);
        book.setId(null);
        book = saveWithPermission(book);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if (book != null){
            permissionService.addPermissionForUser(book, BasePermission.ADMINISTRATION, userName);
        }

        return bookMapper.toDto(book);
    }

    public Optional<BookDTO> updateWithPermission(Book book){
        return bookRepository
            .findById(book.getId())
            .map(bookRepository::save)
            .map(bookMapper::toDto);
    }

    @Override
    @PreAuthorize("hasPermission(#book, 'WRITE') or hasPermission(#book, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')")
    public Optional<BookDTO> partialUpdate(Book book) {
        log.debug("Request to partially update Book : {}", book);
        BookDTO bookDTO = bookMapper.toDto(bookRepository.save(book));
        Optional<BookDTO> optionalBookDTO = Optional.ofNullable(bookDTO);
        return optionalBookDTO;
    }


    private <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        List<Book> book = bookRepository.findAllWithEagerRelationships();
        Page<Book> page = listConvertToPage(book,pageable);

        return page.map(bookMapper::toDto);
    }


    public Page<BookDTO> findAllWithEagerRelationships(Pageable pageable) {
        List<Book> book = bookRepository.findAllWithEagerRelationships();
        Page<Book> page = listConvertToPage(book,pageable);
        return page.map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDTO> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findOneWithEagerRelationships(id).map(bookMapper::toDto);
    }

    @Override
    @PostAuthorize("hasPermission(#book, 'DELETE') or hasPermission(#book, 'ADMINISTRATION') or hasAuthority('ROLE_ADMIN')")
    public void delete(Book book) {
        log.debug("Request to delete Book : {}", book.getId());
        bookRepository.deleteById(book.getId());
    }
}
