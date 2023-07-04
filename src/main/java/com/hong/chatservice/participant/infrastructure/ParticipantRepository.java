package com.hong.chatservice.participant.infrastructure;


import com.hong.chatservice.participant.domain.Participant;
import com.hong.chatservice.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findParticipantByRoomId(Long roomId);
}
