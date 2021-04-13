package com.epam.esm.dto;

import com.epam.esm.model.Entity;

public class Dto {
    private String status;
    private String message;
    private Entity entity;

    public Dto() {
    }

    public Dto(String status, String message, Entity entity) {
        this.status = status;
        this.message = message;
        this.entity = entity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
