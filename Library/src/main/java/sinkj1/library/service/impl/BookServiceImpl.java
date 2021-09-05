package sinkj1.library.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import sinkj1.library.domain.Book;
import sinkj1.library.domain.DeletePermission;
import sinkj1.library.domain.MaskAndObject;
import sinkj1.library.domain.PermissionVM;
import sinkj1.library.repository.BookRepository;
import sinkj1.library.security.jwt.TokenProvider;
import sinkj1.library.service.BookService;
import sinkj1.library.service.PermissionService;
import sinkj1.library.service.dto.BookDTO;
import sinkj1.library.service.dto.DeletePermissionDto;
import sinkj1.library.service.dto.PermissionDto;
import sinkj1.library.service.mapper.BookMapper;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final TokenProvider tokenProvider;

    private final PermissionService permissionService;

    private final BookMapper bookMapper;

    public BookServiceImpl(
        BookRepository bookRepository,
        TokenProvider tokenProvider,
        PermissionService permissionService,
        BookMapper bookMapper
    ) {
        this.bookRepository = bookRepository;
        this.tokenProvider = tokenProvider;
        this.permissionService = permissionService;
        this.bookMapper = bookMapper;
    }

    public Book saveWithPermission(Book book) {
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
        if (book != null) {
            permissionService.addPermissionForUser(book, BasePermission.ADMINISTRATION, userName);
        }

        return bookMapper.toDto(book);
    }

    public Optional<BookDTO> updateWithPermission(Book book) {
        return bookRepository.findById(book.getId()).map(bookRepository::save).map(bookMapper::toDto);
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
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Book> bookList;

        if (checkPermission(authentication)) {
            bookList = bookRepository.findAllWithEagerRelationships();
        } else {
            bookList = bookRepository.findAllWithEagerRelationships(getBookIds());
        }
        Page<Book> page = listConvertToPage(bookList, pageable);

        return page.map(bookMapper::toDto);
    }

    public Page<BookDTO> findAllWithEagerRelationships(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Book> bookList;

        if (checkPermission(authentication)) {
            bookList = bookRepository.findAllWithEagerRelationships();
        } else {
            bookList = bookRepository.findAllWithEagerRelationships(getBookIds());
        }
        Page<Book> page = listConvertToPage(bookList, pageable);

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

    @Override
    public void addPermissions(List<PermissionVM> permissionVMS) {
        List<PermissionDto> permissionDtos = new ArrayList<>();
        for (PermissionVM permissionVM : permissionVMS) {
            permissionDtos.add(
                new PermissionDto(
                    permissionVM.getEntityId(),
                    Book.class.getName(),
                    convertFromStringToBasePermission(permissionVM.getPermission()).getMask(),
                    permissionVM.getUserCredentional()
                )
            );
        }
        permissionService.addPermissions(permissionDtos);
    }

    @Override
    public void deletePermission(PermissionVM permissionVM) {
        DeletePermissionDto deletePermissionDto = new DeletePermissionDto();
        deletePermissionDto.setEntityId(permissionVM.getEntityId());
        deletePermissionDto.setUser(permissionVM.getUserCredentional());
        deletePermissionDto.setPermission(convertFromStringToIntPermission(permissionVM.getPermission()));
        deletePermissionDto.setEntityClassName(Book.class.getName());
        permissionService.deletePermission(deletePermissionDto);
    }

    private boolean checkPermission(Authentication authentication) {
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        List<String> authoritiesStrings = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (authentication.getName().equalsIgnoreCase("admin") || authoritiesStrings.contains("ROLE_ADMIN")) {
            return true;
        }
        return false;
    }

    private List<Long> getBookIds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenProvider.createToken(authentication, false);

        WebClient webClient = WebClient.create("https://practice.sqilsoft.by/internship/yury_sinkevich/acl");
        Flux<MaskAndObject> employeeMap = webClient
            .get()
            .uri("/api/get-acl-entries?objE=sinkj1.library.domain.Book")
            .headers(
                httpHeaders -> {
                    httpHeaders.set("Authorization", "Bearer " + token);
                    httpHeaders.set("X-TENANT-ID", "yuradb");
                }
            )
            .retrieve()
            .bodyToFlux(MaskAndObject.class);
        return employeeMap.collectList().block().stream().map(MaskAndObject::getObjId).collect(Collectors.toList());
    }

    private Permission convertFromStringToBasePermission(String permission) {
        switch (permission.toUpperCase()) {
            case "WRITE":
                return BasePermission.WRITE;
            case "ADMINISTRATION":
                return BasePermission.ADMINISTRATION;
            case "CREATE":
                return BasePermission.CREATE;
            case "DELETE":
                return BasePermission.DELETE;
            default:
                return BasePermission.READ;
        }
    }

    private int convertFromStringToIntPermission(String permission) {
        switch (permission.toUpperCase()) {
            case "WRITE":
                return 2;
            case "ADMINISTRATION":
                return 16;
            case "CREATE":
                return 4;
            case "DELETE":
                return 8;
            default:
                return 1;
        }
    }
}
