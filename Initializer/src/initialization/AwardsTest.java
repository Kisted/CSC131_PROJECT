package initialization;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AwardsTest {

	@Test
	void testGetYear() {
		
		Awards a = new Awards(1950, "Foo", true, "Bar");
		assertEquals(1950, a.getYear());
		
	}

	@Test
	void testGetCategory() {

		Awards a = new Awards(1950, "Foo", true, "Bar");
		assertEquals("Foo", a.getCategory());
		
	}

	@Test
	void testGetWinner() {

		Awards a = new Awards(1950, "Foo", true, "Bar");
		Awards b = new Awards(1950, "Foo", false, "Bar");
		
		assertEquals(true, a.getWinner());
		assertEquals(false, b.getWinner());
		
		
	}

	@Test
	void testGetEntity() {

		Awards a = new Awards(1950, "Foo", true, "Bar");
		assertEquals("Bar", a.getEntity());
		
	}

	@Test
	void testSetYear() {
		
		Awards a = new Awards(1950, "Foo", true, "Bar");
		a.setYear(1970);
		assertEquals(1970,a.getYear());
	}
	@Test
	void testSetCategory() {
		
		Awards a = new Awards(1950, "Foo", true, "Bar");
		a.setCategory("Bat");
		assertEquals("Bat",a.getCategory());
		
	}

	@Test
	void testSetWinner() {

		Awards a = new Awards(1950, "Foo", true, "Bar");
		a.setWinner(false);
		assertEquals(false, a.getWinner());
		
	}

	@Test
	void testSetEntity() {

		Awards a = new Awards(1950, "Foo", true, "Bar");
		a.setEntity("Bat");
		assertEquals("Bat",a.getEntity());
		
	}

}
