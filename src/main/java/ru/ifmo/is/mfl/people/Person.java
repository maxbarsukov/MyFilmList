package ru.ifmo.is.mfl.people;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.ifmo.is.mfl.common.framework.CrudEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "people")
public class Person extends CrudEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "people_id_seq")
  @SequenceGenerator(name = "people_id_seq", sequenceName = "people_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private int id;

  @NotNull
  @NotBlank
  @Column(name = "name", nullable = false, unique = true)
  private String name;
}
