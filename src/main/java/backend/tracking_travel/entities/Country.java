package backend.tracking_travel.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "COUNTRIES")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameOfCountry;
}
