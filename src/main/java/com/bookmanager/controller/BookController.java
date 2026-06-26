package com.bookmanager.controller;

import com.bookmanager.entity.Book;
import com.bookmanager.service.BookService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;
    private final StringRedisTemplate redisTemplate;

    public BookController(BookService bookService, StringRedisTemplate redisTemplate) {
        this.bookService = bookService;
        this.redisTemplate = redisTemplate;
    }

    // 查全部
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // 查一个
    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam String name){
        return bookService.searchByName(name);
    }

    // 新增
    @PostMapping("/books")
    public String addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return "新增成功！id=" + book.getId();
    }

    // 修改
    @PutMapping("/books/{id}")
    public String updateBook(@PathVariable Integer id, @RequestBody Book book) {
        book.setId(id);
        bookService.updateBook(book);
        return "修改成功！";
    }

    // 删除
    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return "删除成功！";
    }

    // Redis 连接测试
    @GetMapping("/redis-test")
    public String redisTest() {
        try {
            redisTemplate.opsForValue().set("test", "hello");
            return redisTemplate.opsForValue().get("test");
        } catch (Exception e) {
            return "Redis连接失败: " + e.getMessage();
        }
    }
}
