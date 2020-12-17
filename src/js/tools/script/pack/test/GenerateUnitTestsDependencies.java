package js.tools.script.pack.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import js.tools.commons.dep.Dependency;
import js.tools.commons.dep.DependencyScanner;
import js.tools.commons.util.Files;

public class GenerateUnitTestsDependencies
{
  private static final File CODE_BASE = new File("D:/docs/workspaces/phoenix/js-lib/client/src");
  private static final String JS_EXT = ".js";

  private static Set<String> processed = new HashSet<String>();

  public static void main(String[] args) throws FileNotFoundException
  {
    scan("bootstrap");
    scan("js.tests.dom.ElementUnitTests");
  }

  private static void scan(String className) throws FileNotFoundException
  {
    File codebase = className.equals("js.tests.dom.ElementUnitTests") ? new File("D:/docs/workspaces/phoenix/js-lib/client.tests/src") : CODE_BASE;
    File file = new File(codebase, Files.dot2path(className, JS_EXT));
    if(!file.exists()) {
      // System.err.println(file);
      return;
    }

    DependencyScanner scanner = new DependencyScanner();
    for(Dependency d : scanner.getDependencies(file)) {
      if(d.isStrong() && processed.add(d.getName())) {
        scan(d.getName());
      }
    }

    System.out.println(String.format("<script src='../../../../../client/src/%s.js' type='text/javascript'></script>", Files.dot2path(className)));
    // System.out.println(className);

    scanner = new DependencyScanner();
    for(Dependency d : scanner.getDependencies(file)) {
      if(!d.isStrong() && processed.add(d.getName())) {
        scan(d.getName());
      }
    }
  }
}
