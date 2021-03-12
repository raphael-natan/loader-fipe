package br.com.dfsys.loaderfipe.domain.repository;

import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    Optional<Modelo> findByNomeAndIdMarca(String nome, Long idMarca);
    Boolean existsByNomeAndIdMarca(String modelo, Long idMarca);
}