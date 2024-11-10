package com.nyasha.drone.backend.spares;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpareService {
    private final SpareRepository spareRepository;

    public List<Spare> findAll() {
        return spareRepository.findAll();
    }

    public Spare getSpare(int id) {
        return spareRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(String.format("Spare with id %d not found", id)));
    }

    public void create(@Valid SpareRequest request) {
        Spare spare = Spare.builder()
                .description(request.getDescription())
                .name(request.getName())
                .partNumber(request.getPartNumber())
                .quantity(request.getQuantity())
                .build();
        spareRepository.save(spare);
    }

    public SpareRequest getSpareRequest(int id) {
        Spare spare = getSpare(id);
        SpareRequest spareRequest = new SpareRequest();
        spareRequest.setDescription(spare.getDescription());
        spareRequest.setName(spare.getName());
        spareRequest.setPartNumber(spare.getPartNumber());
        spareRequest.setQuantity(spare.getQuantity());
        spareRequest.setId(spare.getId());
        return spareRequest;
    }

    public void update(@Valid SpareRequest request, int id) {
        Spare spare = getSpare(id);
        spare.setDescription(request.getDescription());
        spare.setName(request.getName());
        spare.setPartNumber(request.getPartNumber());
        spare.setQuantity(request.getQuantity());
        spareRepository.save(spare);
    }

    public void delete(int id) {
        spareRepository.deleteById(id);
    }
}
