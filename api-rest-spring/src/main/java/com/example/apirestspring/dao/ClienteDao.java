package com.example.apirestspring.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.apirestspring.entity.Cliente;

@Repository
public interface ClienteDao extends CrudRepository<Cliente, Long> {

}
