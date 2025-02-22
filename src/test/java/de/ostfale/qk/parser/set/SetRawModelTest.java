package de.ostfale.qk.parser.set;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test SetRawModel")
@Tag( "unittest")
@QuarkusTest
class SetRawModelTest {

    SetRawModel setRawModel;

    @BeforeEach
    void setUp() {
        setRawModel = new SetRawModel(SetNo.FIRST, 21, 17);
    }

    @Test
    @DisplayName("Test string representation of set")
    void testStringRepresentation() {
        // when
        var result = setRawModel.toString();

        // then
        assertEquals("(Satz 1) 21 : 17", result);
    }
}
