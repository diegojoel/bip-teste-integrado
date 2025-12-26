package com.example.backend;

import com.example.backend.domain.Beneficio;
import com.example.backend.service.BeneficioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

	private final BeneficioService service;

	public BeneficioController(BeneficioService service) {
		this.service = service;
	}

	@GetMapping
	public List<Beneficio> list() {
		return service.listAll();
	}

	@GetMapping("/{id}")
	public Beneficio get(@PathVariable Long id) {
		return service.getById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Beneficio create(@RequestBody Beneficio b) {
		return service.create(b);
	}

	@PutMapping("/{id}")
	public Beneficio update(@PathVariable Long id, @RequestBody Beneficio b) {
		return service.update(id, b);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

	public record TransferRequest(Long fromId, Long toId, BigDecimal amount) {}

	@PostMapping("/transfer")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void transfer(@RequestBody TransferRequest req) {
		service.transfer(req.fromId(), req.toId(), req.amount());
	}
}
