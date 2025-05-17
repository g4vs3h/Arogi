package com.arogi.arogiservice;

import com.arogi.arogiservice.models.Patient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArogiServiceApplication {

    public static void main(String[] args) {
        Patient patient = new Patient();
        patient.setAddress("test");
        System.out.printf("Patient address: %s\n", patient.getAddress());
        SpringApplication.run(ArogiServiceApplication.class, args);
    }

}
