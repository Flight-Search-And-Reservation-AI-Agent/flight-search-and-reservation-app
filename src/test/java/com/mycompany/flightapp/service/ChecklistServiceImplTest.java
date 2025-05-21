package com.mycompany.flightapp.service;

import com.mycompany.flightapp.dto.CheckListDTO;
import com.mycompany.flightapp.model.ChecklistItem;
import com.mycompany.flightapp.model.TripGroup;
import com.mycompany.flightapp.repository.ChecklistItemRepository;
import com.mycompany.flightapp.repository.TripGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChecklistServiceImplTest {

    @InjectMocks
    private ChecklistServiceImpl checklistService;

    @Mock
    private ChecklistItemRepository checklistItemRepository;

    @Mock
    private TripGroupRepository tripGroupRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItem_Success() {
        String tripGroupId = "group123";
        TripGroup tripGroup = TripGroup.builder().tripGroupId(tripGroupId).build();

        when(tripGroupRepository.findById(tripGroupId)).thenReturn(Optional.of(tripGroup));

        ChecklistItem item = ChecklistItem.builder()
                .task("Buy Tickets")
                .assignedTo("user1")
                .completed(false)
                .tripGroup(tripGroup)
                .build();

        when(checklistItemRepository.save(any(ChecklistItem.class))).thenReturn(item);

        ChecklistItem result = checklistService.addItem(tripGroupId, "Buy Tickets", "user1");

        assertNotNull(result);
        assertEquals("Buy Tickets", result.getTask());
        assertFalse(result.isCompleted());
        verify(checklistItemRepository, times(1)).save(any(ChecklistItem.class));
    }

    @Test
    void testAddItem_TripGroupNotFound() {
        when(tripGroupRepository.findById("invalid")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> checklistService.addItem("invalid", "Task", "user"));

        assertEquals("Trip group not found", exception.getMessage());
    }

    @Test
    void testGetChecklistForTripGroup() {
        String tripGroupId = "group123";
        List<ChecklistItem> checklist = List.of(
                ChecklistItem.builder().task("Task 1").build(),
                ChecklistItem.builder().task("Task 2").build()
        );

        when(checklistItemRepository.findByTripGroup_TripGroupId(tripGroupId)).thenReturn(checklist);

        List<ChecklistItem> result = checklistService.getChecklistForTripGroup(tripGroupId);

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTask());
    }

    @Test
    void testToggleComplete() {
        ChecklistItem item = ChecklistItem.builder()
                .itemId("item1")
                .task("Test Task")
                .completed(false)
                .build();

        when(checklistItemRepository.findById("item1")).thenReturn(Optional.of(item));
        when(checklistItemRepository.save(any(ChecklistItem.class))).thenReturn(item);

        ChecklistItem result = checklistService.toggleComplete("item1");

        assertTrue(result.isCompleted());
        verify(checklistItemRepository).save(item);
    }

    @Test
    void testToggleComplete_ItemNotFound() {
        when(checklistItemRepository.findById("missing")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> checklistService.toggleComplete("missing"));
        assertEquals("Item not found", ex.getMessage());
    }

    @Test
    void testUpdateChecklist() {
        String checklistId = "check1";
        ChecklistItem existing = ChecklistItem.builder()
                .itemId(checklistId)
                .task("Old Task")
                .assignedTo("userA")
                .build();

        CheckListDTO dto = new CheckListDTO("New Task", "userB");

        when(checklistItemRepository.findById(checklistId)).thenReturn(Optional.of(existing));
        when(checklistItemRepository.save(any(ChecklistItem.class))).thenReturn(existing);

        ChecklistItem result = checklistService.updateChecklist(checklistId, dto);

        assertEquals("New Task", result.getTask());
        assertEquals("userB", result.getAssignedTo());
    }

    @Test
    void testUpdateChecklist_NotFound() {
        when(checklistItemRepository.findById("invalid")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> checklistService.updateChecklist("invalid", new CheckListDTO("T", "U")));

        assertEquals("Checklist item not found", ex.getMessage());
    }

    @Test
    void testDeleteChecklist() {
        doNothing().when(checklistItemRepository).deleteById("delete1");

        checklistService.deleteChecklist("delete1");

        verify(checklistItemRepository, times(1)).deleteById("delete1");
    }
}
