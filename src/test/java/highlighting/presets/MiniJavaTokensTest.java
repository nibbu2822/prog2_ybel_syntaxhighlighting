package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.regex.Token;
import java.util.regex.Matcher;
import org.junit.jupiter.api.Test;

class MiniJavaTokensTest {

  private boolean matches(Token token, String text) {
    Matcher matcher = token.pattern().matcher(text);
    return matcher.find();
  }

  @Test
  void keywordAtBeginning() {
    Token keyword = MiniJavaTokens.defaultTokens().get(5);

    assertTrue(matches(keyword, "class Test {}"));
  }

  @Test
  void keywordInMiddle() {
    Token keyword = MiniJavaTokens.defaultTokens().get(5);

    assertTrue(matches(keyword, "public class Test {}"));
  }

  @Test
  void keywordAtEnd() {
    Token keyword = MiniJavaTokens.defaultTokens().get(5);

    assertTrue(matches(keyword, "myKeyword return"));
  }

  @Test
  void multipleKeywordsInOneText() {
    Token keyword = MiniJavaTokens.defaultTokens().get(5);

    Matcher matcher = keyword.pattern().matcher("public class Test { return null; }");

    int count = 0;
    while (matcher.find()) {
      count++;
    }

    assertEquals(4, count);
  }

  @Test
  void noKeywordFound() {
    Token keyword = MiniJavaTokens.defaultTokens().get(5);

    assertFalse(matches(keyword, "hello world"));
  }

  @Test
  void annotationAtLineStart() {
    Token annotation = MiniJavaTokens.defaultTokens().get(4);

    assertTrue(matches(annotation, "@Override"));
  }

  @Test
  void annotationWithLeadingWhitespace() {
    Token annotation = MiniJavaTokens.defaultTokens().get(4);

    assertTrue(matches(annotation, "    @Test"));
  }

  @Test
  void lineCommentContainingKeyword() {
    Token comment = MiniJavaTokens.defaultTokens().get(1);

    assertTrue(matches(comment, "// public class return"));
  }

  @Test
  void stringContainingDoubleSlash() {
    Token string = MiniJavaTokens.defaultTokens().get(0);

    assertTrue(matches(string, "\"https://example.com\""));
  }

  @Test
  void stringContainingBlockCommentMarkers() {
    Token string = MiniJavaTokens.defaultTokens().get(0);

    assertTrue(matches(string, "\"/* not a comment */\""));
  }

  @Test
  void blockCommentMatch() {
    Token blockComment = MiniJavaTokens.defaultTokens().get(2);

    assertTrue(matches(blockComment, "/* comment text */"));
  }

  @Test
  void javadocCommentMatch() {
    Token javadoc = MiniJavaTokens.defaultTokens().get(3);

    assertTrue(matches(javadoc, "/** documentation */"));
  }

  @Test
  void charLiteralMatch() {
    Token charLiteral = MiniJavaTokens.defaultTokens().get(6);

    assertTrue(matches(charLiteral, "'a'"));
  }

  @Test
  void typeMatch() {
    Token type = MiniJavaTokens.defaultTokens().get(7);

    assertTrue(matches(type, "int value = 5;"));
  }
}
