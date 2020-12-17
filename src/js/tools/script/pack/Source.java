package js.tools.script.pack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import js.tools.commons.dep.Dependency;
import js.tools.commons.dep.DependencyScanner;
import js.tools.commons.util.Files;

public class Source
{
  private File sourcesDirectory;
  private File file;
  private List<File> dependencies = new ArrayList<File>();
  private DependencyScanner dependencyScanner;

  public Source(File sourcesDirectory, File file) throws IOException
  {
    this.sourcesDirectory = sourcesDirectory;
    this.file = file;
    this.dependencyScanner = new DependencyScanner();
    findOutDependencies();
  }

  public List<File> getDependencies()
  {
    return dependencies;
  }

  public File getFile()
  {
    return file;
  }

  public void serialize(Writer writer) throws IOException
  {
    writer.write(dependencyScanner.toSource());
  }

  public void nice(Writer writer, Config config) throws IOException
  {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    BufferedReader bufferedReader = new BufferedReader(new FilterReader(reader, config));
    String line;
    while((line = bufferedReader.readLine()) != null) {
      writer.write(line);
      writer.write(System.getProperty("line.separator"));
    }
    bufferedReader.close();
  }

  @Override
  public String toString()
  {
    return file.getPath();
  }

  private void findOutDependencies() throws FileNotFoundException
  {
    Collection<Dependency> deps = dependencyScanner.getDependencies(file);
    for(Dependency dep : deps) {
      if(dep.isStrong()) {
        dependencies.add(getSourceFile(dep.getName()));
      }
    }
  }

  private File getSourceFile(String jsClassName)
  {
    return new File(sourcesDirectory, Files.dot2path(jsClassName, ".js"));
  }
}
