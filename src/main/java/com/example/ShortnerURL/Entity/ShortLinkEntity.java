package com.example.ShortnerURL.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Index;
import org.springframework.validation.annotation.Validated;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String longLink;
    @Column(unique=true)
    @Index(name="shortLinkCodeIndex")
    String shortLinkCode;
}