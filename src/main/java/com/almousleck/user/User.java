package com.almousleck.user;

import com.almousleck.cart.Cart;
import com.almousleck.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_email_unique",
                        columnNames = "email"
                )
        }
)
public class User {
    @Id
    @SequenceGenerator(
            name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_id_seq"
    )
    private Long id;
    private String firstname;
    private String lastname;
    @Column(
            name = "email",
            nullable = false
    )
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String profile;
    private String password;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Cart cart;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Order> orders;
}
