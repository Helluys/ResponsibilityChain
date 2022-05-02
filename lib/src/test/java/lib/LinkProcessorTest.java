package lib;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.helluys.patterns.example.chain.link.CommandChain;

public class LinkProcessorTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@Test
	public void unknownSource() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("source",
				"[A]payload");
		assertEquals("unknown command source : 'source'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void unknownTextCommand() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("text",
				"[D]payload");
		assertEquals("unknown command name : 'D'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void erroneousTextCommand() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("text",
				"(A)payload");
		assertEquals("erroneous command : 'Invalid text text'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void textCommandA() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("text",
				"[A]payload");
		assertEquals("A : 'payload'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void textCommandB() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("text",
				"[B]payload");
		assertEquals("B : 'payload'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void textCommandC() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("text",
				"[C]payload");
		assertEquals("C : 'payload'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void unknownJsonCommand() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("json",
				"{\"name\":\"D\",\"text\":\"payload\"}");
		assertEquals("unknown command name : 'D'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void erroneousJsonCommand() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("json",
				"(\"name\":\"A\",\"text\":\"payload\")");
		assertEquals(
				"erroneous command : 'Invalid JSON A JSONObject text must begin with '{' at 1 [character 2 line 1]'"
						+ System.lineSeparator(),
						outContent.toString());
	}

	@Test
	public void jsonCommandA() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("json",
				"{\"name\":\"A\",\"text\":\"payload\"}");
		assertEquals("A : 'payload'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void jsonCommandB() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("json",
				"{\"name\":\"B\",\"text\":\"payload\"}");
		assertEquals("B : 'payload'" + System.lineSeparator(), outContent.toString());
	}

	@Test
	public void jsonCommandC() {
		new CommandChain(Stream.of("text").collect(toSet()), Stream.of("json").collect(toSet())).process("json",
				"{\"name\":\"C\",\"text\":\"payload\"}");
		assertEquals("C : 'payload'" + System.lineSeparator(), outContent.toString());
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}
}
