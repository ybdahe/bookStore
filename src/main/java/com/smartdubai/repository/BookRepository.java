package com.smartdubai.repository;
import org.springframework.data.repository.CrudRepository;
//repository that extends CrudRepository
import com.smartdubai.model.Book;
public interface BookRepository extends CrudRepository<Book, Integer>
{
}
