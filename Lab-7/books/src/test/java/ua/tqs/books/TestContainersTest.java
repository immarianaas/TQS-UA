package ua.tqs.books;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class TestContainersTest {
    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
        .withUsername("admin")
        .withPassword("admin")
        .withDatabaseName("tqs1");

    @Autowired
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    void contextLoads() {
        Book book = new Book();
        book.setName("Testcontainers");

        bookRepository.save(book);

        List<Book> livrosDaBd = bookRepository.findAll();

        assertTrue(book.getName().equals(livrosDaBd.get(0).getName()));
    }

}
