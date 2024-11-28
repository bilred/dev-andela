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
    static final Predicate<String> isBlankLine = String::isBlank;
    static final Predicate<String> isSingleLineComment = s -> s.startsWith("//") || (s.startsWith("/*") && s.endsWith("*/"));

    // Lines that are part of multi-line comments
    // Multi-line comments require state management
    static final BiPredicate<String, Boolean> startLineWithComplexComment = (s, b) -> b.equals(false) && ( s.startsWith("/*") && !s.contains("*/") );
    static final BiPredicate<String, Boolean> endLineWithComplexComment = (s, b) -> b.equals(true) &&  ( s.endsWith("*/") );

    // Consider empty or malformed inputs
    // TODO

    public static long count(String sourceCode) {
        // 1- read line by line
        // 2- add some condition to ignore the comments cases

        AtomicBoolean inMultiLineComment = new AtomicBoolean(false);
        return SPLIT_PATTERN.splitAsStream(sourceCode)
                .map(String::trim)
                .filter( isBlankLine.negate().and(isSingleLineComment.negate()) )
                .filter( s -> {

                    if(isStartOfMultiLineComment(s, inMultiLineComment.get())) {
                        inMultiLineComment.set(true);
                    }

                    if (isEndOfMultiLineComment(s, inMultiLineComment.get())) {
                        inMultiLineComment.set(false);

                        //if(inMultiLineComment.get()) {
                        //    inMultiLineComment.set(false);
                        //    return true; // it's '*/' we won't count then filter
                        //}

                    }

                    return !inMultiLineComment.get(); // if true => be fired and don't count
                })
                .count();
    }

    private static boolean isStartOfMultiLineComment(String line, boolean inMultiLineComment) {
        return startLineWithComplexComment.test(line, inMultiLineComment);
    }

    private static boolean isEndOfMultiLineComment(String line, boolean inMultiLineComment) {
        return endLineWithComplexComment.test(line, inMultiLineComment);
    }

}
