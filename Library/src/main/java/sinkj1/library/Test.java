package sinkj1.library;

import org.springframework.security.acls.domain.BasePermission;
import sinkj1.library.domain.Book;

public class Test {
    public static void main(String[] args) {
        System.out.println(BasePermission.ADMINISTRATION.getMask());
    }
}
