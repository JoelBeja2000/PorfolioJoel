package com.porfoliojoel.com;

import com.porfoliojoel.com.config.AsyncSyncConfiguration;
import com.porfoliojoel.com.config.EmbeddedSQL;
import com.porfoliojoel.com.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { PorfolioJoelApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
