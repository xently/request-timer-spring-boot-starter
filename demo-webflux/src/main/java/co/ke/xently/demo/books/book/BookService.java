package co.ke.xently.demo.books.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    private static BookDto getBookDto(Book book) {
        return new BookDto(book.getTitle(), book.getAuthor());
    }

    public Flux<BookDto> getAllBooks() {
        return Mono.fromCallable(() -> bookRepository.findAll())
                .flatMapMany(Flux::fromIterable)
                .map(BookService::getBookDto)
                .delayElements(Duration.ofMillis(new Random().nextInt(100, 5000)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
