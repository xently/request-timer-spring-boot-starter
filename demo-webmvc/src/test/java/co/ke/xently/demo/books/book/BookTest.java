package co.ke.xently.demo.books.book;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void testBookAccessors() {
        var book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("Title");
        assertThat(book.getAuthor()).isEqualTo("Author");
    }

    @Test
    void testBookBuilder() {
        var book = Book.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .build();

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("Title");
        assertThat(book.getAuthor()).isEqualTo("Author");
    }

    @Test
    void testBookAllArgsConstructor() {
        var book = new Book(1L, "Title", "Author");
        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("Title");
        assertThat(book.getAuthor()).isEqualTo("Author");
    }

    @Test
    void testBookDto() {
        var dto = new BookDto("Title", "Author");
        assertThat(dto.title()).isEqualTo("Title");
        assertThat(dto.author()).isEqualTo("Author");
    }
}
