package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService implements BeneficioEjbServiceRemote {

	@PersistenceContext
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void transfer(Long fromId, Long toId, BigDecimal amount) {
		if (fromId == null || toId == null) {
			throw new BusinessException("IDs origem e destino são obrigatórios");
		}
		if (fromId.equals(toId)) {
			throw new BusinessException("Transferência entre a mesma conta não é permitida");
		}
		if (amount == null || amount.signum() <= 0) {
			throw new BusinessException("Valor da transferência deve ser positivo");
		}

		Beneficio from = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
		Beneficio to = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);
		if (from == null || to == null) {
			throw new BusinessException("Benefício(s) não encontrado(s)");
		}
		if (Boolean.FALSE.equals(from.getAtivo()) || Boolean.FALSE.equals(to.getAtivo())) {
			throw new BusinessException("Benefício inativo");
		}
		if (from.getValor().compareTo(amount) < 0) {
			throw new BusinessException("Saldo insuficiente para transferência");
		}

		from.setValor(from.getValor().subtract(amount));
		to.setValor(to.getValor().add(amount));
		// merges are optional since entities are managed, but keep explicit for clarity
		em.merge(from);
		em.merge(to);
	}
}
