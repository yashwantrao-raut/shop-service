package com.service.shop.controller.req;

public class ShopReq {
    private String name;
    private AddressReq address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressReq getAddress() {
        return address;
    }

    public void setAddress(AddressReq address) {
        this.address = address;
    }
}
