package com.example.article.repository;

import com.example.article.entity.ArticleFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleFileRepository extends CrudRepository<ArticleFile,Long> {
    List<ArticleFile> findAllByArticleId(Long articleId);
}
