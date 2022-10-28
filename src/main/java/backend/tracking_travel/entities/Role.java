package backend.tracking_travel.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
@Entity
@Table(name = "t_role")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Role {
        @Id
        private Long id;

        private String name;

        @Transient
        @ManyToMany(mappedBy = "roles")
        private Set<User> users;
}
