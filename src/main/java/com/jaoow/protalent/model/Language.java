package com.jaoow.protalent.model;

import com.jaoow.protalent.enums.LanguageProficiencyLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String languageName;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL) // Usar ORDINAL para podemos comparar os valores de forma mais eficiente
    private LanguageProficiencyLevel proficiencyLevel;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}