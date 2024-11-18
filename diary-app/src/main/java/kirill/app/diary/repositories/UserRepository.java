package kirill.app.diary.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kirill.app.diary.models.UserDiary;

public interface UserRepository extends JpaRepository<UserDiary, Long>{

	Optional<UserDiary> findByUsername(String username);
}
