package com.epam.esm.dto;

import com.epam.esm.model.Entity;

import java.util.List;

public class JsonResult<T extends Entity> {
    private boolean success;
    private String status;
    private String message;
    private List<T> result;

    private JsonResult() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
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

    public static class Builder<T extends Entity> {
        private JsonResult<T> jsonResult;

        public Builder() {
            jsonResult = new JsonResult<>();
        }

        public Builder<T> withSuccess(boolean success) {
            jsonResult.success = success;
            return this;
        }

        public Builder<T> withStatus(String status) {
            jsonResult.status = status;
            return this;
        }

        public Builder<T> withMessage(String message) {
            jsonResult.message = message;
            return this;
        }

        public Builder<T> withResult(List<T> result) {
            jsonResult.result = result;
            return this;
        }

        public JsonResult<T> build() {
            return jsonResult;
        }
    }
}