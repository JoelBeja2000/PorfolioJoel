package com.porfoliojoel.com.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Persona getPersonaSample1() {
        return new Persona().id(1L).nombre("nombre1").apellido("apellido1").email("email1").telefono("telefono1");
    }

    public static Persona getPersonaSample2() {
        return new Persona().id(2L).nombre("nombre2").apellido("apellido2").email("email2").telefono("telefono2");
    }

    public static Persona getPersonaRandomSampleGenerator() {
        return new Persona()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
