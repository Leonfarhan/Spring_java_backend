package com.farhanalwahid.backend.controller;

import com.farhanalwahid.backend.exception.ResourceNotFoundException;
import com.farhanalwahid.backend.model.BorrowingTransaction;
import com.farhanalwahid.backend.repository.BookRepository;
import com.farhanalwahid.backend.repository.BorrowingTransactionRepository;
import com.farhanalwahid.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestBody BorrowingTransaction newBorrowingTransaction,
            @PathVariable Long id) {
        return borrowingTransactionRepository.findById(id)
                .map(borrowingTransaction -> {
                    borrowingTransaction.setBook(bookRepository.findById(newBorrowingTransaction.getBook().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", newBorrowingTransaction.getBook().getId())));
                    borrowingTransaction.setUser(userRepository.findById(newBorrowingTransaction.getUser().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", newBorrowingTransaction.getUser().getId())));
                    borrowingTransaction.setBorrowDate(newBorrowingTransaction.getBorrowDate());
                    borrowingTransaction.setReturnDate(newBorrowingTransaction.getReturnDate());
                    borrowingTransaction.setStatus(newBorrowingTransaction.getStatus());
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