package com.donworry.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    private String userId;
    private String name;
    private Long avgTransferAmount;

    public User(String userId, String name, Long avgTransferAmount) {
        this.userId = userId;
        this.name = name;
        this.avgTransferAmount = avgTransferAmount;
    }
}
