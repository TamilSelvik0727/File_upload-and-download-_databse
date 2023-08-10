package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
	
	@Query("SELECT new Document(id, name,size) FROM Document d ORDER BY d.uploadTime DESC")
	List<Document> findAll();
	
	
	

}
