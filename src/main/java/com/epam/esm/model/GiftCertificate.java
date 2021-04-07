package com.epam.esm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class GiftCertificate extends Certificate {
    private BigDecimal price;
    private int duration;
    private LocalDateTime lastUpdateDate;
    private boolean isActive;

    public GiftCertificate() {
    }

    public GiftCertificate(String name, String description, BigDecimal price, int duration) {
        super(name, description);
        this.price = price;
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return duration == that.duration &&
                price.compareTo(that.price) == 0 &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), price, duration, lastUpdateDate, isActive);
    }
}
