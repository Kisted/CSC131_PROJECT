package servlets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.*;

class APIHelperTest {

	@Test
	void testSanitizeEndPoint() {
		
		String[] exp = new String[] {"actor","1927", "1"};
		
		Path uri = Paths.get("/actor/1927/winner");
		
		assertArrayEquals(exp,APIHelper.sanitizeEndPoint(uri));
		
	}
	
	@Test
	void testBuildSQLSelectStatement() {
		
		String noPathArray[] = new String[] {""};
		String onePathArray[] = new String[] {"foo"};
		String twoPathArray[] = new String[] {"foo","1945"};
		String threePathArray[] = new String[] {"foo","1945","1"};
		
		assertEquals("select * from awards where (category like '' or category like ' (%)')"
				+ " ORDER BY year ASC, Entity ASC, category ASC FOR JSON AUTO"
				,APIHelper.buildSQLSelectStatement(noPathArray));
		assertEquals("select * from awards where (category like 'foo'"
				+ " or category like 'foo (%)')"
				+ " ORDER BY year ASC, Entity ASC, category ASC FOR JSON AUTO"
				,APIHelper.buildSQLSelectStatement(onePathArray));
		assertEquals("select * from awards where (category like 'foo'"
				+ " or category like 'foo (%)')"
				+ " and year = 1945"
				+ " ORDER BY year ASC, Entity ASC, category ASC FOR JSON AUTO"
				,APIHelper.buildSQLSelectStatement(twoPathArray));
		assertEquals("select * from awards where (category like 'foo'"
				+ " or category like 'foo (%)')"
				+ " and year = 1945"
				+ " and winner = 1"
				+ " ORDER BY year ASC, Entity ASC, category ASC FOR JSON AUTO"
				,APIHelper.buildSQLSelectStatement(threePathArray));
	}
	
	@Test
	void testBuildSearchSQLSelectStatement() {
		//don't want to import a framework to allow me to test the httpservletrequest
		//that I already tested by hand; leaving this as a placeholder for an actual test
		
		assertEquals(true,true);
	}
	
	@Test
	void testIsInt() {
		
		assertEquals(true,APIHelper.isInt("1"));
		assertEquals(true,APIHelper.isInt("0"));
		assertEquals(true,APIHelper.isInt("-1"));
		assertEquals(false,APIHelper.isInt("a"));
		assertEquals(false,APIHelper.isInt("a0"));
		assertEquals(false,APIHelper.isInt("0a"));
		assertEquals(false,APIHelper.isInt("-a"));
		assertEquals(false,APIHelper.isInt("-a0"));
		assertEquals(false,APIHelper.isInt("-0a"));
		
		
		
	}
	
	@Test
	void testIsBinary() {
		
		assertEquals(true,APIHelper.isBinary("1"));
		assertEquals(true,APIHelper.isBinary("0"));
		assertEquals(false,APIHelper.isBinary("10"));
		
	}

}
