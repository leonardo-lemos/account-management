package uol.compass.accountmanagement.model.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record CustomerResponseDTO(Long id,
                                  String name,
                                  String email,
                                  LocalDate birthDate,
                                  List<AccountResponseDTO> accounts) {

}
