package clinic.programming.training;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;


// Di default JUnit crea un'istanza di MathUtilsTest per ogni metodo da testare (TestInstance.Lifecycle.PER_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("When running MathUtils")
public class MathUtilsTest {
	
	MathUtils mathUtils;
	TestInfo testInfo;
	TestReporter testReporter;
	
	
	@BeforeAll
	// Il metodo annotato come BeforeAll deve essere static perchè JUnit non ha la classe su cui eseguirlo. 
	// Se però definisco il TestInstance come classe (TestInstance.Lifecycle.PER_CLASS)
	// allora può non essere static
	public void beforeAllInit() {
		System.out.println("This need to run before all");
	}
	
	
	@AfterAll
	public static void afterAllCleanup() {		// Deve essere static perchè JUnit non ha la classe su cui eseguire il metodo
		System.out.println("Final cleaning test ...");
	}
	
	
	@BeforeEach
	// TestInfo mi fornisce informazioni relative al test che si sta eseguendo 
	public void init(TestInfo testInfo, TestReporter testReporter) {
		this.testInfo = testInfo;
		this.testReporter = testReporter;
		
		mathUtils = new MathUtils();
		
		testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags());		// Aggiungo il messaggio al testReport (di default è la console ed aggiunge un timestamp)
	}
	
	
	@Nested
	@DisplayName("add method")
	@Tag("Math")
	// Per raggruppare più test che possono essere dello stesso tipo. Se fallisce un test fallisce anche il 
	// raggruppamento AddTest
	class AddTest {
		
		@Test
		@DisplayName("when adding two positive numbers")
		public void testAddPositive() {
			int expected = 2;
			int actual = mathUtils.add(1, 1);
			
			assertEquals(expected, actual, "should return the right sum");
		}
		
		
		@Test
		@DisplayName("when adding two negative numbers")
		public void testAddNegative() {
			int expected = -2;
			int actual = mathUtils.add(-1, -1);
			
//			assertEquals(expected, actual, "should return the right sum " + expected + " but returns " + actual);			// La stringa viene composta indipendentemente se il test è passo o fail 
																															// siccome vinee fatta prima del test (è assertEquals che controlla il test)
																															// E' un'operazione dispendiosa. La soluzione è una lambda function
			
			assertEquals(expected, actual, () -> "should return the right sum " + expected + " but returns " + actual);		// Solo se fallisce esegue la lambda function
		}
	}
	
	
	@AfterEach
	public void cleanup() {
		System.out.println("Cleaning up ...");
	}

	
	@Test
	@Tag("Math")
	public void testDivide() {
		boolean isServerUP = false;
		
		assumeTrue(isServerUP);		// Assumpion servono per abilitare/diabilitare in modo programmatico il test anziché con le annotazioni. In questo caso se è false
									// il test non viene avviato (quindi non è né fail né pass, è come se fosse disabilitato)
		
		assertThrows(ArithmeticException.class, () -> mathUtils.divide(1, 0), "Divide by zero should throw");		// Lambda function (introdotta con JUnit 4)
		System.out.println("Test divide run");
	}
	
	
	@Test
	@DisplayName("Test multiply method")
	@Tag("Math")
	public void testMultiply() {
//		assertEquals(4, mathUtils.multiply(2, 2), "Should return the right product");
		
		assertAll(
				() -> assertEquals(4, mathUtils.multiply(2, 2)),
				() -> assertEquals(0, mathUtils.multiply(2, 0)),
				() -> assertEquals(-2, mathUtils.multiply(2, -1)),
				() -> assertEquals(8, mathUtils.multiply(2, 4))
			);
	}
	
	
	@RepeatedTest(5)
	@DisplayName("Test to compute the circle Area")
	@Tag("Circle")
	// Se utilizzo l'annotazione RepetitionTest, tramite l'interfaccia RepetitionInfo passata come parametro al metodo
	// ottengo le informazioni relative all'iterazione
	public void testComputeCircleArea(RepetitionInfo repetition) {
		if(repetition.getCurrentRepetition() == 2)
			fail("Forced fail on repetition 2");
			
		assertEquals(314.1592653589793, mathUtils.computeCircleArea(10), "Should return the calculate circle area"); 
	}
	
	
	@Test
	@Disabled
	public void testDisabled() {
		fail("Test fail intentionally");
	}
	
	
	@Test
	@EnabledOnOs(OS.WINDOWS)
	@DisplayName("Test based on OS")
	public void testForWindowx() {
		System.out.println("Test to run only on Windows machines");
	}
}
