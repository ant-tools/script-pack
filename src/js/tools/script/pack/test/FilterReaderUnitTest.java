package js.tools.script.pack.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import js.tools.commons.util.Classes;
import js.tools.commons.util.Files;
import junit.framework.TestCase;

public class FilterReaderUnitTest extends TestCase
{
  public void testPositiveLookForString() throws Exception
  {
    StringReader source = new StringReader("assert(...)");
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, null);
    assertTrue((boolean)Classes.invoke(reader, "lookForString", "assert"));
  }

  public void testNegativeLookForString() throws Exception
  {
    StringReader source = new StringReader("assets(...)");
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, null);
    assertFalse((boolean)Classes.invoke(reader, "lookForString", "assert"));
  }

  public void testRejectComment() throws Exception
  {
    StringReader source = new StringReader("... bla bla bla / * ** /** */Q");
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);
    Classes.invoke(reader, "rejectComment");
    assertEquals('Q', reader.read());
  }

  public void testPositiveRemoveApiDoc() throws Exception
  {
    StringReader source = new StringReader("1234/**5678*/90");
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "removeApiDoc", true);
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);

    StringWriter writer = new StringWriter();
    Files.copy(reader, writer);
    assertEquals("123490", writer.toString());
  }

  public void testNegativeRemoveApiDoc() throws Exception
  {
    StringReader source = new StringReader("1234/**5678*/90");
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "removeApiDoc", false);
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);

    StringWriter writer = new StringWriter();
    Files.copy(reader, writer);
    assertEquals("1234/**5678*/90", writer.toString());
  }

  public void testRemoveApiDocCRLF() throws Exception
  {
    StringReader source = new StringReader("1234\r\n/**5678*/\r\n90\r\n");
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "removeApiDoc", true);
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);

    StringWriter writer = new StringWriter();
    Files.copy(reader, writer);
    assertEquals("1234\r\n90\r\n", writer.toString());
  }

  public void testPositiveRemoveAssert() throws Exception
  {
    StringReader source = new StringReader("1234$assert( ... ( ... ()))90");
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "removeAssertions", true);
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);

    StringWriter writer = new StringWriter();
    Files.copy(reader, writer);
    assertEquals("123490", writer.toString());
  }

  public void testNegativeRemoveAssert() throws Exception
  {
    StringReader source = new StringReader("1234$assert ( ... ( ... ()))90");
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "removeAssertions", false);
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);

    StringWriter writer = new StringWriter();
    Files.copy(reader, writer);
    assertEquals("1234$assert ( ... ( ... ()))90", writer.toString());
  }

  public void testRemoveAssertCRLF() throws Exception
  {
    StringReader source = new StringReader("1234\r\n$assert( ... ( ... ()));\r\n90");
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "removeAssertions", true);
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);

    StringWriter writer = new StringWriter();
    Files.copy(reader, writer);
    assertEquals("1234\r\n90", writer.toString());
  }

  public void testSourceFile() throws Exception
  {
    Reader source = new BufferedReader(new FileReader("fixture/source.js"));

    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "removeApiDoc", true);
    Classes.setFieldValue(config, "removeAssertions", true);
    Reader reader = Classes.newInstance("js.tools.script.pack.FilterReader", source, config);

    Files.copy(reader, new FileWriter("fixture/output.js"));
  }
}
