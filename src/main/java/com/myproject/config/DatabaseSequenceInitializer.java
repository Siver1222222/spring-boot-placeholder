package com.socialhub.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
public class DatabaseSequenceInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final EntityManager entityManager;
    private final PlatformTransactionManager transactionManager;
    private boolean initialized = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!initialized) {
            //initializeSequences();
            initialized = true;
        }
    }

    private void initializeSequences() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(status -> {
            // Update course sequence
            entityManager.createNativeQuery(
                            "SELECT setval('course_seq', COALESCE((SELECT MAX(id) FROM course), 0), false)",
                            Long.class)
                    .getSingleResult();

            // Update professor sequence
            entityManager.createNativeQuery(
                            "SELECT setval('professor_seq', COALESCE((SELECT MAX(professor_id) FROM professor), 0), false)",
                            Long.class)
                    .getSingleResult();

            // Update student sequence
            entityManager.createNativeQuery(
                            "SELECT setval('student_seq', COALESCE((SELECT MAX(id) FROM student), 0), false)",
                            Long.class)
                    .getSingleResult();

            // Update student profile sequence
            entityManager.createNativeQuery(
                            "SELECT setval('student_profile_seq', COALESCE((SELECT MAX(id) FROM student_profile), 0), false)",
                            Long.class)
                    .getSingleResult();

            return null;
        });
    }
}