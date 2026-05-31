package highlighting.regex;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import org.junit.jupiter.api.Test;

import java.util.List;

class RegexHighlighterTest {

    private final RegexHighlighter highlighter = new RegexHighlighter();

    // -----------------------------
    // 1. einfache Fälle
    // -----------------------------

    @Test
    void simpleNoOverlap() {
        List<HighlightRegion> result =
            highlighter.resolveConflicts(
                highlighter.collectMatches("class Test"));

        assertFalse(result.isEmpty());
    }

    // -----------------------------
    // 2. leerer String
    // -----------------------------

    @Test
    void emptyString() {
        List<HighlightRegion> result =
            highlighter.resolveConflicts(
                highlighter.collectMatches(""));

        assertTrue(result.isEmpty());
    }

    // -----------------------------
    // 3. kein Match
    // -----------------------------

    @Test
    void noMatches() {
        List<HighlightRegion> result =
            highlighter.resolveConflicts(
                highlighter.collectMatches("hello world"));

        assertTrue(result.isEmpty());
    }

    // -----------------------------
    // 4. aufeinanderfolgende Regionen
    // [0,5) und [5,10)
    // -----------------------------

    @Test
    void adjacentRegionsAreKept() {
        List<HighlightRegion> input = List.of(
            new HighlightRegion(0, 5, null),
            new HighlightRegion(5, 10, null)
        );

        List<HighlightRegion> result = highlighter.resolveConflicts(input);

        assertEquals(2, result.size());
    }

    // -----------------------------
    // 5. Overlap: Keyword im Kommentar
    // -----------------------------
    @Test
    void keywordInsideCommentOverlap() {
        List<HighlightRegion> regions =
            highlighter.collectMatches("// class Test");

        List<HighlightRegion> resolved =
            highlighter.resolveConflicts(regions);

        // Erwartung: Kommentar gewinnt (kommt vor Keyword in Tokenliste)
        assertFalse(resolved.isEmpty());
    }

    // -----------------------------
    // 6. Javadoc vs Blockcomment Overlap
    // -----------------------------
    @Test
    void javadocAndBlockCommentOverlap() {
        String text = "/** doc */ /* normal */";

        List<HighlightRegion> regions =
            highlighter.collectMatches(text);

        List<HighlightRegion> resolved =
            highlighter.resolveConflicts(regions);

        assertFalse(resolved.isEmpty());
    }

    // -----------------------------
    // 7. Overlap-Verhalten allgemein
    // -----------------------------
    @Test
    void overlappingRegionsKeepFirst() {
        List<HighlightRegion> input = List.of(
            new HighlightRegion(0, 10, null),
            new HighlightRegion(5, 15, null)
        );

        List<HighlightRegion> result = highlighter.resolveConflicts(input);

        // nur die erste Region bleibt
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).start());
        assertEquals(10, result.get(0).end());
    }

}
