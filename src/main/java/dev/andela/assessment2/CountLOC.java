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
    static final BiPredicate<String, Boolean> isStartLineWithComplexComment = (line, isInMultiLineComment) -> isInMultiLineComment.equals(false) && ( line.startsWith("/*") && !line.contains("*/") );
    static final BiPredicate<String, Boolean> isEndLineWithComplexComment = (line, isInMultiLineComment) -> isInMultiLineComment.equals(true) &&  ( line.endsWith("*/") );

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

                    if (isEndLineWithComplexComment.test(s, isInMultiLineComment.get())) {
                        isInMultiLineComment.set(false);

                        //if(isInMultiLineComment.get()) {
                        //    isInMultiLineComment.set(false);
                        //    return true; // it's '*/' we won't count then filter
                        //}

                    }

                    return !isInMultiLineComment.get(); // if true => be fired and don't count
                })
                .count();
    }

}
