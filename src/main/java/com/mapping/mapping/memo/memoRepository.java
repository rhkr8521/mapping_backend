package com.mapping.mapping.memo;

import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface memoRepository extends CrudRepository<memo, Long>{
	
    List<memo> findByWriter(String writer);
}