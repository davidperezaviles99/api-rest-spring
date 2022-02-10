package com.example.apirestspring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.apirestspring.service.ClienteService;
import com.example.apirestspring.entity.Cliente;


@RestController
@RequestMapping("/api")
public class ClienteController {
	
	@Autowired
	private ClienteService servicio;
	
	@GetMapping("/clientes")
	public List<Cliente> cliente(){
		return servicio.findAll();
	}
	
	@GetMapping("/clientes/{id}")
	public Cliente clienteid(@PathVariable Long id) {
		return servicio.findbyId(id);
	}
	
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente saveCliente(@RequestBody Cliente cliente) {
		return servicio.save(cliente);
	}
	
	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente modifCliente(@RequestBody Cliente cliente, @PathVariable long id) {
		Cliente clienteUpdate = servicio.findbyId(id);
		
		clienteUpdate.setNombre(cliente.getNombre());
		clienteUpdate.setApellido(cliente.getApellido());
		clienteUpdate.setTelefono(cliente.getTelefono());
		clienteUpdate.setEmail(cliente.getEmail());
		clienteUpdate.setCreatedAt(cliente.getCreatedAt());
		
		return servicio.save(clienteUpdate);		
	}
	
//	@DeleteMapping("/clientes/{id}")
//	public void deleteCliente(@PathVariable Long id) {
//		servicio.delete(id);
//	}
	
	@DeleteMapping("/clientes/{id}")
	public Cliente deleteshowCliente(@PathVariable Long id) {
		Cliente clienteborrado = servicio.findbyId(id);
		servicio.delete(id);
		return clienteborrado;
	}
	

}
