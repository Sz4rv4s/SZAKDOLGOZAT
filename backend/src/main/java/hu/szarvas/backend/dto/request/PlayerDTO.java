package hu.szarvas.backend.dto.request;

import lombok.Data;

@Data
public class PlayerDTO {
    private Integer id;
    private String name;
    private String position;
    private String dateOfBirth;
    private String nationality;
}
