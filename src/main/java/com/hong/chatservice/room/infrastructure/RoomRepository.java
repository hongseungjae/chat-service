package com.hong.chatservice.room.infrastructure;


import com.hong.chatservice.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r " +
            "JOIN FETCH r.participants.member "+
            "where r.Id = :roomId")
    Optional<Room> findDetails(@Param("roomId") Long roomId);

    @Query("SELECT r FROM Room r " +
            "JOIN FETCH r.participants.member")
    List<Room> findRooms();
}
