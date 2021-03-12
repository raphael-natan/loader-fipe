package br.com.dfsys.loaderfipe.domain.repository;

import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByNomeAndIdTipoVeiculo(String nome, Long tipoVeiculo);
    Boolean existsByNomeAndIdTipoVeiculo(String marca, Long tipoVeiculo);
}
