import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    public static final Epic EPIC_1 = new Epic("epic1", "epic1 description");
    public static final Epic EPIC_2 = new Epic("epic1", "epic1 description");
    public static final Epic EPIC_3 = new Epic("epic3", "epic3 description");


    @BeforeAll
    public static void beforeAll() {
        EPIC_1.setId(1);
        EPIC_2.setId(1);
        EPIC_3.setId(2);
    }

    @Test
    public void equals_twoEpicsWithSameId_true() {
        assertEquals(EPIC_1, EPIC_2);
    }

    @Test
    public void equals_twoEpicsWithDifferentId_false() {
        assertNotEquals(EPIC_1, EPIC_3);
    }
}