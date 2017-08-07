package sk.mary.vcsannotstats.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BlameProcessor {

    public List<String> deleteUnnecessaryLines(String blameFile) {
        File newBlameFile = new File(blameFile);
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.lines(Paths.get(newBlameFile.getAbsolutePath())).filter(s -> !(StringUtils.isBlank(s) || s.contains("git blame -f"))).collect(Collectors.toList());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return lines;
    }

}
