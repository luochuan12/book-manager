package com.bookmanager.mapper;

import com.bookmanager.entity.Book;
import org.apache.ibatis.annotations.*;

import javax.swing.*;
import java.util.List;

@Mapper
public interface BookMapper {

    @Select("SELECT id, name, author, price FROM book")
    List<Book> findAll();

    @Select("SELECT id, name, author, price FROM book WHERE id = #{id}")
    Book findById(Integer id);

    @Select("SELECT id,name,author,price FROM book WHERE name LIKE concat('%',#{name},'%')")
    List<Book> searchByName(String name);

    @Insert("INSERT INTO book(name, author, price) VALUES(#{name}, #{author}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Book book);

    @Update("UPDATE book SET name=#{name}, author=#{author}, price=#{price} WHERE id=#{id}")
    int update(Book book);

    @Delete("DELETE FROM book WHERE id=#{id}")
    int deleteById(Integer id);
}
