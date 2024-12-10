package com.os.user.entity;

import com.os.customer.entity.Customer;
import com.os.memo.entity.Memo;
import com.os.save.entity.SavePayment;
import com.os.util.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false ,unique = true)
    private String accountId;

    @Column(nullable = false)
    private String accountPw;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column
    private String status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade =CascadeType.REMOVE)
    private List<Memo> memos;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade =CascadeType.REMOVE)
    private List<Customer> customer;

    @OneToOne(mappedBy= "user" , cascade = CascadeType.ALL)
    private SavePayment savePayment;

    @Setter
    @Column
    private LocalDateTime lastLogin;

    @Builder
    public User(Long id, String username, String accountId, String accountPw, UserRole role, String status) {
        this.id = id;
        this.username = username;
        this.accountId = accountId;
        this.accountPw = accountPw;
        this.role = role;
        this.status = status;
    }
}
