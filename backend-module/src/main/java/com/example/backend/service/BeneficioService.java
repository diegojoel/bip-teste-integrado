package com.example.backend.service;

import com.example.backend.domain.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BeneficioService implements TransferPort {

	private final BeneficioRepository repository;

	public BeneficioService(BeneficioRepository repository) {
		this.repository = repository;
	}

	public List<Beneficio> listAll() {
		return repository.findAll();
	}

	public Beneficio getById(Long id) {
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Benefício não encontrado"));
	}

	public Beneficio create(Beneficio b) {
		return repository.save(b);
	}

	public Beneficio update(Long id, Beneficio update) {
		Beneficio found = getById(id);
		found.setNome(update.getNome());
		found.setDescricao(update.getDescricao());
		found.setValor(update.getValor());
		found.setAtivo(update.getAtivo());
		return repository.save(found);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	@Transactional
	public void transfer(Long fromId, Long toId, BigDecimal amount) {
		if (fromId == null || toId == null) throw new IllegalArgumentException("IDs são obrigatórios");
		if (fromId.equals(toId)) throw new IllegalArgumentException("Conta origem e destino devem ser diferentes");
		if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Valor deve ser positivo");

		Beneficio from = repository.findWithLockingById(fromId)
				.orElseThrow(() -> new IllegalArgumentException("Origem não encontrada"));
		Beneficio to = repository.findWithLockingById(toId)
				.orElseThrow(() -> new IllegalArgumentException("Destino não encontrado"));
		if (Boolean.FALSE.equals(from.getAtivo()) || Boolean.FALSE.equals(to.getAtivo()))
			throw new IllegalStateException("Benefício inativo");

		if (from.getValor().compareTo(amount) < 0)
			throw new IllegalStateException("Saldo insuficiente");

		from.setValor(from.getValor().subtract(amount));
		to.setValor(to.getValor().add(amount));
		repository.save(from);
		repository.save(to);
	}
}


