package js.tools.script.pack.test;

import java.util.List;

import js.tools.commons.util.Classes;
import js.tools.script.pack.Main;
import junit.framework.TestCase;

public class ConfigUnitTest extends TestCase
{
  public void testParseFlags() throws Throwable
  {
    String[] args = new String[]
    {
        "-verbose", "-debug", "-nice", "-compress", "-obfuscate", "-remove-assertions", "-remove-unused", "-replace-constants"
    };
    Object config = Classes.invoke(Main.class, "getConfig", (Object)args);

    assertTrue((boolean)Classes.getFieldValue(config, "verbose"));
    assertTrue((boolean)Classes.getFieldValue(config, "debug"));
    assertTrue((boolean)Classes.getFieldValue(config, "nice"));
    assertTrue((boolean)Classes.getFieldValue(config, "compress"));
    assertTrue((boolean)Classes.getFieldValue(config, "obfuscate"));
    assertTrue((boolean)Classes.getFieldValue(config, "removeAssertions"));
    assertTrue((boolean)Classes.getFieldValue(config, "removeUnused"));
    assertTrue((boolean)Classes.getFieldValue(config, "replaceConstants"));
  }

  public void testParseOmnideps() throws Throwable
  {
    String[] args = new String[]
    {
        "-omnideps", "legacy", "bootstrap"
    };
    Object config = Classes.invoke(Main.class, "getConfig", (Object)args);

    List<String> omnideps = Classes.getFieldValue(config, "omnideps");
    assertEquals(2, omnideps.size());
    assertEquals("legacy", omnideps.get(0));
    assertEquals("bootstrap", omnideps.get(1));
  }
}
