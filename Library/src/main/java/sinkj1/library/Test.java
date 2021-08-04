package sinkj1.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import sinkj1.library.service.dto.Book;

public class Test {

    private static void checkCast(Class<?> clazz, Object obj) {
        clazz.cast(obj);
    }

    public static void main(String[] args) throws JsonProcessingException {

        Del del = new Del(1);
        Del del2;
        Object o = del;
        del2 = (Del) o;
        System.out.println(del2.a);

    }
}
