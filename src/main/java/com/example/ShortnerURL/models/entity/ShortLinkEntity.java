package com.example.ShortnerURL.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Index;


@Entity(name = "links")
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