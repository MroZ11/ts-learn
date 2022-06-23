package com.example.txlearn.controller;

import com.example.txlearn.dto.PackParam;
import com.example.txlearn.service.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pack")
public class PackController {

    @Autowired
    private PackService packService;

    @PostMapping("addTx")
    public ResponseEntity<Object> addTx(@RequestBody PackParam param) {
        packService.createPackTx(param);
        return ResponseEntity.ok(null);
    }

    @PostMapping("addTx2")
    public ResponseEntity<Object> addTx2(@RequestBody PackParam param) {
        packService.createPackTx2(param);
        return ResponseEntity.ok(null);
    }

    @PostMapping("addTx3")
    public ResponseEntity<Object> addTx3(@RequestBody PackParam param) {
        packService.createPackTx3(param);
        return ResponseEntity.ok(null);
    }

}
