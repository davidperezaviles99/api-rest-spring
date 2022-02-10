package com.example.apirestspring.service;

import java.util.List;

import com.example.apirestspring.entity.Cliente;

public interface ClienteService {
	
	public List<Cliente> findAll();
	
	public Cliente findbyId(Long Id);
	
	public Cliente save(Cliente cliente);
	
	public void delete(Long id);

}
