package com.porfoliojoel.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PresentacionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPresentacionAllPropertiesEquals(Presentacion expected, Presentacion actual) {
        assertPresentacionAutoGeneratedPropertiesEquals(expected, actual);
        assertPresentacionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPresentacionAllUpdatablePropertiesEquals(Presentacion expected, Presentacion actual) {
        assertPresentacionUpdatableFieldsEquals(expected, actual);
        assertPresentacionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPresentacionAutoGeneratedPropertiesEquals(Presentacion expected, Presentacion actual) {
        assertThat(expected)
            .as("Verify Presentacion auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPresentacionUpdatableFieldsEquals(Presentacion expected, Presentacion actual) {
        assertThat(expected)
            .as("Verify Presentacion relevant properties")
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPresentacionUpdatableRelationshipsEquals(Presentacion expected, Presentacion actual) {}
}
