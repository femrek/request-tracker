package me.femrek.viewcounter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DatabaseTriggerInitializer {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseTriggerInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createTriggerFunction() {
        log.info("Creating trigger function and trigger for incrementing counter on table_a");

        String createFunction = """
                CREATE OR REPLACE FUNCTION increment_counter()
                RETURNS trigger AS $$
                BEGIN
                  UPDATE subscription
                  SET counter = counter + 1,
                      last_request_at = NOW()
                  WHERE id = NEW.subscription;
                  RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """;

        String createTrigger = """
                DROP TRIGGER IF EXISTS trg_increment_counter ON request;
                CREATE TRIGGER trg_increment_counter
                AFTER INSERT ON request
                FOR EACH ROW
                EXECUTE FUNCTION increment_counter();
                """;

        jdbcTemplate.execute(createFunction);
        jdbcTemplate.execute(createTrigger);
    }
}

