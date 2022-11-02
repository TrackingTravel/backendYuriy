package backend.tracking_travel.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class SubRoute {
    @Size(min = 2, message = "Не меньше 2 знаков")
    private String title;
    private String description;
}
