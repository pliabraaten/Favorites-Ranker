package com.ranker.web.repository;

import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // Sets up in-memory database for testing
class FavoritesListRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FavoritesListRepository favoritesListRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        // Create a test user
        testUser = new UserEntity();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    void shouldSaveFavoritesList() {
        // Given
        FavoritesList list = new FavoritesList();
        list.setListName("My Movies");
        list.setUser(testUser);
        list.setSortedCount(0);
        list.setRanked(false);

        // When
        FavoritesList savedList = favoritesListRepository.save(list);

        // Then
        assertThat(savedList.getId()).isNotNull();
        assertThat(savedList.getListName()).isEqualTo("My Movies");
        assertThat(savedList.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldFindListsByUserId() {
        // Given
        FavoritesList list1 = new FavoritesList();
        list1.setListName("Movies");
        list1.setUser(testUser);
        list1.setSortedCount(0);
        list1.setRanked(false);

        FavoritesList list2 = new FavoritesList();
        list2.setListName("Books");
        list2.setUser(testUser);
        list2.setSortedCount(0);
        list2.setRanked(false);

        entityManager.persist(list1);
        entityManager.persist(list2);
        entityManager.flush();

        // When
        List<FavoritesList> foundLists = favoritesListRepository.findByUserId(testUser.getId());

        // Then
        assertThat(foundLists).hasSize(2);
        assertThat(foundLists).extracting(FavoritesList::getListName)
                .containsExactlyInAnyOrder("Movies", "Books");
    }

    @Test
    void shouldDeleteFavoritesList() {
        // Given
        FavoritesList list = new FavoritesList();
        list.setListName("Temp List");
        list.setUser(testUser);
        list.setSortedCount(0);
        list.setRanked(false);

        FavoritesList savedList = entityManager.persist(list);
        entityManager.flush();
        Long listId = savedList.getId();

        // When
        favoritesListRepository.deleteById(listId);

        // Then
        FavoritesList deletedList = entityManager.find(FavoritesList.class, listId);
        assertThat(deletedList).isNull();
    }
}
