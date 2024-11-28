package dev.andela.assessment2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Count Lines of Code
 */
public class CountLOC {

    // Pattern for splitting a String by Newline
    static final Pattern SPLIT_PATTERN = Pattern.compile("\\R");

    // Lines that are Single line comment
    static final Predicate<String> isBlankLine = String::isBlank;
    static final Predicate<String> isSingleLineComment = line -> line.startsWith("//") || (line.startsWith("/*") && line.endsWith("*/"));

    // Lines that are part of multi-line comments
    // Multi-line comments require state management
    static final BiPredicate<String, Boolean> isStartLineWithComplexComment = (line, isInMultiLineComment) -> !isInMultiLineComment && ( line.startsWith("/*") && !line.contains("*/") );
    static final BiPredicate<String, Boolean> isEndLineWithComplexComment = (line, isInMultiLineComment) -> isInMultiLineComment &&  ( line.endsWith("*/") );

    // Consider empty or malformed inputs
    // TODO

    public static long count(String sourceCode) {
        // 1- read line by line
        // 2- add some condition to ignore the comments cases

        AtomicBoolean isInMultiLineComment = new AtomicBoolean(false);
        return SPLIT_PATTERN.splitAsStream(sourceCode)
                .map(String::trim)
                .filter( isBlankLine.negate().and(isSingleLineComment.negate()) )
                .filter( s -> {

                    if(isStartLineWithComplexComment.test(s, isInMultiLineComment.get())) {
                        isInMultiLineComment.set(true);
                    }

                    boolean shouldSkip = isInMultiLineComment.get();

                    if (isEndLineWithComplexComment.test(s, isInMultiLineComment.get())) {
                        isInMultiLineComment.set(false);
                    }

                    return !shouldSkip; // Skip lines in multi-line comments
                })
                .count();
    }

}

/**
 * Suggestions for Improvement
 * 1- Robust Handling of Edge Cases:
 *  - Blank Input: Consider adding a condition to handle empty or null sourceCode strings to avoid potential errors.
 *  - Malformed Comments: Test for edge cases like unclosed multi-line comments (/* without a closing ) to ensure the logic behaves predictably.
 *    * Example: "/* Missing end". The current code will assume subsequent lines are in a comment block indefinitely
 *  - Nested Comments: While rare in Java, ensure nested multi-line comments don’t break the logic if encountered.
 *
 * 2- Naming Improvements
 * isStartLineWithComplexComment and isEndLineWithComplexComment could have shorter names, such as isStartOfBlockComment and isEndOfBlockComment, for better readability.
 *
 * 3- An other solution would be with using the .reduce
 *  - Why Use reduce?
 *  - Immutable Streams: Streams in Java are inherently immutable. reduce allows combining stream elements into a single result by applying an accumulator.
 * - Tracking Multiple Values: We need to track two counters: one for code lines and one for comment lines. Using an int[] makes it easy to update both counters while processing each line.
 * - State Sharing: By using an array, each element in the stream contributes to the cumulative total (code lines and comment lines).
 */
