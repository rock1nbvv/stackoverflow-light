package rockinbvv.stackoverflowlight.system.security;

import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySessionRepository implements SessionRepository<CustomSession> {

    private final Map<String, CustomSession> sessions = new ConcurrentHashMap<>();
    private final Duration sessionTimeout = Duration.ofMinutes(30);

    @Override
    public CustomSession createSession() {
        CustomSession session = new CustomSession();
        session.setLastAccessedTime(Instant.now());
        session.setMaxInactiveInterval(sessionTimeout);
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public void save(CustomSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public CustomSession findById(String id) {
        CustomSession session = sessions.get(id);
        if (session != null && !isExpired(session)) {
            session.setLastAccessedTime(Instant.now());
            return session;
        }
        sessions.remove(id);
        return null;
    }

    @Override
    public void deleteById(String id) {
        sessions.remove(id);
    }

    private boolean isExpired(CustomSession session) {
        return session.getLastAccessedTime().plus(session.getMaxInactiveInterval()).isBefore(Instant.now());
    }
}

