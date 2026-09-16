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
        log.debug("Number of fetched data {}", result.getSize());
        log.info("Successfully get all data");
        return result;
    }

    public List<Book> getByNameAndAuthor(String bookName, String author) {
        log.info("Fetching books with data: bookName={}, author={} ", bookName, author);
        var res = bookRepository.findByBookNameAndAuthor(bookName, author);
        log.debug("Number of fetched data {}", res.size());
        log.info("Successfully get all data by name & author");
        return res;
    }

    public List<Book> getByAuthor(String author) {
        var res = bookRepository.findByAuthor(author);
        log.debug("Number of fetched data {}", res.size());
        log.info("Successfully get all data by author");
        return res;
    }

    public List<Book> getByBook(String bookName) {
        var res = bookRepository.findByBookName(bookName);
        log.debug("Number of fetched data {}", res.size());
        log.info("Successfully get all data by author");
        return res;
    }

    public List<Book> addBook(BookRequest book) {
        log.info("Starting insert new book data");

        var bookNameExisted = bookRepository.findByBookName(book.getBookName());
        var bookAuthorExisted = bookRepository.findByAuthor(book.getAuthor());

        log.debug("Book name: {}, is exist: {}", book.getBookName(), bookNameExisted);
        log.debug("Book author: {}, is exist: {}", book.getAuthor(), bookAuthorExisted);

        if(!bookNameExisted.isEmpty() && !bookAuthorExisted.isEmpty())
            throw new CustomException(ErrorEnum.DUPLICATE_DATA);

        Book bookNew = new Book();
        bookNew.setBookName(book.getBookName());
        bookNew.setAuthor(book.getAuthor());
        bookNew.setPrice(book.getPrice());
        bookNew.setCreate_date(LocalDateTime.now());
        bookNew.setUpdate_date(LocalDateTime.now());

        log.debug("Inserting new book data: {}", bookNew);
        // saving data
        Book dataCreated = bookRepository.save(bookNew);
        log.info("Finish inserting book data");

        // looging save
        log.info("Starting saving data to activity log");
        ActivityLog logInsert = new ActivityLog();
        logInsert.setBookNameNew(dataCreated.getBookName());
        logInsert.setActivity("insert data");
        logInsert.setAuthorNew(dataCreated.getAuthor());
        logInsert.setPriceNew(dataCreated.getPrice());
        logInsert.setCreateDate(LocalDateTime.now());

        log.debug("Inserting data insert to activity log : {}", logInsert);
        logRepo.save(logInsert);
        log.info("Finish inserting data insert to activity log");
        return bookRepository.findAll();
    }


    public UpdateBookHelper editBook(String id, BookRequest bookRequest) {
        log.info("Starting editing book data");
        Book bookEdit = bookRepository.findById(id).orElseThrow(()-> new CustomException(ErrorEnum.ERROR_NOT_FOUNT));
        var bookTemp = new Book();
        BeanUtils.copyProperties(bookEdit,bookTemp);
        bookEdit.setBookName(bookRequest.getBookName());
        bookEdit.setAuthor(bookRequest.getAuthor());
        bookEdit.setPrice(bookRequest.getPrice());
        bookEdit.setUpdate_date(LocalDateTime.now());

        log.debug("Book data to update: {}", bookEdit);
        // save data
        Book newBook =  bookRepository.save(bookEdit);
        log.info("Finish inserting book data");
        UpdateBookHelper newOldData = new UpdateBookHelper(bookTemp, newBook);

        // looging
        log.info("Starting saving data to activity log");
        ActivityLog logInsert = new ActivityLog();
        logInsert.setActivity("update data");
        logInsert.setBookNameOld(newOldData.getOldBook().getBookName());
        logInsert.setBookNameNew(newOldData.getNewBook().getBookName());
        logInsert.setAuthorOld(newOldData.getOldBook().getAuthor());
        logInsert.setAuthorNew(newOldData.getNewBook().getAuthor());
        logInsert.setPriceOld(newOldData.getOldBook().getPrice());
        logInsert.setPriceNew(newOldData.getNewBook().getPrice());
        logInsert.setCreateDate(LocalDateTime.now());
        log.debug("Inserting data update to activity log : {}", logInsert);
        logRepo.save(logInsert);
        log.info("Finish inserting data update to activity log");
        return newOldData;
    }

    public Book deleteBook(String id) {
        log.info("Starting deleting book data");
        Book getBookSelected = bookRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorEnum.ERROR_NOT_FOUNT));

        Book tempBook = new Book();
        BeanUtils.copyProperties(getBookSelected, tempBook);
        log.debug("Book data to be deleted: {}", tempBook);

        bookRepository.deleteById(id);
        log.info("Finish deleting book data");
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
