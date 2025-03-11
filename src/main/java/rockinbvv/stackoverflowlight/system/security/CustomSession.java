package rockinbvv.stackoverflowlight.system.security;

import org.springframework.session.Session;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomSession implements Session, Serializable {

    private String id;
    private Instant creationTime;
    private Instant lastAccessedTime;
    private Duration maxInactiveInterval;
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    public CustomSession() {
        this.id = java.util.UUID.randomUUID().toString();
        this.creationTime = Instant.now();
        this.lastAccessedTime = Instant.now();
        this.maxInactiveInterval = Duration.ofMinutes(30);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String changeSessionId() {
        this.id = java.util.UUID.randomUUID().toString();
        return this.id;
    }
    @Override
    public Instant getCreationTime() {
        return creationTime;
    }

    @Override
    public void setLastAccessedTime(Instant lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public Instant getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void setMaxInactiveInterval(Duration interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public Duration getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public boolean isExpired() {
        return lastAccessedTime.plus(maxInactiveInterval).isBefore(Instant.now());
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    @Override
    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    @Override
    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }
}
