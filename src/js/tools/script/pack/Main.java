package js.tools.script.pack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main
{
  public static void main(String[] args)
  {
    try {
      Packer packer = new Packer(getConfig(args));
      packer.pack();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private static Config getConfig(String[] args)
  {
    List<List<String>> optionsList = new ArrayList<List<String>>();
    List<String> optionValues = null;
    for(int i = 0; i < args.length; ++i) {
      if(args[i].charAt(0) == '-') {
        optionValues = new ArrayList<String>();
        optionsList.add(optionValues);
      }
      optionValues.add(args[i]);
    }

    Config config = new Config();
    for(List<String> option : optionsList) {
      String optionName = option.get(0);
      if(optionName.equals("-verbose")) {
        config.verbose = true;
        continue;
      }
      if(optionName.equals("-sourcepath")) {
        config.sourcepath = new File(option.get(1));
        continue;
      }
      if(optionName.equals("-excludes")) {
        config.excludes = option.subList(1, option.size());
        continue;
      }
      if(optionName.equals("-f") || optionName.equals("-file")) {
        config.file = new File(option.get(1));
        continue;
      }
      if(optionName.equals("-omnideps")) {
        config.omnideps = option.subList(1, option.size());
        continue;
      }
      if(optionName.equals("-debug")) {
        config.debug = true;
        continue;
      }
      if(optionName.equals("-nice")) {
        config.nice = true;
        continue;
      }
      if(optionName.equals("-remove-apidoc")) {
        config.removeApiDoc = true;
        continue;
      }
      if(optionName.equals("-remove-assertions")) {
        config.removeAssertions = true;
        continue;
      }
      if(optionName.equals("-compress")) {
        config.compress = true;
        continue;
      }
      if(optionName.equals("-obfuscate")) {
        config.obfuscate = true;
        continue;
      }
      if(optionName.equals("-remove-unused")) {
        config.removeUnused = true;
        continue;
      }
      if(optionName.equals("-replace-constants")) {
        config.replaceConstants = true;
      }
    }
    return config;
  }
}
