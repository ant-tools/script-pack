package js.tools.script.pack;

import java.io.IOException;
import java.io.Reader;

class FilterReader extends Reader
{
  private Config config;
  private Reader reader;

  public FilterReader(Reader reader, Config config)
  {
    this.config = config;
    this.reader = reader;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException
  {
    int i = 0;
    for(; i < len; ++i) {
      int c = read();
      if(c == -1) {
        if(i == 0) {
          return -1;
        }
        break;
      }
      cbuf[off + i] = (char)c;
    }
    return i;
  }

  @Override
  public int read() throws IOException
  {
    // aaaaaa/*....*/aaaaaa$assert.(..()..) ;aaaaaa

    int c = reader.read();
    if(c == -1) {
      return -1;
    }

    if(config.removeApiDoc) {
      if(c == '/' && lookForString("*")) {
        if(!rejectComment()) {
          return -1;
        }
        return read();
      }
    }

    if(config.removeAssertions) {
      if(c == '$' && lookForString("assert(")) {
        if(!rejectAssert()) {
          return -1;
        }
        return read();
      }
    }

    return c;
  }

  private boolean lookForString(String string) throws IOException
  {
    // * or assert

    reader.mark(string.length());

    for(int i = 0; i < string.length(); ++i) {
      int c = reader.read();
      if(c != string.charAt(i)) {
        reader.reset();
        return false;
      }
    }

    return true;
  }

  private boolean rejectComment() throws IOException
  {
    // /* .... */ ?CR ?LF

    boolean asterisk = false;
    for(;;) {
      int c = reader.read();
      if(c == -1) {
        return false;
      }
      if(c == '/' && asterisk) {
        break;
      }
      asterisk = c == '*';
    }

    for(;;) {
      reader.mark(1);
      int c = reader.read();
      if(c == -1) {
        return false;
      }
      if(!Character.isWhitespace(c)) {
        reader.reset();
        break;
      }
    }

    return true;
  }

  private boolean rejectAssert() throws IOException
  {
    // $assert ?WS ( *CH ( *CH ) *CH ) ?WS ?; ?CR ?LF

    int parenthesisCount = 1;

    while(parenthesisCount > 0) {
      int c = reader.read();
      if(c == -1) {
        return false;
      }
      if(c == '(') {
        ++parenthesisCount;
      }
      else if(c == ')') {
        --parenthesisCount;
      }
    }

    reader.mark(1);
    if(reader.read() != ';') {
      reader.reset();
    }

    for(;;) {
      reader.mark(1);
      int c = reader.read();
      if(c == -1) {
        return false;
      }
      if(!Character.isWhitespace(c)) {
        reader.reset();
        break;
      }
    }

    return true;
  }

  @Override
  public void close() throws IOException
  {
    reader.close();
  }
}
