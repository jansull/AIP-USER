package com.os.util.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@Getter
public class BaseEntity {

    @Column(nullable = false)
//    @CreationTimestamp
    private LocalDateTime createAt;                // 결제 생성시간

    @Column(nullable = true)
//    @UpdateTimestamp
    private LocalDateTime updateAt;
}
