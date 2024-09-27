package com.farhanalwahid.backend.controller;

import com.farhanalwahid.backend.exception.ResourceNotFoundException;
import com.farhanalwahid.backend.model.BorrowingTransaction;
import com.farhanalwahid.backend.repository.BookRepository;
import com.farhanalwahid.backend.repository.BorrowingTransactionRepository;
import com.farhanalwahid.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:5173/")
public class BorrowingTransactionController {

    private final BorrowingTransactionRepository borrowingTransactionRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BorrowingTransactionController(BorrowingTransactionRepository borrowingTransactionRepository,
                                          BookRepository bookRepository, UserRepository userRepository) {
        this.borrowingTransactionRepository = borrowingTransactionRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/borrowing-transactions")
    public BorrowingTransaction newBorrowingTransaction(@RequestBody BorrowingTransaction newBorrowingTransaction) {
        return borrowingTransactionRepository.save(newBorrowingTransaction);
    }

    @GetMapping("/borrowing-transactions")
    public List<BorrowingTransaction> getAllBorrowingTransactions() {
        return borrowingTransactionRepository.findAll();
    }

    @GetMapping("/borrowing-transactions/{id}")
    public BorrowingTransaction getBorrowingTransactionById(@PathVariable Long id) {
        return borrowingTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing Transaction", "id", id));
    }

    @PutMapping("/borrowing-transactions/{id}")
    public BorrowingTransaction updateBorrowingTransaction(
            @RequestBody Map<String, Object> requestBody,
            @PathVariable Long id) {

        return borrowingTransactionRepository.findById(id)
                .map(borrowingTransaction -> {
                    if (requestBody.containsKey("status")) {
                        borrowingTransaction.setStatus((String) requestBody.get("status"));
                    }

                    if (requestBody.containsKey("book")) {
                        Object bookObject = requestBody.get("book");
                        if (bookObject instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> bookData = (Map<String, Object>) bookObject;
                            Long bookId = Long.valueOf(bookData.get("id").toString());
                            borrowingTransaction.setBook(bookRepository.findById(bookId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId)));
                        }
                    }

                    if (requestBody.containsKey("user")) {
                        Object userObject = requestBody.get("user");
                        if (userObject instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> userData = (Map<String, Object>) userObject;
                            Long userId = Long.valueOf(userData.get("id").toString());
                            borrowingTransaction.setUser(userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)));
                        }
                    }

                    if (requestBody.containsKey("borrowDate")) {
                        LocalDate borrowDate = LocalDate.parse(requestBody.get("borrowDate").toString());
                        borrowingTransaction.setBorrowDate(borrowDate);
                    }

                    if (requestBody.containsKey("returnDate")) {
                        LocalDate returnDate = LocalDate.parse(requestBody.get("returnDate").toString());
                        borrowingTransaction.setReturnDate(returnDate);
                    }

                    return borrowingTransactionRepository.save(borrowingTransaction);
                }).orElseThrow(() -> new ResourceNotFoundException("BorrowingTransaction", "id", id));
    }

    @DeleteMapping("/borrowing-transactions/{id}")
    public String deleteBorrowingTransaction(@PathVariable Long id) {
        if (!borrowingTransactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Borrowing Transaction", "id", id);
        }
        borrowingTransactionRepository.deleteById(id);
        return "Borrowing Transaction with id " + id + " has been deleted successfully.";
    }
}