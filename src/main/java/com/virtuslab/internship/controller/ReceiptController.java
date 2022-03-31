package com.virtuslab.internship.controller;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptGenerator receiptGenerator;

    public ReceiptController(ReceiptGenerator receiptGenerator) {
        this.receiptGenerator = receiptGenerator;
    }

    @PostMapping
    public Receipt generateReceipt(@RequestBody Basket basket) {
        return receiptGenerator.generate(basket);
    }
}
