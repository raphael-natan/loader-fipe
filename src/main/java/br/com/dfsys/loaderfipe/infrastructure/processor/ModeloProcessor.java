package br.com.dfsys.loaderfipe.infrastructure.processor;

import br.com.dfsys.loaderfipe.domain.entity.fipe.Veiculo;
import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Modelo;
import br.com.dfsys.loaderfipe.domain.repository.MarcaRepository;
import br.com.dfsys.loaderfipe.domain.repository.ModeloRepository;
import br.com.dfsys.loaderfipe.domain.repository.TipoVeiculoRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ModeloProcessor implements ItemProcessor<Veiculo, Modelo> {

    @Autowired
    private TipoVeiculoRepository tipoVeiculoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ModeloRepository modeloRepository;

    private Set<String> modelos = new HashSet<>();

    @Override
    public Modelo process(Veiculo veiculo) {
        AtomicReference<Modelo> modelo = new AtomicReference<>();
        if(!modelos.contains(veiculo.getModelo())){
            tipoVeiculoRepository.findByNome(veiculo.getTipo())
                    .ifPresent(tipoVeiculo -> {
                        marcaRepository.findByNomeAndIdTipoVeiculo(veiculo.getMarca(), tipoVeiculo.getId())
                                .ifPresent(marca -> {
                                    if(!modeloRepository.existsByNomeAndIdMarca(veiculo.getModelo(), marca.getId())){
                                        modelos.add(veiculo.getModelo());
                                        modelo.set(Modelo.builder()
                                                        .idMarca(marca.getId())
                                                        .nome(veiculo.getModelo())
                                                        .build());
                                    }
                                });
                    });
        }
        return modelo.get();
    }
}