package js.tools.script.pack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import js.tools.commons.util.Files;
import js.tools.commons.util.Params;

public class Packer
{
  private static final String ENABLE_JS_DEBUG = "__js_debug__ = true;";

  private Config config;
  private Writer archive;
  private File sourcesDirectory;
  private List<File> excludes = new ArrayList<File>();
  private List<File> omnideps = new ArrayList<File>();
  private Set<File> processed = new HashSet<File>();

  public Packer(Config config) throws IOException
  {
    this.config = config;
    archive = new BufferedWriter(new FileWriter(config.file));
    sourcesDirectory = config.sourcepath;
    for(String excludePackage : config.excludes) {
      excludes.add(new File(sourcesDirectory, Files.dot2path(excludePackage)));
    }
    for(String omnidep : config.omnideps) {
      omnideps.add(new File(sourcesDirectory, Files.dot2path(omnidep, ".js")));
    }
  }

  public void pack() throws IOException
  {
    if(config.debug) {
      archive.write(ENABLE_JS_DEBUG);
      archive.write(System.getProperty("line.separator"));
    }
    parseOmnidepsList();
    parseFileSystemTree(sourcesDirectory);
    archive.close();
  }

  private void parseOmnidepsList() throws IOException
  {
    for(File f : omnideps) {
      if(processed.add(f)) {
        addToArchive(new Source(sourcesDirectory, f));
      }
    }
  }

  private void parseFileSystemTree(File file) throws IOException
  {
    Params.isDirectory(file, "File");
    if(isExcluded(file)) {
      return;
    }
    
    for(File f : file.listFiles()) {
      if(isHidden(f)) {
        continue;
      }
      if(f.isDirectory()) {
        parseFileSystemTree(f);
        continue;
      }
      if(isPackageInfo(f)) {
        continue;
      }
      if(isSource(f) && processed.add(f)) {
        addToArchive(new Source(sourcesDirectory, f));
      }
    }
  }

  private void addToArchive(Source source) throws IOException
  {
    // first process dependencies
    for(File dependency : source.getDependencies()) {
      if(isSource(dependency) && processed.add(dependency)) {
        addToArchive(new Source(sourcesDirectory, dependency));
      }
    }

    // then process source code
    if(config.verbose) {
      System.out.println("Processing: " + source.getFile());
    }
    if(config.nice) {
      source.nice(archive, config);
    }
    else {
      source.serialize(archive);
    }
  }

  private boolean isExcluded(File file)
  {
    for(File excluded : excludes) {
      if(excluded.equals(file)) {
        if(config.verbose) {
          System.out.println("Exclude: " + file);
        }
        return true;
      }
    }
    return false;
  }

  private boolean isHidden(File file)
  {
    return file.getName().charAt(0) == '.';
  }

  private boolean isPackageInfo(File file)
  {
    return file.getName().endsWith("package-info.js");
  }

  private boolean isSource(File file)
  {
    return file.exists() && file.getName().endsWith(".js");
  }
}
