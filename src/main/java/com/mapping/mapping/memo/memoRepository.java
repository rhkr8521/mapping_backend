package com.mapping.mapping.memo;

import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface memoRepository extends CrudRepository<memo, Long> {
    List<memo> findByWriter(String writer);
    List<memo> findByTag(String tag);
    List<memo> findByContentContains(String content);
}
