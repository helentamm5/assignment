package com.example.assignment.adapter.database;

import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sanctioned_person")
public class SanctionedPersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="normalized_name")
    private String normalizedName;
    @Column(name="is_deleted")
    private boolean isDeleted;

    public SanctionedPerson toDomain() {
        return SanctionedPerson.builder()
            .id(getId())
            .name(getName())
            .normalizedName(getNormalizedName())
            .isDeleted(isDeleted())
            .build();
    }
}
