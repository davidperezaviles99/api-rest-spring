package com.example.apirestspring.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
//	@GetMapping("/clientes/{id}")
//	public Cliente clienteid(@PathVariable Long id) {
//		return servicio.findbyId(id);
//	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> clienteShow(@PathVariable Long id){
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = servicio.findbyId(id);
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al realizar la consulta");
			response.put("Error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mensaje", "El cliente id:".concat(id.toString().concat("No existe")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
		
	}
	
//	@PostMapping("/clientes")
//	@ResponseStatus(HttpStatus.CREATED)
//	public Cliente saveCliente(@RequestBody Cliente cliente) {
//		return servicio.save(cliente);
//	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?> save(@RequestBody Cliente cliente){
		Cliente clienteNew = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			clienteNew = servicio.save(cliente);
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al realizar la insercion");
			response.put("Error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "El cliente fue creado con exito.");
		response.put("cliente", clienteNew);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
//	@PutMapping("/clientes/{id}")
//	@ResponseStatus(HttpStatus.CREATED)
//	public Cliente modifCliente(@RequestBody Cliente cliente, @PathVariable long id) {
//		Cliente clienteUpdate = servicio.findbyId(id);
//		
//		clienteUpdate.setNombre(cliente.getNombre());
//		clienteUpdate.setApellido(cliente.getApellido());
//		clienteUpdate.setTelefono(cliente.getTelefono());
//		clienteUpdate.setEmail(cliente.getEmail());
//		clienteUpdate.setCreatedAt(cliente.getCreatedAt());
//		
//		return servicio.save(clienteUpdate);		
//	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> updateCliente(@RequestBody Cliente cliente, @PathVariable Long id){
		Cliente clienteActual = servicio.findbyId(id);
		Map<String,Object> response = new HashMap<>();
		
		try {
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setTelefono(cliente.getTelefono());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreatedAt(cliente.getCreatedAt());
			
			servicio.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al realizar la actualizacion");
			response.put("Error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "El cliente fue modificado con exito.");
		response.put("cliente", clienteActual);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	
//	@DeleteMapping("/clientes/{id}")
//	public void deleteCliente(@PathVariable Long id) {
//		servicio.delete(id);
//	}
	
//	@DeleteMapping("/clientes/{id}")
//	public Cliente deleteshowCliente(@PathVariable Long id) {
//		Cliente clienteborrado = servicio.findbyId(id);
//		servicio.delete(id);
//		return clienteborrado;
//	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> deleteshowCliente(@PathVariable Long id) {
		Cliente clienteborrado = servicio.findbyId(id);
		Map<String,Object> response = new HashMap<>();
		
		try {
			
			if(clienteborrado == null) {
				response.put("mensaje", "El cliente id:".concat(id.toString().concat("No existe")));
				return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
			}
			
			servicio.delete(id);
			
			String nombreFotoAnterior = clienteborrado.getImagen();
			if(nombreFotoAnterior !=null && nombreFotoAnterior.length()>0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoanterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoanterior.exists() && archivoFotoanterior.canRead()) {
					archivoFotoanterior.delete();
				}
			}
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al eliminar el cliente");
			response.put("Error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "El cliente fue eliminado con exito.");
		response.put("cliente", clienteborrado);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> uploadImagen(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		Map<String,Object> response = new HashMap<>();
		
		Cliente cliente = servicio.findbyId(id);
		
		if(!archivo.isEmpty()) {
			//String nombreArchivo = archivo.getOriginalFilename();
			String nombreArchivo = UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ", "");
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("Mensaje", "Error al subir la imagen  del cliente");
				response.put("Error", e.getMessage().concat("_ ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = cliente.getImagen();
			if(nombreFotoAnterior !=null && nombreFotoAnterior.length()>0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoanterior = rutaFotoAnterior.toFile();
				
				if(archivoFotoanterior.exists() && archivoFotoanterior.canRead()) {
					archivoFotoanterior.delete();
				}
			}
			
			cliente.setImagen(nombreArchivo);
			servicio.save(cliente);
			
			response.put("cliente", cliente);
			response.put("Mensaje", "La imagen fue subida."+nombreArchivo);			
		}else {
			response.put("Archivo", "archivo vacio");
		}
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);

		
	}
}
