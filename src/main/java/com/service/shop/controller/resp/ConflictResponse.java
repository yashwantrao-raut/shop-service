package com.service.shop.controller.resp;

import java.util.List;

public class ConflictResponse<E,O> {
    List<E> messages;
    List<O> responseObjects;
    public ConflictResponse(List<E> messages, List<O> responseObjects) {
        this.messages = messages;
        this.responseObjects = responseObjects;
    }
    public List<E> getMessages() {
        return messages;
    }
    public List<O> getResponseObjects() {
        return responseObjects;
    }
}
