package com.service.shop.controller.resp;

public class ShopResp {
    private String name;
    private AddressResp address;

    public ShopResp(String name, AddressResp address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressResp getAddress() {
        return address;
    }

    public void setAddress(AddressResp address) {
        this.address = address;
    }
}
