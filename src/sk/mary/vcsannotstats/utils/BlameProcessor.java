package sk.mary.vcsannotstats.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class BlameProcessor {

    public List<String> deleteUnnecessaryLines(String blameFile) {
        File newBlameFile = new File(blameFile);
        List<String> lines = new ArrayList<>();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            lines = Files.lines(Paths.get(newBlameFile.getAbsolutePath())).filter(s -> !(StringUtils.isBlank(s) || s.contains("git blame -f"))).collect(Collectors.toList());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return lines;
    }

    public String parseCommitFromLine(String line){
        return line.split("( )")[0];
    }

    public String parseNameFromLine(String line){
        return line.split("(\\()",3)[1].split(" ")[0];
    }

    public String parseDateFromLine(String line){
        String subline = line.trim();
        subline = subline.split("(\\()")[1].split("( )",2)[1].split("(\\+)")[0];
        return subline.substring(0, subline.length()-1).trim();
    }

    public Set<String> getContributors(Collection<List<String>> allLines){
        Set<String> contributors = new HashSet<>();
        allLines.forEach(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) {
                strings.stream().forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        if(!"00000000".equals(parseCommitFromLine(s))) contributors.add(parseNameFromLine(s));
                    }
                });
            }
        });
        return contributors;
    }

    public Set<String> getCommits(Collection<List<String>> allLines){
        Set<String> commits = new HashSet<>();
        allLines.forEach(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) {
                strings.stream().forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        String commit = parseCommitFromLine(s);
                        if (!"00000000".equals(commit)) commits.add(commit);
                    }
                });
            }
        });
        return commits;
    }

    public String countUncommitted(Collection<List<String>> allLines){
        List<String> commits = new ArrayList<>();
        allLines.forEach(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) {
                strings.stream().forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        String commit = parseCommitFromLine(s);
                        if ("00000000".equals(commit)) commits.add(commit);
                    }
                });
            }
        });
        return String.valueOf(commits.size());
    }
}
