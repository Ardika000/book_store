package com.dproject.daniel_book_store.service;

import com.dproject.daniel_book_store.helper.UpdateBookHelper;
import com.dproject.daniel_book_store.helper.exception.CustomException;
import com.dproject.daniel_book_store.helper.core.ErrorEnum;
import com.dproject.daniel_book_store.model.ActivityLog;
import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.repository.ActivityLogRepository;
import com.dproject.daniel_book_store.repository.BookRepository;
import com.dproject.daniel_book_store.dto.request.BookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final ActivityLogRepository logRepo;

    public Page<Book> getAllDataBook(LocalDate minDate, LocalDate maxDate, int page, int size, String sortBy) {
        log.info("Fetching books with date filter: minDate={}, maxDate={}", minDate, maxDate);
        LocalDateTime start = (minDate != null) ? minDate.atStartOfDay() : null;
        LocalDateTime end = (maxDate != null) ? maxDate.atTime(LocalTime.MAX):null;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        var result = bookRepository.filterByDateRange(start, end, pageable);
        log.info("SUccessfully fetched {} booksfrom database", result.getSize());

        return result;
    }

    public List<Book> getByNameAndAuthor(String bookName, String author) {
        log.info("Fetching books with data: bookName={}, author={} ", bookName, author);
        var res = bookRepository.findByBookNameAndAuthor(bookName, author);
        log.info("SUccessfully fetched {} booksfrom database", res);
        return res;
    }

    public List<Book> getByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> getByBook(String bookName) {
        return bookRepository.findByBookName(bookName);
    }

    public List<Book> addBook(BookRequest book) {
        Book bookNew = new Book();
        bookNew.setBookName(book.getBookName());
        bookNew.setAuthor(book.getAuthor());
        bookNew.setPrice(book.getPrice());
        bookNew.setCreate_date(LocalDateTime.now());
        bookNew.setUpdate_date(LocalDateTime.now());

        // saving data
        Book dataCreated = bookRepository.save(bookNew);

        // looging save
        ActivityLog logInsert = new ActivityLog();
        logInsert.setBookNameNew(dataCreated.getBookName());
        logInsert.setActivity("insert data");
        logInsert.setAuthorNew(dataCreated.getAuthor());
        logInsert.setPriceNew(dataCreated.getPrice());
        logInsert.setCreateDate(LocalDateTime.now());
        logRepo.save(logInsert);

        return bookRepository.findAll();
    }


    public UpdateBookHelper editBook(String id, BookRequest bookRequest) {
        Book bookEdit = bookRepository.findById(id).orElseThrow(()-> new CustomException(ErrorEnum.ERROR_NOT_FOUNT));
        var bookTemp = new Book();
        BeanUtils.copyProperties(bookEdit,bookTemp);
        bookEdit.setBookName(bookRequest.getBookName());
        bookEdit.setAuthor(bookRequest.getAuthor());
        bookEdit.setPrice(bookRequest.getPrice());
        bookEdit.setUpdate_date(LocalDateTime.now());

        // save data
        Book newBook =  bookRepository.save(bookEdit);
        UpdateBookHelper newOldData = new UpdateBookHelper(bookTemp, newBook);

        // looging
        ActivityLog logInsert = new ActivityLog();
        logInsert.setActivity("update data");
        logInsert.setBookNameOld(newOldData.getOldBook().getBookName());
        logInsert.setBookNameNew(newOldData.getNewBook().getBookName());
        logInsert.setAuthorOld(newOldData.getOldBook().getAuthor());
        logInsert.setAuthorNew(newOldData.getNewBook().getAuthor());
        logInsert.setPriceOld(newOldData.getOldBook().getPrice());
        logInsert.setPriceNew(newOldData.getNewBook().getPrice());
        logInsert.setCreateDate(LocalDateTime.now());
        logRepo.save(logInsert);

        return newOldData;
    }

    public Book deleteBook(String id) {
        Book getBookSelected = bookRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorEnum.ERROR_NOT_FOUNT));

        Book tempBook = new Book();
        BeanUtils.copyProperties(getBookSelected, tempBook);

        bookRepository.deleteById(id);
        return tempBook;
    }

    public Book getBookById(String id) {
        log.info("Searching for book with ID {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book Search failed. ID not found: {}", id);
                    return new CustomException(ErrorEnum.ERROR_NOT_FOUNT);
                });
    }
}
