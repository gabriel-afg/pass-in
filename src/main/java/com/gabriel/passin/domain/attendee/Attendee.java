package com.gabriel.passin.domain.attendee;

import com.gabriel.passin.domain.event.Event;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneColumn;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "created_at")

    private LocalDateTime createdAt;
}
