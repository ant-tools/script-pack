package js.tools.script.pack;

import java.io.File;
import java.util.Collections;
import java.util.List;

class Config
{
  boolean verbose;
  boolean debug;
  boolean nice;
  boolean compress;
  boolean obfuscate;
  boolean removeApiDoc;
  boolean removeAssertions;
  boolean removeUnused;
  boolean replaceConstants;

  File sourcepath;
  List<String> excludes = Collections.emptyList();
  File file;
  List<String> omnideps = Collections.emptyList();
}