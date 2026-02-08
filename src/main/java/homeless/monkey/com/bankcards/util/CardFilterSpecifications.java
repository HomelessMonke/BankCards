package homeless.monkey.com.bankcards.util;

import homeless.monkey.com.bankcards.dto.card.CardFilter;
import homeless.monkey.com.bankcards.entity.CardEntity;
import homeless.monkey.com.bankcards.entity.CardStatus;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class CardSearchSpecifications {

    private CardSearchSpecifications() {}

    public static Specification<CardEntity> byOwnerId(Long ownerId){
        return (root, cq, cb) ->
                ownerId != null ? cb.equal(root.get("owner").get("id"), ownerId)
                                : cb.conjunction();
    }

    public static Specification<CardEntity> fromFilter(CardFilter filter){
        if(filter == null)
            return (root, cq, cb) -> cb.conjunction();

        Specification<CardEntity> spec = (root, cq, cb) -> cb.conjunction();

        if(StringUtils.isNotBlank(filter.getCardNumber())){
            spec = spec.and(byCardNumber(filter.getCardNumber()));
        }

        if(StringUtils.isNotBlank(filter.getStatus())){
            try{
                CardStatus status = CardStatus.valueOf(filter.getStatus().toUpperCase());
                spec = spec.and(byStatus(status));
            }
            catch (IllegalArgumentException _){}
        }

        if(StringUtils.isNotBlank(filter.getOwnerFirstName())){
            spec = spec.and(byOwnerFirstName(filter.getOwnerFirstName()));
        }

        if(StringUtils.isNotBlank(filter.getOwnerLastName())){
            spec = spec.and(byOwnerLastName(filter.getOwnerLastName()));
        }

        if (filter.getMinBalance() != null) {
            spec = spec.and(minBalance(filter.getMinBalance()));
        }

        if (filter.getMaxBalance() != null) {
            spec = spec.and(maxBalance(filter.getMaxBalance()));
        }

        if (filter.getExpirationAfter() != null) {
            spec = spec.and(expirationAfter(filter.getExpirationAfter()));
        }

        if (filter.getExpirationBefore() != null) {
            spec = spec.and(expirationBefore(filter.getExpirationBefore()));
        }

        return spec;
    }

    private static Specification<CardEntity> byCardNumber(String term) {
        return (root, cq, cb) -> {
            String pattern = "%" + term.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("cardNumber")), pattern);
        };
    }

    private static Specification<CardEntity> byStatus(CardStatus status) {
        return (root, cq, cb) -> cb.equal(root.get("cardStatus"), status);
    }

    private static Specification<CardEntity> byOwnerFirstName(String term) {
        return (root, cq, cb) -> {
            String pattern = "%" + term.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("owner").get("firstName")), pattern);
        };
    }

    private static Specification<CardEntity> byOwnerLastName(String term) {
        return (root, cq, cb) -> {
            String pattern = "%" + term.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("owner").get("lastName")), pattern);
        };
    }

    private static Specification<CardEntity> minBalance(BigDecimal min) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("balance"), min);
    }

    private static Specification<CardEntity> maxBalance(BigDecimal max) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get("balance"), max);
    }

    private static Specification<CardEntity> expirationAfter(LocalDate date) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("expirationDate"), date);
    }

    private static Specification<CardEntity> expirationBefore(LocalDate date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("expirationDate"), date);
    }
}
