package co.ke.xently.demo.books.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    private static BookDto getBookDto(Book book) {
        return new BookDto(book.getTitle(), book.getAuthor());
    }

    public List<BookDto> getAllBooks() {
        try {
            Thread.sleep(new Random().nextLong(100, 5000));
        } catch (InterruptedException ignored) {
        }
        return bookRepository.findAll()
                .stream()
                .map(BookService::getBookDto)
                .toList();
    }
}
