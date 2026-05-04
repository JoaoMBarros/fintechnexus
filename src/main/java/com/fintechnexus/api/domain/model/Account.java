package com.fintechnexus.api.domain.model;

import com.fintechnexus.api.domain.exception.InsufficientFundsException;
import com.fintechnexus.api.domain.exception.InvalidAccountStatusException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    /*
    * The Style.TIME from hibernate generates an uuid based on timestamp (same as the uuidv7);
    * */
    @Id
    @Column(updatable = false, nullable = false)
    @org.hibernate.annotations.UuidGenerator(style = org.hibernate.annotations.UuidGenerator.Style.TIME)
    private UUID id;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "balance_cents", nullable = false)
    @Builder.Default
    private Long balanceCents = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void debit(Long amountCents){
        validatePositive(amountCents);
        validateAccount();
        validateCents(amountCents);
        this.balanceCents -= amountCents;
    }

    public void credit(Long amountCents){
        validatePositive(amountCents);
        validateAccount();
        this.balanceCents += amountCents;
    }

    public void freeze(){
        validateClosedAccount();
        this.status = AccountStatus.FROZEN;
    }

    public void unfreeze(){
        validateClosedAccount();
        this.status = AccountStatus.ACTIVE;
    }

    public void close(){
        this.status = AccountStatus.CLOSED;
    }

    public BigDecimal getBalanceAsDecimal() {
        return BigDecimal.valueOf(this.balanceCents, 2);
    }

    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }

    public boolean isFrozen() {
        return this.status == AccountStatus.FROZEN;
    }

    public boolean isDeleted() {
        return this.status == AccountStatus.CLOSED;
    }

    private void validatePositive(Long amountCents){
        if (amountCents == null || amountCents <= 0){
            throw new IllegalArgumentException(
                    "Amount must be positive, got: " + amountCents
            );
        }
    }

    private void validateAccount() {
        if (this.status != AccountStatus.ACTIVE){
            throw new InvalidAccountStatusException(
                    "Account is not active"
            );
        }
    }

    private void validateClosedAccount(){
        if(this.status == AccountStatus.CLOSED){
            throw new InvalidAccountStatusException(
                    "Account is closed"
            );
        }
    }

    private void validateCents(Long amountCents){
        if (this.balanceCents < amountCents){
            throw new InsufficientFundsException(
                    "Not enough funds"
            );
        }
    }

    public enum AccountType {
        CHECKING,
        SAVINGS
    }

    public enum AccountStatus {
        ACTIVE,
        FROZEN,
        CLOSED
    }
}
