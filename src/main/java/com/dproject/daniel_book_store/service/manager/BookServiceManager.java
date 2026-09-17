package com.dproject.daniel_book_store.service.manager;

import com.dproject.daniel_book_store.dto.output.BookCategoryOutput;
import com.dproject.daniel_book_store.dto.output.UpdateBookOutput;
import com.dproject.daniel_book_store.dto.projection.BookProjection;
import com.dproject.daniel_book_store.dto.request.BookRequest;
import com.dproject.daniel_book_store.helper.UpdateBookHelper;
import com.dproject.daniel_book_store.helper.core.ErrorEnum;
import com.dproject.daniel_book_store.helper.exception.CustomException;
import com.dproject.daniel_book_store.model.ActivityLog;
import com.dproject.daniel_book_store.model.Book;
import com.dproject.daniel_book_store.model.Category;
import com.dproject.daniel_book_store.repository.ActivityLogRepository;
import com.dproject.daniel_book_store.repository.BookRepository;
import com.dproject.daniel_book_store.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceManager {
    private final BookRepository bookRepository;
    private final ActivityLogRepository logRepo;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BookCategoryOutput createBookCategory(BookRequest book){
        log.info("Starting insert new book data");

        var bookNameExisted = bookRepository.findByBookName(book.getBookName());
        var bookAuthorExisted = bookRepository.findByAuthor(book.getAuthor());
        var isExist = false;
        if(!bookNameExisted.isEmpty() && !bookAuthorExisted.isEmpty())
            isExist = true;

        log.debug("Book name: {}, is exist: {}", book.getBookName(), isExist);
        log.debug("Book author: {}, is exist: {}", book.getAuthor(), isExist);

        if(!bookNameExisted.isEmpty() && !bookAuthorExisted.isEmpty())
            throw new CustomException(ErrorEnum.DUPLICATE_DATA);

        Category category = categoryRepository.findById(book.getCategoryId())
                .orElseThrow(()-> new CustomException(ErrorEnum.ERROR_NOT_FOUNT));

        Book bookNew = new Book();
        bookNew.setBookName(book.getBookName());
        bookNew.setAuthor(book.getAuthor());
        bookNew.setPrice(book.getPrice());
        bookNew.setCreate_date(LocalDateTime.now());
        bookNew.setUpdate_date(LocalDateTime.now());
        bookNew.setCategory(category);

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
        return BookCategoryOutput.builder()
                .bookId(dataCreated.getId())
                .bookName(dataCreated.getBookName())
                .author(dataCreated.getAuthor())
                .price(dataCreated.getPrice())
                .categoryId(dataCreated.getCategory().getId())
                .categoryName(dataCreated.getCategory().getName())
                .build();
    }

    @Transactional
    public UpdateBookOutput updateBookCategory(String id, BookRequest bookRequest){
        log.info("Starting editing book data");
        Book bookEdit = bookRepository.findById(id).orElseThrow(()-> new CustomException(ErrorEnum.ERROR_NOT_FOUNT));


        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(()-> new CustomException(ErrorEnum.ERROR_NOT_FOUNT));

        var bookTemp = new Book();
        BeanUtils.copyProperties(bookEdit,bookTemp);
        bookEdit.setBookName(bookRequest.getBookName());
        bookEdit.setAuthor(bookRequest.getAuthor());
        bookEdit.setPrice(bookRequest.getPrice());
        bookEdit.setUpdate_date(LocalDateTime.now());
        bookEdit.setCategory(category);

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
        BookCategoryOutput newDataDto = BookCategoryOutput.builder()
                .bookId(newBook.getId())
                .bookName(newBook.getBookName())
                .author(newBook.getAuthor())
                .price(newBook.getPrice())
                .categoryId(newBook.getCategory().getId())
                .categoryName(newBook.getCategory().getName())
                .build();

        BookCategoryOutput oldDataDto = BookCategoryOutput.builder()
                .bookId(bookTemp.getId())
                .bookName(bookTemp.getBookName())
                .author(bookTemp.getAuthor())
                .price(bookTemp.getPrice())
                .categoryId(bookTemp.getCategory().getId())
                .categoryName(bookTemp.getCategory().getName())
                .build();

        return UpdateBookOutput.builder()
                .oldData(oldDataDto)
                .newData(newDataDto)
                .build();
    }

    @Transactional
    public BookProjection deleteBook(String id) {
        log.info("Starting Delete book");
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorEnum.ERROR_NOT_FOUNT));

        BookProjection deleteData = BookProjection.builder()
                .bookId(book.getId())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .price(book.getPrice())
                .categoryId(book.getCategory().getId())
                .categoryName(book.getCategory().getName())
                .build();

        log.debug("fetch book will be deleted : {}", deleteData);
        bookRepository.deleteById(id);
        log.info("Finish deleting book data");
        return deleteData;
    }
}
