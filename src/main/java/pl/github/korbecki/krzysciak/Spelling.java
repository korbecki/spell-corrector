package pl.github.korbecki.krzysciak;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Spelling {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyząźćóęłźż";
    public static final String WORD_REGEX = "[^a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]";
    public static final String EMPTY_CHARACTER = " ";
    private final Map<String, Integer> dict = new HashMap<>();

    public Spelling(File dictionaryFile) throws Exception {
        Stream.of(new String(Files.readAllBytes(dictionaryFile.toPath())).toLowerCase()
                        .replaceAll(WORD_REGEX, EMPTY_CHARACTER)
                        .split(EMPTY_CHARACTER))
                        .forEach((word) -> {
                            dict.compute(word.trim(), (k, v) -> v == null ? 1 : v + 1);
                        });
    }

    Stream<String> edits1(final String word) {
        Stream<String> deletes = IntStream.range(0, word.length())
                .mapToObj((i) -> word.substring(0, i) + word.substring(i + 1));
        Stream<String> replaces = IntStream.range(0, word.length())
                .boxed()
                .flatMap((i) -> ALPHABET.chars().mapToObj((c) -> word.substring(0, i) + (char) c + word.substring(i + 1)));
        Stream<String> inserts = IntStream.range(0, word.length() + 1)
                .boxed().flatMap((i) -> ALPHABET.chars().mapToObj((c) -> word.substring(0, i) + (char) c + word.substring(i)));
        Stream<String> transposes = IntStream.range(0, word.length() - 1)
                .mapToObj((i) -> word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2));
        return Stream.of(deletes, replaces, inserts, transposes).flatMap((x) -> x);
    }

    private Stream<String> known(Stream<String> words) {
        return words.filter(dict::containsKey);
    }

    public String correct(String word) {
        Optional<String> e1 = known(edits1(word)).max((a, b) -> dict.get(a) - dict.get(b));
        if (e1.isPresent())
            return dict.containsKey(word) ? word : e1.get();

        Optional<String> e2 = known(edits1(word).flatMap(this::edits1)).max((a, b) -> dict.get(a) - dict.get(b));
        return (e2.orElse(word));
    }
}