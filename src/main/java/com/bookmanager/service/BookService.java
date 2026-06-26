package com.bookmanager.service;

import com.bookmanager.entity.Book;
import com.bookmanager.mapper.BookMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookService {

    private final BookMapper bookMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public BookService(BookMapper bookMapper, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.bookMapper = bookMapper;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        System.out.println("bookMapper 真实类型: " + bookMapper.getClass().getName());
    }

    // ========== 查询（走缓存） ==========

    public List<Book> getAllBooks() {
        try {
            String cached = redisTemplate.opsForValue().get("books:all");
            if (cached != null) {
                System.out.println(">>> 从缓存读取");
                return objectMapper.readValue(cached, new TypeReference<List<Book>>() {});
            }

            System.out.println(">>> 从数据库读取");
            List<Book> books = bookMapper.findAll();

            String json = objectMapper.writeValueAsString(books);
            redisTemplate.opsForValue().set("books:all", json, 60, TimeUnit.SECONDS);

            return books;
        } catch (Exception e) {
            System.out.println("缓存异常，降级到数据库: " + e.getMessage());
            return bookMapper.findAll();
        }
    }

    public Book getBookById(Integer id) {
        return bookMapper.findById(id);
    }

    public List<Book> searchByName(String name){
        return bookMapper.searchByName(name);
    }

    // ========== 写入（清缓存） ==========

    public int addBook(Book book) {
        int result = bookMapper.insert(book);
        clearCache("新增");
        return result;
    }

    public int updateBook(Book book) {
        int result = bookMapper.update(book);
        clearCache("修改");
        return result;
    }

    public int deleteBook(Integer id) {
        int result = bookMapper.deleteById(id);
        clearCache("删除");
        return result;
    }

    // ========== 缓存辅助方法 ==========

    private void clearCache(String action) {
        try {
            redisTemplate.delete("books:all");
            System.out.println(">>> 缓存已清除（" + action + "操作）");
        } catch (Exception e) {
            System.out.println("清除缓存失败: " + e.getMessage());
        }
    }
}
