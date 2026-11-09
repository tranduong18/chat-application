package com.duong.chatapplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Role extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String name;

    private String description;
}
