package com.example.article.repository;

import com.example.article.entity.ArticleFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleFileRepository extends CrudRepository<ArticleFile,Long> {
    List<ArticleFile> findAllByArticleId(Long articleId);

    @Query("SELECT af FROM ArticleFile af WHERE af.article.id = :articleId AND af.type LIKE %:type%")
    List<ArticleFile> findAllByArticleIdAndTypeContaining(@Param("articleId") Long articleId, @Param("type") String type);

    @Query("SELECT af FROM ArticleFile af WHERE af.article.id = :articleId AND af.type NOT LIKE %:type%")
    List<ArticleFile> findAllByArticleIdAndTypeNotContaining(@Param("articleId") Long articleId, @Param("type") String type);
}
