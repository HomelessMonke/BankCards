package homeless.monkey.com.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_cards")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus cardStatus = CardStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    public Long getOwnerId() {return owner.getId();}

    public boolean belongsToUser(Long id) {return id.equals(owner.getId());}
    public boolean isActiveCard() {return cardStatus == CardStatus.ACTIVE;}
    public boolean enoughBalance(BigDecimal compareBalance) {return balance.compareTo(compareBalance) >= 0;}

    public void subtractBalance(BigDecimal subBalance){

        if(!enoughBalance(subBalance))
            throw new IllegalArgumentException("На карте не достаточно средств");

        balance = balance.subtract(subBalance);
    }

    public void addBalance(BigDecimal addBalance){
        balance = balance.add(addBalance);
    }
}
