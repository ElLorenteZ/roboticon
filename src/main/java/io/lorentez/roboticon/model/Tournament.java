package io.lorentez.roboticon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.lorentez.roboticon.model.validators.DateEndNotBeforeDateStart;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Tournament")
@DateEndNotBeforeDateStart
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate dateStart;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate dateEnd;

    @Builder.Default
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<Competition> competitions = new HashSet<>();
}
