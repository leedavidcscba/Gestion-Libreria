/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author leedavidcuellar
 */
@Controller
public class PortalContolador {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
}
