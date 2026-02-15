package com.ranker.web.repository;

import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FavoritesListRepositoryTest {

    @Autowired
    private FavoritesListRepository favoritesListRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveFavoritesList() {
        // Given
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        UserEntity savedUser = userRepository.save(testUser);

        FavoritesList list = new FavoritesList();
        list.setListName("My Movies");
        list.setUser(savedUser);
        list.setSortedCount(0);
        list.setRanked(false);

        // When
        FavoritesList savedList = favoritesListRepository.save(list);

        // Then
        assertThat(savedList.getId()).isNotNull();
        assertThat(savedList.getListName()).isEqualTo("My Movies");
    }

    @Test
    void shouldFindListsByUserId() {
        // Given
        UserEntity user = createTestUser("user1", "user1@test.com");

        FavoritesList list1 = createTestList("Movies", user);
        FavoritesList list2 = createTestList("Books", user);

        favoritesListRepository.save(list1);
        favoritesListRepository.save(list2);

        // When
        List<FavoritesList> foundLists = favoritesListRepository.findByUserId(user.getId());

        // Then
        assertThat(foundLists).hasSize(2);
        assertThat(foundLists).extracting(FavoritesList::getListName)
                .containsExactlyInAnyOrder("Movies", "Books");
    }

    @Test
    void shouldDeleteFavoritesList() {
        // Given
        UserEntity user = createTestUser("user2", "user2@test.com");
        FavoritesList list = createTestList("Temp List", user);
        FavoritesList savedList = favoritesListRepository.save(list);
        Long listId = savedList.getId();

        // When
        favoritesListRepository.deleteById(listId);

        // Then
        Optional<FavoritesList> deletedList = favoritesListRepository.findById(listId);
        assertThat(deletedList).isEmpty();
    }

    @Test
    void shouldUpdateListName() {
        // Given
        UserEntity user = createTestUser("user3", "user3@test.com");
        FavoritesList list = createTestList("Old Name", user);
        FavoritesList savedList = favoritesListRepository.save(list);

        // When
        savedList.setListName("New Name");
        FavoritesList updatedList = favoritesListRepository.save(savedList);

        // Then
        assertThat(updatedList.getListName()).isEqualTo("New Name");
    }

    @Test
    void shouldFindListById() {
        // Given
        UserEntity user = createTestUser("user4", "user4@test.com");
        FavoritesList list = createTestList("Test List", user);
        FavoritesList savedList = favoritesListRepository.save(list);

        // When
        Optional<FavoritesList> foundList = favoritesListRepository.findById(savedList.getId());

        // Then
        assertThat(foundList).isPresent();
        assertThat(foundList.get().getListName()).isEqualTo("Test List");
        assertThat(foundList.get().getUser().getUsername()).isEqualTo("user4");
    }

    // Helper methods
    private UserEntity createTestUser(String username, String email) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password123");
        return userRepository.save(user);
    }

    private FavoritesList createTestList(String name, UserEntity user) {
        FavoritesList list = new FavoritesList();
        list.setListName(name);
        list.setUser(user);
        list.setSortedCount(0);
        list.setRanked(false);
        return list;
    }
}