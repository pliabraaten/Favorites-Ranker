package com.ranker.web.repository;

import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.models.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FavoritesListRepository favoritesListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private FavoritesList testList;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user = userRepository.save(user);

        testList = new FavoritesList();
        testList.setListName("Movies");
        testList.setUser(user);
        testList.setSortedCount(0);
        testList.setRanked(false);
        testList = favoritesListRepository.save(testList);
    }

    // ==================== findByPosition ====================

    @Test
    void shouldFindByPosition() {
        // Given
        Item item = createAndSaveItem("Movie", 1);

        // When
        Optional<Item> found = itemRepository.findByPosition(1);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Movie");
    }

    @Test
    void shouldReturnEmptyWhenPositionNotFound() {
        // When
        Optional<Item> found = itemRepository.findByPosition(99);

        // Then
        assertThat(found).isEmpty();
    }

    // ==================== findByFavoritesListIdAndPosition ====================

    @Test
    void shouldFindByFavoritesListIdAndPosition() {
        // Given
        createAndSaveItem("First", 1);
        createAndSaveItem("Second", 2);

        // When
        Optional<Item> found = itemRepository.findByFavoritesListIdAndPosition(
                testList.getId(), 2);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Second");
    }

    // ==================== countByFavoritesListId ====================

    @Test
    void shouldCountItemsInList() {
        // Given
        createAndSaveItem("First", 1);
        createAndSaveItem("Second", 2);
        createAndSaveItem("Third", 3);

        // When
        int count = itemRepository.countByFavoritesListId(testList.getId());

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    void shouldReturnZeroCountForEmptyList() {
        // When
        int count = itemRepository.countByFavoritesListId(testList.getId());

        // Then
        assertThat(count).isEqualTo(0);
    }

    // ==================== findByFavoritesListIdOrderByPositionAsc
    // ====================

    @Test
    void shouldReturnItemsOrderedByPosition() {
        // Given — save in reverse order to verify sorting
        createAndSaveItem("Third", 3);
        createAndSaveItem("First", 1);
        createAndSaveItem("Second", 2);

        // When
        List<Item> items = itemRepository.findByFavoritesListIdOrderByPositionAsc(
                testList.getId());

        // Then — should be sorted by position
        assertThat(items).hasSize(3);
        assertThat(items.get(0).getName()).isEqualTo("First");
        assertThat(items.get(1).getName()).isEqualTo("Second");
        assertThat(items.get(2).getName()).isEqualTo("Third");
    }

    // ==================== decrementPositionsAfter ====================

    @Test
    void shouldDecrementPositionsAfterDeletedPosition() {
        // Given
        createAndSaveItem("First", 1);
        createAndSaveItem("Third", 3);
        createAndSaveItem("Fourth", 4);
        // Note: position 2 is the "deleted" position — it doesn't exist in the DB

        // When — simulate decrement after deleting item at position 2
        itemRepository.decrementPositionsAfter(testList.getId(), 2);
        itemRepository.flush(); // force DB write
        entityManager.clear(); // clear JPA first-level cache so we read fresh from DB

        // Then — positions > 2 (i.e., 3 and 4) should be decremented by 1
        List<Item> items = itemRepository.findByFavoritesListIdOrderByPositionAsc(
                testList.getId());

        assertThat(items).hasSize(3);
        assertThat(items.get(0).getPosition()).isEqualTo(1); // unchanged (position 1)
        assertThat(items.get(1).getPosition()).isEqualTo(2); // was 3, decremented
        assertThat(items.get(2).getPosition()).isEqualTo(3); // was 4, decremented
    }

    // ==================== Helper ====================

    private Item createAndSaveItem(String name, int position) {
        Item item = new Item();
        item.setName(name);
        item.setPosition(position);
        item.setFavoritesList(testList);
        return itemRepository.save(item);
    }
}
