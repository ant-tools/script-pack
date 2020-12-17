package js.tools.script.pack.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;

import js.tools.commons.dep.Dependency;
import js.tools.commons.dep.DependencyScanner;
import js.tools.commons.util.Classes;
import js.tools.script.pack.Packer;
import junit.framework.TestCase;

public class PackerUnitTests extends TestCase
{
  public void testNumberFormat() throws FileNotFoundException
  {
    File sourcesRepository = new File("D:/docs/workspaces/bbnet/js-lib/client/src");
    File sourceFile = new File(sourcesRepository, "js/format/Number.js");
    DependencyScanner scanner = new DependencyScanner();
    Collection<Dependency> deps = scanner.getDependencies(sourceFile);

    for(Dependency dep : deps) {
      System.out.println(dep);
    }
  }

  public void testPacker() throws Exception
  {
    Object config = Classes.newInstance("js.tools.script.pack.Config");
    Classes.setFieldValue(config, "verbose", true);
    Classes.setFieldValue(config, "debug", true);
    Classes.setFieldValue(config, "nice", true);
    Classes.setFieldValue(config, "sourcepath", new File("script"));
    Classes.setFieldValue(config, "file", new File("pack-test.js"));
    Classes.setFieldValue(config, "excludes", Collections.emptyList());
    Classes.setFieldValue(config, "omnideps", Collections.emptyList());

    Packer packer = Classes.newInstance("js.tools.script.pack.Packer", config);
    packer.pack();
  }
}
