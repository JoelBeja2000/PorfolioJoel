package com.porfoliojoel.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonaAllPropertiesEquals(Persona expected, Persona actual) {
        assertPersonaAutoGeneratedPropertiesEquals(expected, actual);
        assertPersonaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonaAllUpdatablePropertiesEquals(Persona expected, Persona actual) {
        assertPersonaUpdatableFieldsEquals(expected, actual);
        assertPersonaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonaAutoGeneratedPropertiesEquals(Persona expected, Persona actual) {
        assertThat(expected)
            .as("Verify Persona auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonaUpdatableFieldsEquals(Persona expected, Persona actual) {
        assertThat(expected)
            .as("Verify Persona relevant properties")
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getApellido()).as("check apellido").isEqualTo(actual.getApellido()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getTelefono()).as("check telefono").isEqualTo(actual.getTelefono()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonaUpdatableRelationshipsEquals(Persona expected, Persona actual) {}
}
