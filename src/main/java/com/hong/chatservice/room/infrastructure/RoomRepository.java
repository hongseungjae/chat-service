package com.hong.chatservice.room.infrastructure;


import com.hong.chatservice.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
