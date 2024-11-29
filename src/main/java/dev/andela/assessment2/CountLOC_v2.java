package dev.andela.assessment2;

import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

public class CountLOC_v2 {
    static final Pattern SPLIT_PATTERN = Pattern.compile("\\R");

    static final Predicate<String> isBlankLine = String::isBlank;
    static final Predicate<String> isSingleLineComment = line -> line.startsWith("//") || (line.startsWith("/*") && line.endsWith("*/"));
    static final BiPredicate<String, Boolean> isStartOfBlockComment = (line, isInComment) -> !isInComment && line.startsWith("/*") && !line.contains("*/");
    static final BiPredicate<String, Boolean> isEndOfBlockComment = (line, isInComment) -> isInComment && line.endsWith("*/");

    public static long count(String sourceCode) {
        if (sourceCode == null || sourceCode.isBlank()) {
            return 0;
        }

        return SPLIT_PATTERN.splitAsStream(sourceCode)
                .map(String::trim)
                .collect(() -> new State(false, 0), // Initial state
                        (state, line) -> {
                            // Update the state based on the line
                            if (isStartOfBlockComment.test(line, state.isInMultiLineComment)) {
                                state.isInMultiLineComment = true;
                            } else if (state.isInMultiLineComment) {
                                if (isEndOfBlockComment.test(line, state.isInMultiLineComment)) {
                                    state.isInMultiLineComment = false;
                                }
                            } else if (!isBlankLine.test(line) && !isSingleLineComment.test(line)) {
                                state.lineCount++;
                            }
                        },
                        (state1, state2) -> {
                            // Combine states (not relevant for sequential streams)
                            state1.lineCount += state2.lineCount;
                        })
                .lineCount; // Return the final count
    }

    static class State {
        boolean isInMultiLineComment;
        int lineCount;

        State(boolean isInMultiLineComment, int lineCount) {
            this.isInMultiLineComment = isInMultiLineComment;
            this.lineCount = lineCount;
        }
    }
}
